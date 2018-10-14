package com.baymin.springboot.service.impl;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IOrderCarePlanService;
import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.entity.OrderCarePlan;
import com.baymin.springboot.store.enumconstant.OrderStatus;
import com.baymin.springboot.store.repository.IOrderCarePlanRepository;
import com.baymin.springboot.store.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

import static com.baymin.springboot.common.exception.ErrorDescription.ORDER_INFO_NOT_CORRECT;

@Service
@Transactional
public class OrderCarePlanServiceImpl implements IOrderCarePlanService {

    @Autowired
    private IOrderCarePlanRepository orderCarePlanRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public OrderCarePlan findByOrderId(String orderId) {
        return orderCarePlanRepository.findByOrderId(orderId);
    }

    @Override
    public void saveOrderCarePlan(OrderCarePlan orderCarePlan) {
        Order order = orderRepository.findById(orderCarePlan.getOrderId()).orElse(null);
        if (Objects.isNull(order) ||
                (order.getStatus() != OrderStatus.ORDER_PROCESSING && order.getStatus() != OrderStatus.ORDER_ASSIGN)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), ORDER_INFO_NOT_CORRECT));
        }

        orderCarePlan.setCreateTime(new Date());
        orderCarePlanRepository.save(orderCarePlan);

        order.setCarePlanExists(true);
        orderRepository.save(order);
    }
}
