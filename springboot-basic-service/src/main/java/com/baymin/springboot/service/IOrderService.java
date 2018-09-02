package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.Evaluate;
import com.baymin.springboot.store.entity.Invoice;
import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.payload.UserOrderRequest;

import java.util.List;
import java.util.Map;

public interface IOrderService {

    Order saveUserOrder(UserOrderRequest request);

    List<Order> queryUserOrder(String userId, String status, String ownerType);

    Map<String, Object> queryOrderDetail(String orderId);

    void orderEvaluate(Evaluate evaluate);

    void saveInvoiceRequest(Invoice invoice);
}
