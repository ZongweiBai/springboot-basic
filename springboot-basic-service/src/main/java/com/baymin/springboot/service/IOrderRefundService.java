package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.OrderRefund;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;

public interface IOrderRefundService {
    OrderRefund saveOrderRefund(OrderRefund orderRefund);

    void updateOrderRefund(String refundId, CommonDealStatus dealStatus, String dealDesc);
}
