package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.enumconstant.CareType;
import com.baymin.springboot.store.enumconstant.OrderStatus;
import com.baymin.springboot.store.payload.UserOrderVo;
import com.baymin.springboot.store.payload.OrderDetailVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IOrderService {

    Order saveUserOrder(UserOrderVo request);

    List<Order> queryUserOrder(String userId, String status, String ownerType);

    OrderDetailVo queryOrderDetail(String orderId);

    Map<String, Object> getOrderBasic(String orderId);

    void orderEvaluate(Evaluate evaluate);

    void saveInvoiceRequest(Invoice invoice);

    Page<Order> queryOrderForPage(Pageable pageable, OrderStatus status, String orderId, CareType careType, Date maxDate, Date minDate, String payStatus, String orderSource);

    Map<String,Object> getOrderDetail(String orderId);

    void assignOrderStaff(String orderId, String staffId);

    void offlinePay(PayRecord payRecord);

    void staffChangeRequest(OrderStaffChange staffChange);

    void serviceStart(String orderId);

    Map<String, Object> getOrderBasicWithUserInfo(String orderId);

    Order queryOrderById(String orderId);

    List<OrderStaffChange> queryUserStaffChange(String userId);

    void orderCompleted(String orderId);
}
