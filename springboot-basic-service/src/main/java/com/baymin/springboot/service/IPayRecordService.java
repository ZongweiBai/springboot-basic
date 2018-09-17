package com.baymin.springboot.service;

import com.guotion.arrive.common.enums.PayFor;
import com.guotion.arrive.entity.order.Order;
import com.guotion.arrive.entity.pay.PayRecord;
import com.guotion.arrive.entity.user.User;

import java.util.List;
import java.util.Map;

public interface IPayRecordService {

    /**
     * 微信商品支付
     */
    Map<String, Object> payOrderWithWeChat(User user, List<Order> orderList, String appID, String mchID, String key, String orderId, String tradeId, String openId, Double totalProductPrice);

    /**
     * 支付成功回调
     */
    void orderPaySuccess(PayRecord payRecord) throws Exception;

    /**
     * 根据tradeNo查询支付记录
     */
    PayRecord getRecordByTradeNo(String trade_no);

    void updateOrderPaySuccess(List<Order> orderList) throws Exception;

    /**
     * 余额支付订单
     */
    Map<String, Object> payOrderWithBalance(User user, List<Order> orderList, String orderId, String tradId) throws Exception;

    PayRecord getSuccessRecordByOrderId(String orderId, PayFor payFor);

    List<PayRecord> queryByAccountId(String accountId);

    List<PayRecord> queryByOrderOrTradeId(String tradeId, String orderId);

    void updateRecord(PayRecord payRecord);
}
