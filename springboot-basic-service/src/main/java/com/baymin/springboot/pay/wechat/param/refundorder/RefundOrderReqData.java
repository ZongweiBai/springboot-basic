package com.baymin.springboot.pay.wechat.param.refundorder;

import com.baymin.springboot.pay.wechat.WechatConfig;
import com.baymin.springboot.pay.wechat.param.RandomStringGenerator;
import com.baymin.springboot.pay.wechat.param.Signature;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Data
public class RefundOrderReqData {

    private String appid; // 必填	公众账号ID
    private String mch_id; // 必填	商户号，微信支付分配的商户号
    private String nonce_str; // 必填	随机字符串，不长于32位
    private String sign; // 必填	签名
    private String sign_type; // 签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
    private String transaction_id; // 微信生成的订单号，在支付通知中有返回
    private String out_trade_no; // 必填	商户系统内部的订单号
    private String out_refund_no; // 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
    private int total_fee; // 必填	订单总金额，只能为整数
    private int refund_fee; // 退款总金额，订单总金额，单位为分，只能为整数
    private String refund_fee_type = "CNY";//退款货币类型，需与支付一致，或者不填。符合ISO 4217标准的三位字母代码，默认人民币：CNY
    private String refund_desc; // 非必填 退款原因	若商户传入，会在下发给用户的退款消息中体现退款原因
    private String refund_account; // 非必填	退款资金来源
    private String notify_url; // 非必填	异步接收微信支付退款结果通知的回调地址，通知URL必须为外网可访问的url，不允许带参数 如果参数中传了notify_url，则商户平台上配置的回调地址将不会生效。

    public RefundOrderReqData() {
        super();
    }

    public RefundOrderReqData(String transactionId, String out_refund_no,
                              String body, int totalFree, int refund_fee, String appID, String mchID,
                              String key, String channelCode, String refund_desc) {
        setAppid(appID);
        System.out.println("appID:" + appID);
        setMch_id(mchID);
        System.out.println("mchID:" + mchID);
        setTransaction_id("transaction_id:" + transactionId);
        System.out.println("transaction_id:" + getTransaction_id());
        setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
        System.out.println("nonce_str:" + getNonce_str());
        System.out.println("body:" + body);
        setOut_trade_no(RandomStringGenerator.getTradeNo(channelCode));
        System.out.println("out_trade_no:" + getOut_trade_no());
        setTotal_fee(totalFree);
        System.out.println("totalFree:" + totalFree);
        setRefund_fee(refund_fee);
        System.out.println("refund_fee:" + refund_fee);
        setNotify_url(WechatConfig.notifyUrl);
        System.out.println("notify_url:" + getNotify_url());
        setOut_refund_no(out_refund_no);
        System.out.println("out_refund_no:" + getOut_refund_no());
        setRefund_desc(refund_desc);
        System.out.println("refund_desc:" + getRefund_desc());
        String sign = Signature.getSign(toMap(), key);
        setSign(sign);
        System.out.println("sign:" + sign);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj;
            try {
                obj = field.get(this);
                if (obj != null) {
                    map.put(field.getName(), obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return map;
    }

}
