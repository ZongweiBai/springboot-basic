package com.baymin.springboot.pay.wechat.param.paynotify;

import lombok.Data;

@Data
public class PayNotifyReqData {

    // 通知参数
    private String return_code; // 返回状态码，此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断；SUCCESS/FAIL
    private String return_msg; // 返回信息，如非空，为错误原因

    // 以下字段在return_code为SUCCESS的时候有返回
    private String appid;
    private String mch_id;
    private String device_info;
    private String nonce_str; // 随机字符串
    private String sign;
    private String result_code; // 业务结果
    private String err_code; // 错误代码
    private String err_code_des; // 错误代码描述
    private String openid; // 用户标识，用户在商户appid下的唯一标识
    private String is_subscribe; // 是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
    private String trade_type; // 交易类型，JSAPI、NATIVE、APP
    private String bank_type; // 银行类型，采用字符串类型的银行标识，银行类型见银行列表
    private int total_fee; // 订单总金额，单位为分
    private String fee_type; // 货币种类
    private int cash_fee; // 现金支付金额
    private String cash_fee_type; // 现金支付货币类型
    private int coupon_fee; // 代金券或立减优惠金额<=订单总金额，订单总金额-代金券或立减优惠金额=现金支付金额
    private int coupon_count; // 代金券或立减优惠使用数量
    private String transaction_id; // 微信支付订单号
    private String out_trade_no; // 商户订单号
    private String time_end; // 支付完成时间

}