package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IOrderCarePlanService;
import com.baymin.springboot.store.entity.OrderCarePlan;
import com.baymin.springboot.store.repository.IOrderCarePlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class OrderCarePlanServiceImpl implements IOrderCarePlanService {

    @Autowired
    private IOrderCarePlanRepository orderCarePlanRepository;

    @Override
    public OrderCarePlan findByOrderId(String orderId) {
        return orderCarePlanRepository.findByOrderId(orderId);
    }

    @Override
    public void saveOrderCarePlan(OrderCarePlan orderCarePlan) {
        orderCarePlan.setCreateTime(new Date());
        orderCarePlanRepository.save(orderCarePlan);
    }
}
