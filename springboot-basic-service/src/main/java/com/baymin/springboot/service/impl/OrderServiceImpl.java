package com.baymin.springboot.service.impl;

import com.baymin.springboot.common.constant.RequestConstant;
import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.common.util.BigDecimalUtil;
import com.baymin.springboot.service.IOrderService;
import com.baymin.springboot.store.dao.IInvoiceDao;
import com.baymin.springboot.store.dao.IOrderDao;
import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.enumconstant.*;
import com.baymin.springboot.store.payload.UserOrderVo;
import com.baymin.springboot.store.payload.OrderDetailVo;
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
import java.util.stream.Collectors;

import static com.baymin.springboot.common.exception.ErrorDescription.ORDER_INFO_NOT_CORRECT;
import static com.baymin.springboot.store.enumconstant.CareType.HOME_CARE;
import static com.baymin.springboot.store.enumconstant.CareType.HOSPITAL_CARE;

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

    @Autowired
    private IServiceProductRepository serviceProductRepository;

    @Override
    public Order saveUserOrder(UserOrderVo request) {
        Invoice invoice = request.getInvoice();
        Order order = new Order();

        order.setOrderUserId(request.getOrderUserId());
        order.setOrderTime(new Date());
        order.setCareType(request.getOrderType());
        if (StringUtils.equals(RequestConstant.OFFLINE, request.getPayway())) {
            order.setPayWay(PayWay.PAY_ONLINE_WITH_WECHAT);
        }
        order.setOrderSource("WECHAT");
        order.setServiceProductId(request.getProductId());

        double totalFee = calculateTotalFee(request);
        order.setTotalFee(totalFee);
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

        if (HOSPITAL_CARE == request.getOrderType() || HOME_CARE == request.getOrderType()) {
            Map<String, Object> patientInfo = new HashMap<>();
            if (CollectionUtils.isNotEmpty(request.getQuestions())) {
                Map<String, List<Question>> questionMap = request.getQuestions().stream().collect(Collectors.groupingBy(Question::getQuestionType));
                if (!questionMap.isEmpty()) {
                    questionMap.forEach((key, value) -> patientInfo.put(key, value));
                    orderExt.setPatientInfo(patientInfo);
                }
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

    private Double calculateTotalFee(UserOrderVo request) {
        double totalFee = 0.0;
        ServiceProduct product = serviceProductRepository.findById(request.getProductId()).orElse(null);
        if (Objects.nonNull(product)) {
            totalFee = BigDecimalUtil.mul(product.getProductPrice(), request.getServiceDuration());
        }
        if (HOSPITAL_CARE == request.getOrderType() || HOME_CARE == request.getOrderType()) {
            if (CollectionUtils.isNotEmpty(request.getBasicItems())) {
                List<String> productItems = Arrays.asList(product.getBasicItems().split(","));
                List<BasicItem> billingItems = new ArrayList<>();
                for (BasicItem basicItem : request.getBasicItems()) {
                    if (!productItems.contains(basicItem.getId())) {
                        billingItems.add(basicItem);
                    }
                }

                if (CollectionUtils.isNotEmpty(billingItems)) {
                    double itemTotalFee = billingItems.stream().map(BasicItem::getItemFee).reduce(0.0, BigDecimalUtil::add);
                    totalFee = BigDecimalUtil.add(totalFee, itemTotalFee);
                }
            }
        }
        return totalFee;
    }

    @Override
    public List<Order> queryUserOrder(String userId, String status, String ownerType) {
        return orderDao.queryUserOrder(userId, status, ownerType);
    }

    @Override
    public OrderDetailVo queryOrderDetail(String orderId) {
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
    public Order queryOrderById(String orderId) {
        return orderRepository.findById(orderId).orElse(null);
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
