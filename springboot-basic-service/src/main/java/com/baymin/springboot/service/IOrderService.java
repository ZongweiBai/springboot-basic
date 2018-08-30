package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.payload.UserOrderRequest;

import java.util.Map;

public interface IOrderService {

    Order saveUserOrder(UserOrderRequest request);

    Map<String, Object> queryUserOrder(String userId, String status);
}
