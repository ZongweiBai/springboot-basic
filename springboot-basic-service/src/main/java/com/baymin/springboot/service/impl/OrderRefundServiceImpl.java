package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IOrderRefundService;
import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.entity.OrderRefund;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import com.baymin.springboot.store.repository.IOrderRefundRepository;
import com.baymin.springboot.store.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

@Service
@Transactional
public class OrderRefundServiceImpl implements IOrderRefundService {

    @Autowired
    private IOrderRefundRepository orderRefundRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public void saveOrderRefund(OrderRefund orderRefund) {
        Order order = orderRepository.findById(orderRefund.getOrderId()).orElse(null);
        orderRefund.setCareType(order.getCareType());
        orderRefund.setDealTime(new Date());
        orderRefundRepository.save(orderRefund);
    }

    @Override
    public void updateOrderRefund(String refundId, CommonDealStatus dealStatus, String dealDesc) {
        OrderRefund refund = orderRefundRepository.findById(refundId).orElse(null);
        refund.setDealStatus(dealStatus);
        if (Objects.nonNull(dealDesc)) {
            refund.setDealDesc(dealDesc);
        }
        refund.setDealTime(new Date());
        orderRefundRepository.save(refund);
    }
}
