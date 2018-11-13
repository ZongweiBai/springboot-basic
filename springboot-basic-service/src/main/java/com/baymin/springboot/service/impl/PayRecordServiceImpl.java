package com.baymin.springboot.service.impl;

import com.baymin.springboot.common.constant.WebConstant;
import com.baymin.springboot.pay.wechat.param.unifiedorder.UnifiedOrderReqData;
import com.baymin.springboot.pay.wechat.param.unifiedorder.UnifiedOrderResData;
import com.baymin.springboot.pay.wechat.service.UnifiedOrderService;
import com.baymin.springboot.service.IPayRecordService;
import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.entity.PayRecord;
import com.baymin.springboot.store.entity.UserProfile;
import com.baymin.springboot.store.enumconstant.OrderStatus;
import com.baymin.springboot.store.enumconstant.PayWay;
import com.baymin.springboot.store.repository.IOrderRepository;
import com.baymin.springboot.store.repository.IPayRecordRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 支付记录service
 * Created by baymin on 2016/9/12.
 */
@Service
@Transactional
public class PayRecordServiceImpl implements IPayRecordService {

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IPayRecordRepository payRecordRepository;

    @Override
    public Map<String, Object> payOrderWithWeChat(String payType, final UserProfile user, Order order, String appID, String mchID, String key) {
        final Map<String, Object> reMap = new HashMap<>();

        final double totalFee = order.getTotalFee();

        String channelCode = "FMY_CARE";
        String idpId = null;
        if (StringUtils.isBlank(payType)) {
            payType = "JSAPI";
        }
        if (StringUtils.equals("JSAPI", payType)) {
            idpId = user.getIdpId();
        }

        try {
            UnifiedOrderReqData reqData = new UnifiedOrderReqData(null, payType, order.getId(), (int) (totalFee * 100), appID, mchID, key, channelCode, idpId, order.getId());
            new UnifiedOrderService().doService(reqData, new UnifiedOrderService.ResultListener() {
                @Override
                public void onFailByReturnCodeError(UnifiedOrderResData resData) {
                    reMap.put(WebConstant.RESULT, WebConstant.FAULT);
                    reMap.put(WebConstant.MESSAGE, "统一下单API请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问");
                }

                @Override
                public void onFailByReturnCodeFail(UnifiedOrderResData resData) {
                    reMap.put(WebConstant.RESULT, WebConstant.FAULT);
                    reMap.put(WebConstant.MESSAGE, resData.getReturn_msg() + "!!");
                }

                @Override
                public void onUnifiedOrderFail(UnifiedOrderResData resData) {
                    reMap.put(WebConstant.RESULT, WebConstant.FAULT);
                    reMap.put(WebConstant.MESSAGE, resData.getErr_code_des() + "!!!");
                }

                @Override
                public void onUnifiedOrderSuccess(UnifiedOrderResData resData) {
                    // 在系统生成预支付订单
                    PayRecord payRecord = new PayRecord();
                    if (Objects.nonNull(user)) {
                        payRecord.setPayerUserId(user.getId());
                    } else {
                        payRecord.setPayerUserId(order.getOrderUserId());
                    }
                    payRecord.setCreateTime(new Date());
                    payRecord.setPayFee(totalFee);
                    payRecord.setPayResult(false);
                    payRecord.setPayWay(PayWay.PAY_ONLINE_WITH_WECHAT);
                    payRecord.setResultDesc("生成预支付订单");
                    payRecord.setOrderId(order.getId());
                    payRecord.setTradeNo(reqData.getOut_trade_no());
                    payRecordRepository.save(payRecord);

                    resData.setOut_trade_no(reqData.getOut_trade_no());

                    reMap.put(WebConstant.RESULT, WebConstant.SUCCESS);
                    reMap.put(WebConstant.INFO, resData);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            reMap.put(WebConstant.RESULT, WebConstant.FAULT);
            reMap.put(WebConstant.MESSAGE, "生成预付单失败");
        }

        return reMap;
    }

    @Override
    public void orderPaySuccess(PayRecord payRecord) {
        Order order = orderRepository.findById(payRecord.getOrderId()).orElse(null);
        // 更新订单成功
        if (Objects.nonNull(order)) {
            order.setPayTime(new Date());
            if (order.getStatus() == OrderStatus.ORDER_UN_PAY) {
                order.setStatus(OrderStatus.ORDER_PAYED);
            }
            orderRepository.save(order);
        }

        // 修改支付记录为成功
        payRecord.setResultDesc("支付成功");
        payRecord.setFinishTime(new Date());
        payRecord.setPayResult(true);
        payRecordRepository.save(payRecord);
    }

    @Override
    public PayRecord getRecordByTradeNo(String tradeNo) {
        return payRecordRepository.findByTradeNo(tradeNo);
    }

}
