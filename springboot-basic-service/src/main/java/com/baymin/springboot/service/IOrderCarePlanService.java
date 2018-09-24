package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.OrderCarePlan;

public interface IOrderCarePlanService {
    OrderCarePlan findByOrderId(String orderId);

    void saveOrderCarePlan(OrderCarePlan orderCarePlan);
}
