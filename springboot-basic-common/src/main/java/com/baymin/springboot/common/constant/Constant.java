package com.baymin.springboot.common.constant;

/**
 * 系统常量
 * Created by Administrator on 2015/1/20 0020.
 */
public interface Constant {

    String IMG_HOST = "http://127.0.0.1:9090/admin";

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
        String accessKeyId = "LTAI9FVWK4Iqw3Wj";
        String accessKeySecret = "gio5DRQnEfZ6hR1tsJRiBXM1gF1sSV";
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
