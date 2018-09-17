package com.baymin.springboot.pay.wechat;

/**
 * 微信配置
 * Created by baymin on 2016/10/19.
 */
public class WechatConfig {

    public static String AppID = "wxe98ff38ab973378e";
    public static String AppSecret = "4c0be9dcda96ad1079c48678ead089a0";
    public static String mchID = "1493923352";
    public static String key = "L8R3HmFAJLY4LJNiZb14Y8Gh0qMEhp5g";

    // 微信支付的域名配置
    public static String wechatPayHost = "http://www.dgyygg.cn/web/";
    // 机器IP
    public static String ip = "120.79.8.35";
    // 提供给微信回调URL
    public static String notifyUrl = "http://www.dgyygg.cn/web/wechatNotifyController/notifyPayResult";
    // 微信openID的code获取
    public static String WECHAT_OPENID_CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=";

}
