package com.baymin.springboot.store.dao;

import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.entity.OrderExt;

import java.util.List;

public interface IOrderDao {

    Order saveUserOrder(Order order, OrderExt orderExt);

    List<Order> queryUserOrder(String userId, String status);
}
