package com.baymin.springboot.pay.wechat;

/**
 * 微信配置
 * Created by baymin on 2016/10/19.
 */
public class WechatConfig {

//    public static String AppID = "wx2e480aa3f1f76b4a";
//    public static String AppSecret = "d5c4e3ea0e63f44441d4723c15a83e15";
//    public static String mchID = "1244739002";
//    public static String key = "L8R3HmFAJLY4LJNiZb14Y8Gh0qMEhp5g";

    public static String AppID = "wx86b3751ad43f4062";
    public static String AppSecret = "f52a587fefed285df9244f310eee8a34";
    public static String mchID = "1244739002";
    public static String key = "e10adc3949ba59abbe56e057f20f883e";

    public static String apiValidationToken = "18040225420166561792";

    // 微信openID的code获取
    public static String WECHAT_OPENID_CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=";
    // 微信网页授权access_token获取
    public static String WECHAT_WEB_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    // 微信网页授权获取用户基本信息
    public static String WECHAT_WEB_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";

    // 微信基础access_token获取
    public static String WECHAT_BASIC_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    // 微信jsapi_ticket获取
    public static String WECHAT_JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
    // 微信发送模板消息
    public static String WECHAT_SEND_MODEL_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";

    // 微信支付的域名配置
    public static String wechatPayHost = "http://www.dgyygg.cn/web/";
    // 机器IP
    public static String ip = "120.79.8.35";
    // 提供给微信回调URL
    public static String notifyUrl = "http://dms.ecare-easy.com/family-care/api/wechat/pay/notify";

    /**
     * 模板消息ID
     */
    public static String TEMPLATE_MSG_ORDER_FINISHED = "Dyvp3-Ff0cnail_CDSzk1fIc6-9lOkxsQE7exTJbwUE";

}
