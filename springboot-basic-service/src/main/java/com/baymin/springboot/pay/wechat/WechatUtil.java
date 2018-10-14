package com.baymin.springboot.pay.wechat;

import com.baymin.springboot.common.util.HttpClientUtil;
import com.baymin.springboot.pay.wechat.param.RandomStringGenerator;
import com.baymin.springboot.pay.wechat.param.Signature;
import com.baymin.springboot.pay.wechat.param.pojo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import static com.baymin.springboot.pay.wechat.WechatConfig.*;

@Slf4j
public class WechatUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static BasicTokenResponse getBasicToken() {
        String addr = String.format(WECHAT_BASIC_ACCESS_TOKEN_URL, AppID, AppSecret);
        String responseStr = HttpClientUtil.sendGet(addr, null);
        log.debug("获取微信basic accessToken的response是：{}", responseStr);
        if (StringUtils.isBlank(responseStr)) {
            return null;
        }
        try {
            return objectMapper.readValue(responseStr, BasicTokenResponse.class);
        } catch (IOException e) {
            log.error("获取微信basic accessToken失败", e);
            return null;
        }
    }

    public static JsapiTicketResponse getJsapiTicket(String accessToken) {
        String addr = String.format(WECHAT_JSAPI_TICKET_URL, accessToken);
        String responseStr = HttpClientUtil.sendGet(addr, null);
        log.debug("获取微信jspai_ticket的response是：{}", responseStr);
        if (StringUtils.isBlank(responseStr)) {
            return null;
        }
        try {
            return objectMapper.readValue(responseStr, JsapiTicketResponse.class);
        } catch (IOException e) {
            log.error("获取微信jspai_ticket失败", e);
            return null;
        }
    }

    /**
     * "content": "{ {result.DATA} }\n\n领奖金额:{ {withdrawMoney.DATA} }\n领奖  时间:{ {withdrawTime.DATA} }\n银行信息:{ {cardInfo.DATA} }\n到账时间:  { {arrivedTime.DATA} }\n{ {remark.DATA} }",
     * "example": "您已提交领奖申请\n\n领奖金额：xxxx元\n领奖时间：2013-10-10 12:22:22\n银行信息：xx银行(尾号xxxx)\n到账时间：预计xxxxxxx\n\n预计将于xxxx到达您的银行卡"
     *
     * @param accessToken
     * @param openId
     * @param templateId
     * @param redirectUrl
     * @param extension
     * @return
     */
    public static ModelMsgResponse sendModelMsg(String accessToken, String openId, String templateId,
                                                String redirectUrl, Map<String, String> extension) {
        String addr = String.format(WECHAT_SEND_MODEL_MSG_URL, accessToken);
        ModelMsgRequest request = new ModelMsgRequest();
        request.setTouser(openId);
        request.setTemplateId(templateId);
        request.setUrl(redirectUrl);
        Map<String, Map<String, String>> contentMap = new HashMap<>();


        Map<String, String> firstMap = new HashMap<>();
//        firstMap.put("value", "您好，本次服务已结束，谢谢您的支持，请对我们本次服务给予评价");
//        firstMap.put("color", "#173177");
//        contentMap.put("first", firstMap);

        extension.forEach((key, value) -> {
            Map<String, String> extensionMap = new HashMap<>();
            extensionMap.put("value", value);
            extensionMap.put("color", "#173177");
            contentMap.put(key, extensionMap);
        });

//        Map<String, String> remarkMap = new HashMap<>();
//        firstMap.put("value", "您好，本次服务已结束，谢谢您的支持，请对我们本次服务给予评价");
//        firstMap.put("color", "#173177");

        request.setData(contentMap);

        String responseStr;
        try {
            String requestBody = objectMapper.writeValueAsString(request);
            log.debug("发送微信模板消息的request是：{}", requestBody);

            responseStr = HttpClientUtil.sendPost(addr, requestBody);
            log.debug("发送微信模板消息的response是：{}", responseStr);
            if (StringUtils.isBlank(responseStr)) {
                return null;
            }
            return objectMapper.readValue(responseStr, ModelMsgResponse.class);
        } catch (IOException e) {
            log.error("发送微信模板消息失败", e);
            return null;
        }
    }

    public static JssdkSignResponse sign(String jsapi_ticket, String url) {
        String nonce_str = RandomStringGenerator.getRandomStringByLength(32);
        String timestamp = create_timestamp();
        String orignStr;
        String signature = "";
        Map<String, String> params = new HashMap<>();
        params.put("jsapi_ticket", jsapi_ticket);
        params.put("noncestr", nonce_str);
        params.put("timestamp", timestamp);
        params.put("url", url);

        //1.1 对所有待签名参数按照字段名的ASCII 码从小到大排序（字典序）
        Map<String, String> sortParams = Signature.sortAsc(params);
        //1.2 使用URL键值对的格式拼接成字符串
        orignStr = Signature.mapJoin(sortParams, false);
        log.debug(orignStr);

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(orignStr.getBytes("UTF-8")); //对string1 字符串进行SHA-1加密处理
            signature = byteToHex(crypt.digest());  //对加密后字符串转成16进制
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            log.error("生成js-sdk签名信息失败");
        }

        return new JssdkSignResponse(false, url, jsapi_ticket, nonce_str, timestamp, signature);
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    //生成时间戳字符串
    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    public static void main(String[] args) {
//        WechatUtil.getBasicToken();

        /**
         *您好，本次服务已结束，谢谢您的支持，请对我们本次服务给予评价
         * 姓名：{{userName.DATA}}
         * 电话：{{userMobile.DATA}}
         * 地址：{{userAddr.DATA}}
         * 订单号:{{orderId.DATA}}
         */

        String accessToken = "14_q291U-9pAFPesnDGVbW8FeXPBOtoOrm2LgswmNvplGEPkzvrg3fY-mpzHVKaCNJnP4rfPzOr0iWxif_pDsuKr0lnylJ6PKp7Rqo56UHr3Om1v-1sllONGsBahYF8j9xDnfEes641f-NKg-1oSEVgAHACOF";
        String openId = "oecPZ5v-AAcSBE0ftO0gY54Lh1Po";
        String templateId = "vdCaNcWdA1oJE4CU8bG_j9OkAaT7T7H8d1dvoM2DAmM";
        String redirectUrl = "http://www.baidu.com";
        Map<String, String> extension = new HashMap<>();
        extension.put("userName", "Baizongwei");
        extension.put("userMobile", "18666177436");
        extension.put("userAddr", "广州市天河区 南村镇 16号");
        extension.put("orderId", "5555004545");
        ModelMsgResponse response = WechatUtil.sendModelMsg(accessToken, openId, templateId, redirectUrl, extension);
        log.debug("返回值：{}", response);
    }

}
