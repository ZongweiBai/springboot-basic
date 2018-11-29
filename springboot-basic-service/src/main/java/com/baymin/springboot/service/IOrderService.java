package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.enumconstant.CareType;
import com.baymin.springboot.store.enumconstant.OrderStatus;
import com.baymin.springboot.store.payload.OrderDetailVo;
import com.baymin.springboot.store.payload.UserOrderVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IOrderService {

    Order saveUserOrder(UserOrderVo request);

    List<Order> queryUserOrder(String userId, String status, String ownerType);

    OrderDetailVo queryOrderDetail(String orderId);

    Map<String, Object> getOrderBasic(String orderId, String type);

    void orderEvaluate(Evaluate evaluate);

    void saveInvoiceRequest(Invoice invoice);

    Page<Order> queryOrderForPage(Pageable pageable, OrderStatus status, String orderId, CareType careType, Date maxDate, Date minDate, String payStatus, String orderSource, String account, String address);

    Map<String, Object> getOrderDetail(String orderId);

    void assignOrderStaff(String orderId, String staffId, String adminId, String nurseId);

    void offlinePay(PayRecord payRecord, Admin sysUser);

    void staffChangeRequest(OrderStaffChange staffChange);

    void serviceStart(String orderId);

    Map<String, Object> getOrderBasicWithUserInfo(String orderId);

    Order queryOrderById(String orderId);

    List<OrderStaffChange> queryUserStaffChange(String userId);

    void orderCompleted(String orderId);

    List<Order> queryOrderForList(CareType careType, Date maxDate, Date minDate, OrderStatus status);
}
