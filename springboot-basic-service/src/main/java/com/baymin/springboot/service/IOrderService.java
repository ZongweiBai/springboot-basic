package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.Evaluate;
import com.baymin.springboot.store.entity.Invoice;
import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.entity.OrderStaffChange;
import com.baymin.springboot.store.enumconstant.CareType;
import com.baymin.springboot.store.enumconstant.OrderStatus;
import com.baymin.springboot.store.enumconstant.PayWay;
import com.baymin.springboot.store.payload.UserOrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IOrderService {

    Order saveUserOrder(UserOrderRequest request);

    List<Order> queryUserOrder(String userId, String status, String ownerType);

    Map<String, Object> queryOrderDetail(String orderId);

    void orderEvaluate(Evaluate evaluate);

    void saveInvoiceRequest(Invoice invoice);

    Page<Order> queryOrderForPage(Pageable pageable, OrderStatus status, String orderId, CareType careType, Date maxDate, Date minDate, String payStatus, String orderSource);

    Map<String,Object> getOrderDetail(String orderId);

    void assignOrderStaff(String orderId, String staffId);

    void offlinePay(String orderId, PayWay payWay);

    void staffChangeRequest(OrderStaffChange staffChange);
}
