package com.baymin.springboot.store.dao;

import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.entity.OrderExt;
import com.baymin.springboot.store.payload.OrderDetailVo;

import java.util.Date;
import java.util.List;

public interface IOrderDao {

    Order saveUserOrder(Order order, OrderExt orderExt);

    List<Order> queryUserOrder(String userId, String status, String ownerType, Date minDate, Date maxDate);

    OrderDetailVo queryOrderDetail(String orderId);
}
