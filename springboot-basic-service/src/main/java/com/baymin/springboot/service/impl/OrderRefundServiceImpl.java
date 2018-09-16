package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.IOrderRefundService;
import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.entity.OrderRefund;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import com.baymin.springboot.store.repository.IOrderRefundRepository;
import com.baymin.springboot.store.repository.IOrderRepository;
import org.apache.commons.lang3.StringUtils;
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
        if (StringUtils.isNotBlank(orderRefund.getId())) {
            OrderRefund oldData = orderRefundRepository.findById(orderRefund.getId()).orElse(null);
            oldData.setDealDesc(orderRefund.getDealDesc());
            oldData.setDealStatus(orderRefund.getDealStatus());
            if (orderRefund.getDealStatus() == CommonDealStatus.AGREE) {
                oldData.setRefundDuration(orderRefund.getRefundDuration());
                oldData.setRefundFee(orderRefund.getRefundFee());
            }
            orderRefundRepository.save(oldData);
        } else {
            Order order = orderRepository.findById(orderRefund.getOrderId()).orElse(null);
            orderRefund.setCareType(order.getCareType());
            orderRefund.setRefundTime(new Date());
            orderRefund.setDealTime(new Date());
            orderRefundRepository.save(orderRefund);
        }
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
