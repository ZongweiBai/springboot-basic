package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.OrderRefund;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;

import java.util.List;

public interface IOrderRefundService {
    OrderRefund saveOrderRefund(OrderRefund orderRefund);

    void updateOrderRefund(String refundId, CommonDealStatus dealStatus, String dealDesc);

    List<OrderRefund> queryUserOrderRefund(String userId);
}
