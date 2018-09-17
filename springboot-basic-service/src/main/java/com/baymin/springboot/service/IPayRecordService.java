package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.entity.PayRecord;
import com.baymin.springboot.store.entity.UserProfile;

import java.util.Map;

public interface IPayRecordService {

    /**
     * 微信商品支付
     */
    Map<String, Object> payOrderWithWeChat(UserProfile user, Order order, String appID, String mchID, String key);

    /**
     * 支付成功回调
     */
    void orderPaySuccess(PayRecord payRecord) throws Exception;

    /**
     * 根据tradeNo查询支付记录
     */
    PayRecord getRecordByTradeNo(String trade_no);

}
