package com.baymin.springboot.service.impl;

import com.baymin.springboot.common.constant.RequestConstant;
import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IOrderService;
import com.baymin.springboot.store.dao.IInvoiceDao;
import com.baymin.springboot.store.dao.IOrderDao;
import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.enumconstant.CareType;
import com.baymin.springboot.store.enumconstant.InvoiceStatus;
import com.baymin.springboot.store.enumconstant.OrderStatus;
import com.baymin.springboot.store.enumconstant.PayWay;
import com.baymin.springboot.store.payload.UserOrderRequest;
import com.baymin.springboot.store.repository.IEvaluateRepository;
import com.baymin.springboot.store.repository.IInvoiceRepository;
import com.baymin.springboot.store.repository.IOrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.baymin.springboot.common.constant.RequestConstant.*;
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
        orderExt.setServiceStartTime(request.getServiceStartDate());
        orderExt.setServiceEndDate(request.getServiceEndDate());
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
            invoice.setDealStatus(false);
            invoice.setOrderIds(order.getId());
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
        invoice.setDealStatus(false);
        invoiceDao.save(invoice);

        orderRepository.updateInvoiceStatus(orderIdList, InvoiceStatus.INVOICING);
    }

    @Override
    public Page<Order> queryOrderForPage(Pageable pageable, OrderStatus status, String orderId, CareType careType, Date maxDate, Date minDate) {
        QOrder qOrder = QOrder.order;

        BooleanExpression predicate = qOrder.id.isNotNull();
        if (Objects.nonNull(status)) {
            predicate.and(qOrder.status.eq(status));
        }
        if (StringUtils.isNotBlank(orderId)) {
            predicate.and(qOrder.id.eq(orderId));
        }
        if (Objects.nonNull(careType)) {
            predicate.and(qOrder.careType.eq(careType));
        }
        if (Objects.nonNull(maxDate)) {
            predicate.and(qOrder.orderTime.lt(maxDate));
        }
        if (Objects.nonNull(minDate)) {
            predicate.and(qOrder.orderTime.gt(minDate));
        }

        return orderRepository.findAll(predicate, pageable);
    }

    @Override
    public Map<String, Object> getOrderDetail(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        List<Invoice> invoiceList = invoiceRepository.findByOrderIds(orderId);

        Map<String, Object> detailMap = new HashMap<>();
        detailMap.put("order", order);
        detailMap.put("invoiceList", invoiceList);
        return detailMap;
    }
}
