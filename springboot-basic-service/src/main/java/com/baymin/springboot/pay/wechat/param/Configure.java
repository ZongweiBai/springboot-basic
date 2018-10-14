package com.baymin.springboot.pay.wechat.param;

public class Configure {

    /**
     * 是否调试模式
     */
    public static Boolean DEBUGMODE = true;

    /**
     * 统一下单
     * 应用场景：除被扫支付场景以外，商户系统先调用该接口在微信支付服务后台生成预支付交易单，返回正确的预支付交易回话标识后再按扫码、JSAPI、APP等不同场景生成交易串调起支付
     */
    public static String UNIFIEDORDER_API = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 退款申请接口
     */
    public static String ORDER_REFUND_API = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    /**
     * 查询订单
     * 该接口提供所有微信支付订单的查询，商户可以通过该接口主动查询订单状态，完成下一步的业务逻辑。
     * 需要调用查询接口的情况：
     * ◆ 当商户后台、网络、服务器等出现异常，商户系统最终未接收到支付通知；
     * ◆ 调用支付接口后，返回系统错误或未知交易状态情况；
     * ◆ 调用被扫支付API，返回USERPAYING的状态；
     * ◆ 调用关单或撤销接口API之前，需确认支付状态；
     */
    public static String ORDER_QUERY_API = "https://api.mch.weixin.qq.com/pay/orderquery";

    public static String HttpsRequestClassName = "com.baymin.springboot.pay.wechat.param.HttpsRequest";

    public static String getHttpsRequestClassName() {
        return HttpsRequestClassName;
    }

}