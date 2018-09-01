package com.baymin.springboot.store.dao;

import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.entity.OrderExt;

import java.util.List;
import java.util.Map;

public interface IOrderDao {

    Order saveUserOrder(Order order, OrderExt orderExt);

    List<Order> queryUserOrder(String userId, String status, String ownerType);

    Map<String, Object> queryOrderDetail(String orderId);
}
