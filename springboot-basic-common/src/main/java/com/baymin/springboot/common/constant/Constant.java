package com.baymin.springboot.common.constant;

/**
 * 系统常量
 * Created by Administrator on 2015/1/20 0020.
 */
public interface Constant {

//    String IMG_HOST = "http://127.0.0.1:9090/admin";
    String IMG_HOST = "http://120.78.69.141/admin";

    interface AliyunAPI {
        //云通信短信API产品
        String dycdpProduct = "Dycdpapi";
        //云通信短信API产品域名
        String dycdpDomain = "dycdpapi.aliyuncs.com";

        //云通信短信API产品
        String dysmsProduct = "Dysmsapi";
        //云通信短信API产品域名
        String dysmsDomain = "dysmsapi.aliyuncs.com";

        // 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
        String accessKeyId = "LTAICpePRa6IP0Rh";
        String accessKeySecret = "dHfQs1tPdF7D4URw9RlJF0XA40INXV";
        String signName = "一家依护";

        String ORDER_ASSING = "SMS_146802582"; //订单指派提醒
        String ORDER_CHANGE_REJECT = "SMS_146807518"; //客户换人申请不通过通知
        String ORDER_CHANGE_AGREE = "SMS_146802565";  //客户换人申请通过通知
        String ORDER_REFUND_REJECT = "SMS_146802556"; //退款申请不通过通知
        String ORDER_REFUND_AGREE = "SMS_146807496";  //客户退款申请通过通知
        String ORDER_CHECK_REJECT = "SMS_146802529";  //订单审核不通过
        String ORDER_CHECK_AGREE = "SMS_146807451";   //订单审核通过通知
        String ORDER_USER_REG = "SMS_146802494";  //用户注册验证
    }

    interface WechatTemplate {
        String T_REQUEST_DENY = "9yiC4eqAVxSyIU6x91h_Ost-QAKil2JN7ZOekQm08SI"; // 换人或者退款申请不通过消息模板
        String T_REQUEST_AGREE = "YoLZUeVY-096cPDvenNaXuf6i11HHJSCAFx9Hqv2NaY"; // 换人或者退款申请通过消息模板
        String T_ORDER_ASSIGNED = "Qxirwkn6aueTH-Ux6g_c4LSeZQxr7x3Zs1yinuNjoq0"; // 订单完成指派通知用户
        String T_ORDER_COMPLETED = "f_-2w5cfGdOmcBiZKmdxDvpPo8y7iMoSiVnETZZ81DQ";   // 订单完成评价提醒通知用户
        String T_MSG_REDIRECT_URL = "http://dms.ecare-easy.com/";
    }

    interface JWTAPI {
        String JWT_ID = "I_AM_ROBOT";
        String JWT_SECRET = "7786df7fc3a34e26a61c034d5ec8245d";
        String JWT_TOKEN = "access_token";
        String JWT_REFRESH_TOKEN = "refresh_token";
        int JWT_TTL = 60 * 60 * 1000;  //millisecond
        long JWT_REFRESH_TTL = 7 * 24 * 60 * 60 * 1000; //millisecond
    }

}
