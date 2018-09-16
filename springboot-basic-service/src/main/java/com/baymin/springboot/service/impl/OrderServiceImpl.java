package com.baymin.springboot.service.impl;

import com.baymin.springboot.common.constant.RequestConstant;
import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IOrderService;
import com.baymin.springboot.store.dao.IInvoiceDao;
import com.baymin.springboot.store.dao.IOrderDao;
import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.enumconstant.*;
import com.baymin.springboot.store.payload.UserOrderRequest;
import com.baymin.springboot.store.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.baymin.springboot.common.constant.RequestConstant.*;
import static com.baymin.springboot.common.exception.ErrorDescription.ORDER_INFO_NOT_CORRECT;
import static com.baymin.springboot.store.enumconstant.CareType.*;

@Service
@Transactional
public class OrderServiceImpl implements IOrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private IOrderDao orderDao;

    @Autowired
    private IInvoiceDao invoiceDao;

    @Autowired
    private IEvaluateRepository evaluateRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IInvoiceRepository invoiceRepository;

    @Autowired
    private IServiceStaffRepository serviceStaffRepository;

    @Autowired
    private IPayRecordRepository payRecordRepository;

    @Autowired
    private IOrderStaffChangeRepository orderStaffChangeRepository;

    @Autowired
    private IUserProfileRepository userProfileRepository;

    @Autowired
    private IOrderExtRepository orderExtRepository;

    @Override
    public Order saveUserOrder(UserOrderRequest request) {
        Invoice invoice = request.getInvoice();
        Order order = new Order();

        order.setOrderUserId(request.getOrderUserId());
        order.setOrderTime(new Date());
        order.setCareType(request.getOrderType());
        if (StringUtils.equals(RequestConstant.OFFLINE, request.getPayway())) {
            order.setPayWay(PayWay.PAY_ONLINE_WITH_WECHAT);
        }
        order.setOrderSource("WECHAT");
        // TODO need to calculate total fee
        order.setTotalFee(0.0);
        order.setVersion(0);
        if (Objects.isNull(request.getInvoice())) {
            order.setInvoiceStatus(InvoiceStatus.NOT_INVOICED);
        } else {
            order.setInvoiceStatus(InvoiceStatus.INVOICING);
        }
        order.setStatus(OrderStatus.ORDER_UN_PAY);

        OrderExt orderExt = new OrderExt();
        orderExt.setContact(request.getContact());
        orderExt.setContactMobile(request.getContactMobile());
        orderExt.setServiceAddress(request.getServiceAddress());
        orderExt.setServiceDuration(request.getServiceDuration());
        orderExt.setServiceNumber(request.getServiceNumber());
        orderExt.setServiceStartTime(new Date(request.getServiceStartDate()));
        orderExt.setServiceEndDate(new Date(request.getServiceEndDate()));
        Map<String, Object> extension = request.getExtension();
        if (Objects.nonNull(extension)) {
            Map<String, Object> patientInfo = new HashMap<>();
            if (HOSPITAL_CARE == request.getOrderType() || HOME_CARE == request.getOrderType()) {
                if (extension.containsKey(DISEASES)) {
                    patientInfo.put(DISEASES, extension.get(DISEASES));
                }
                if (extension.containsKey(SELF_CARE)) {
                    patientInfo.put(SELF_CARE, extension.get(SELF_CARE));
                }
                if (extension.containsKey(EATING)) {
                    patientInfo.put(EATING, extension.get(EATING));
                }
                if (extension.containsKey(CATHETER_CARE)) {
                    patientInfo.put(CATHETER_CARE, extension.get(CATHETER_CARE));
                }
                if (extension.containsKey(ASSIST_WITH_MEDICATION)) {
                    patientInfo.put(ASSIST_WITH_MEDICATION, extension.get(ASSIST_WITH_MEDICATION));
                }
            } else if (REHABILITATION == request.getOrderType()) {
                if (extension.containsKey(REHABILITATION_TYPE)) {
                    patientInfo.put(REHABILITATION_TYPE, extension.get(REHABILITATION_TYPE));
                }
            }

            if (!patientInfo.isEmpty()) {
                orderExt.setPatientInfo(patientInfo);
            }
        }
        order = orderDao.saveUserOrder(order, orderExt);

        if (Objects.nonNull(invoice)) {
            invoice.setCreateTime(new Date());
            invoice.setDealStatus(CommonDealStatus.APPLY);
            invoice.setOrderIds(order.getId());
            invoice.setInvoiceFee(order.getTotalFee());
            invoiceDao.save(invoice);
        }

        return order;
    }

    @Override
    public List<Order> queryUserOrder(String userId, String status, String ownerType) {
        return orderDao.queryUserOrder(userId, status, ownerType);
    }

    @Override
    public Map<String, Object> queryOrderDetail(String orderId) {
        return orderDao.queryOrderDetail(orderId);
    }

    @Override
    public Map<String, Object> getOrderBasic(String orderId) {
        Map<String, Object> detailMap = new HashMap<>();
        Order order = orderRepository.findById(orderId).orElse(null);
        detailMap.put("order", order);

        OrderExt orderExt = orderExtRepository.findByOrderId(orderId);
        detailMap.put("orderExt", orderExt);

        return detailMap;
    }

    @Override
    public Map<String, Object> getOrderBasicWithUserInfo(String orderId) {
        Map<String, Object> detailMap = new HashMap<>();
        Order order = orderRepository.findById(orderId).orElse(null);
        detailMap.put("order", order);

        OrderExt orderExt = orderExtRepository.findByOrderId(orderId);
        detailMap.put("orderExt", orderExt);

        if (Objects.nonNull(order) && StringUtils.isNotBlank(order.getServiceStaffId())) {
            ServiceStaff staff = serviceStaffRepository.findById(order.getServiceStaffId()).orElse(null);
            detailMap.put("staff", staff);
        }

        return detailMap;
    }

    @Override
    public void orderEvaluate(Evaluate evaluate) {
        evaluate.setCreateTime(new Date());
        evaluateRepository.save(evaluate);
    }

    @Override
    public void saveInvoiceRequest(Invoice invoice) {
        String orderIds = invoice.getOrderIds();
        List<String> orderIdList = Arrays.asList(orderIds.split(","));
        List<Order> invoicedOrders = orderRepository.findInvoicedOrder(orderIdList, InvoiceStatus.NOT_INVOICED);
        if (CollectionUtils.isNotEmpty(invoicedOrders)) {
            logger.error("Those orders:{} has been invoiced!", StringUtils.join(orderIdList));
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), "部分订单已开票，请重新选择"));
        }

        invoice.setCreateTime(new Date());
        invoice.setDealStatus(CommonDealStatus.APPLY);
        invoice.setInvoiceFee(orderRepository.sumTotalFeeByIds(orderIdList));
        invoiceDao.save(invoice);

        for (String orderId : orderIdList) {
            orderRepository.updateInvoiceStatus(orderId, InvoiceStatus.INVOICING, invoice.getId());
        }
    }

    @Override
    public Page<Order> queryOrderForPage(Pageable pageable, OrderStatus status, String orderId, CareType careType, Date maxDate, Date minDate, String payStatus, String orderSource) {
        BooleanBuilder builder = new BooleanBuilder();
        QOrder qOrder = QOrder.order;

        if (Objects.nonNull(status)) {
            builder.and(qOrder.status.eq(status));
        }
        if (StringUtils.isNotBlank(orderId)) {
            builder.and(qOrder.id.eq(orderId));
        }
        if (Objects.nonNull(careType)) {
            builder.and(qOrder.careType.eq(careType));
        }
        if (Objects.nonNull(maxDate)) {
            builder.and(qOrder.orderTime.lt(maxDate));
        }
        if (Objects.nonNull(minDate)) {
            builder.and(qOrder.orderTime.gt(minDate));
        }
        if (StringUtils.equals("T", payStatus)) {
            builder.and(qOrder.payTime.isNotNull());
        } else if (StringUtils.equals("F", payStatus)) {
            builder.and(qOrder.payTime.isNull());
        }
        if (StringUtils.isNotBlank(orderSource)) {
            builder.and(qOrder.orderSource.eq(orderSource));
        }

        return orderRepository.findAll(builder, pageable);
    }

    @Override
    public Map<String, Object> getOrderDetail(String orderId) {
        Map<String, Object> detailMap = new HashMap<>();
        Order order = orderRepository.findById(orderId).orElse(null);
        detailMap.put("order", order);

        UserProfile userProfile = userProfileRepository.findById(order.getOrderUserId()).orElse(null);
        detailMap.put("user", userProfile);

        OrderExt orderExt = orderExtRepository.findByOrderId(orderId);
        detailMap.put("orderExt", orderExt);

        if (StringUtils.isNotBlank(order.getInvoiceId())) {
            Invoice invoice = invoiceRepository.findById(order.getInvoiceId()).orElse(null);
            if (Objects.nonNull(invoice)) {
                detailMap.put("invoice", invoice);
            }
        }

        return detailMap;
    }

    @Override
    public void assignOrderStaff(String orderId, String staffId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        ServiceStaff staff = serviceStaffRepository.findById(staffId).orElse(null);
        if (Objects.isNull(order) || Objects.isNull(staff)) {
            return;
        }
        order.setServiceStaffId(staffId);
        order.setStatus(OrderStatus.ORDER_ASSIGN);
        orderRepository.save(order);

        staff.setServiceStatus(ServiceStatus.ASSIGNED);
        serviceStaffRepository.save(staff);
    }

    @Override
    public void offlinePay(PayRecord payRecord) {
        Order order = orderRepository.findById(payRecord.getOrderId()).orElse(null);
        if (Objects.isNull(order)) {
            return;
        }
        order.setStatus(OrderStatus.ORDER_PAYED);
        order.setPayWay(payRecord.getPayWay());
        order.setPayTime(new Date());
        orderRepository.save(order);

        payRecord.setCreateTime(new Date());
        payRecord.setFinishTime(new Date());
        payRecord.setPayerUserId(order.getOrderUserId());
        payRecord.setPayFee(order.getTotalFee());
        payRecord.setPayResult(true);
        payRecord.setResultDesc("线下支付");
        payRecordRepository.save(payRecord);
    }

    @Override
    public void staffChangeRequest(OrderStaffChange staffChange) {
        Order order = orderRepository.findById(staffChange.getOrderId()).orElse(null);
        if (Objects.isNull(order) ||
                (order.getStatus() != OrderStatus.ORDER_PROCESSING && order.getStatus() != OrderStatus.ORDER_ASSIGN)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), ORDER_INFO_NOT_CORRECT));
        }

        staffChange.setCreateTime(new Date());
        if (Objects.isNull(staffChange.getDealStatus())) {
            staffChange.setDealStatus(CommonDealStatus.APPLY);
        } else if (staffChange.getDealStatus() == CommonDealStatus.AGREE) {
            // 后台操作，直接成功
            order.setServiceStaffId(staffChange.getNewStaffId());
            orderRepository.save(order);

            serviceStaffRepository.updateServiceStatus(staffChange.getOldStaffId(), ServiceStatus.FREE);
            serviceStaffRepository.updateServiceStatus(staffChange.getNewStaffId(), ServiceStatus.ASSIGNED);
        }
        orderStaffChangeRepository.save(staffChange);
    }

    @Override
    public void serviceStart(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (Objects.isNull(order) || order.getStatus() != OrderStatus.ORDER_ASSIGN) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), ORDER_INFO_NOT_CORRECT));
        }
        ServiceStaff serviceStaff = serviceStaffRepository.findById(order.getServiceStaffId()).orElse(null);
        if (Objects.isNull(serviceStaff)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), ORDER_INFO_NOT_CORRECT));
        }

        order.setStatus(OrderStatus.ORDER_PROCESSING);
        orderRepository.save(order);

        serviceStaff.setServiceStatus(ServiceStatus.IN_SERVICE);
        serviceStaffRepository.save(serviceStaff);
    }
}
