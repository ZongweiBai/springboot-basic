package com.baymin.springboot.common.exception;

/**
 * Created by ebaizon on 4/20/2017.
 */
public class ErrorDescription {

    public static final String INVALID_REQUEST = "请求参数非法";
    public static final String INVALID_SMS_CODE = "用户名或验证码错误";
    public static final String INVALID_PASSWORD = "用户名或密码错误";
    public static final String SERVER_ERROR = "服务器异常";
    public static final String TOKEN_INVALID = "Token非法";
    public static final String TOKEN_EXPIRED = "Token已过期";
    public static final String ORDER_INFO_NOT_CORRECT = "订单信息不正确";
    public static final String USER_NOT_EXIST = "用户不存在";
    public static final String USER_EXIST = "用户已存在";
    public static final String NOT_MEET_CONDITION = "不满足评价条件";
    public static final String ORDER_PAYED = "订单已支付，不能重复支付";
    public static final String RECORD_NOT_EXIST = "记录不存在";

}
