package com.baymin.springboot.service.impl;

import com.baymin.springboot.common.constant.RequestConstant;
import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IOrderService;
import com.baymin.springboot.store.dao.IInvoiceDao;
import com.baymin.springboot.store.dao.IOrderDao;
import com.baymin.springboot.store.entity.Invoice;
import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.entity.OrderExt;
import com.baymin.springboot.store.enumconstant.InvoiceStatus;
import com.baymin.springboot.store.enumconstant.PayWay;
import com.baymin.springboot.store.payload.UserOrderRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.baymin.springboot.common.constant.RequestConstant.*;
import static com.baymin.springboot.common.exception.ErrorDescription.SERVER_ERROR;
import static com.baymin.springboot.store.enumconstant.OrderType.HOME_CARE;
import static com.baymin.springboot.store.enumconstant.OrderType.HOSPITAL_CARE;
import static com.baymin.springboot.store.enumconstant.OrderType.REHABILITATION;

@Service
public class OrderServiceImpl implements IOrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private IOrderDao orderDao;

    @Autowired
    private IInvoiceDao invoiceDao;

    @Override
    public Order saveUserOrder(UserOrderRequest request) {
        Invoice invoice = request.getInvoice();
        Order order = new Order();

        order.setOrderUserId(request.getOrderUserId());
        order.setOrderTime(new Date());
        order.setOrderType(request.getOrderType());
        if (StringUtils.equals(RequestConstant.OFFLINE, request.getPayway())) {
            order.setPayWay(PayWay.PAY_ONLINE_WITH_WECHAT);
        }
        // TODO need to calculate total fee
        order.setTotalFee(0.0);
        order.setVersion(0);
        if (Objects.isNull(request.getInvoice())) {
            order.setInvoiceStatus(InvoiceStatus.NOT_INVOICED);
        } else {
            order.setInvoiceStatus(InvoiceStatus.INVOICED);
        }

        OrderExt orderExt = new OrderExt();
        orderExt.setContact(request.getContact());
        orderExt.setContactMobile(request.getContactMobile());
        orderExt.setServiceAddress(request.getServiceAddress());
        orderExt.setServiceDuration(request.getServiceDuration());
        orderExt.setServiceNumber(request.getServiceNumber());
        orderExt.setServiceStartTime(new Date(request.getServiceStartTime()));
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
                JsonNode jsonNode;
                try {
                    jsonNode = JacksonUtil.toJsonNode(objectMapper.writeValueAsString(patientInfo));
                } catch (JsonProcessingException e) {
                    logger.error("Got error when write Object:{} as String", patientInfo, e);
                    throw new WebServerException(HttpStatus.INTERNAL_SERVER_ERROR, new ErrorInfo(ErrorCode.server_error.name(), SERVER_ERROR));
                }
                orderExt.setPatientInfo(jsonNode);
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
    public Map<String, Object> queryUserOrder(String userId, String status) {
        List<Order> orders = orderDao.queryUserOrder(userId, status);
        return null;
    }
}
