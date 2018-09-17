package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.constant.WebConstant;
import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.pay.wechat.WechatConfig;
import com.baymin.springboot.pay.wechat.param.Util;
import com.baymin.springboot.pay.wechat.param.WXProtocolData;
import com.baymin.springboot.pay.wechat.param.WXResCommonData;
import com.baymin.springboot.pay.wechat.param.jsApi.JsApiOrderReqData;
import com.baymin.springboot.pay.wechat.param.paynotify.PayNotifyReqData;
import com.baymin.springboot.pay.wechat.param.unifiedorder.UnifiedOrderResData;
import com.baymin.springboot.service.IOrderService;
import com.baymin.springboot.service.IPayRecordService;
import com.baymin.springboot.service.IUserProfileService;
import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.entity.PayRecord;
import com.baymin.springboot.store.entity.UserProfile;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.baymin.springboot.common.exception.ErrorDescription.*;

@Api(value = "微信相关", tags = "微信相关")
@RestController
@RequestMapping(path = "/api/wechat")
public class WechatApi {

    private static final Logger logger = LoggerFactory.getLogger(WechatApi.class);

    @Autowired
    private IUserProfileService userProfileService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IPayRecordService payRecordService;

    @ResponseBody
    @GetMapping(value = "openId")
    public void getUserOpenId(String code, String grant_type, String accountId, HttpServletResponse response) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpURLConnection connection = null;
        BufferedReader bufferReader = null;
        try {
            StringBuffer urlAddr = new StringBuffer("https://api.weixin.qq.com/sns/oauth2/access_token");
            urlAddr.append("?appid=").append(WechatConfig.AppID);
            urlAddr.append("&secret=").append(WechatConfig.AppSecret);
            urlAddr.append("&code=").append(code);
            urlAddr.append("&grant_type=").append(grant_type);
            URL url = new URL(urlAddr.toString());    // 把字符串转换为URL请求地址
            connection = (HttpURLConnection) url.openConnection();// 打开连接
            connection.connect();// 连接会话
            // 获取输入流
            bufferReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferReader.readLine()) != null) {// 循环读取流
                sb.append(line);
            }
            String openIdInfo = sb.toString();
            resultMap.put("result", 500);
            if (StringUtils.isBlank(openIdInfo)) {
                throw new WebServerException(HttpStatus.INTERNAL_SERVER_ERROR, new ErrorInfo(ErrorCode.server_error.name(), GET_WECHAT_OPENID_ERROR));
            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> resultObject = objectMapper.readValue(openIdInfo, new TypeReference<Map<String, Object>>() {
                });
                Object openid = resultObject.get("openid");
                if (openid == null) {
                    throw new WebServerException(HttpStatus.INTERNAL_SERVER_ERROR, new ErrorInfo(ErrorCode.server_error.name(), GET_WECHAT_OPENID_ERROR));
                } else {
                    if (StringUtils.isNotBlank(accountId)) {
                        UserProfile user = userProfileService.findById(accountId);
                        if (user != null) {
                            user.setIdpId(openid.toString());
                            userProfileService.saveUserProfile(user);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebServerException(HttpStatus.INTERNAL_SERVER_ERROR, new ErrorInfo(ErrorCode.server_error.name(), GET_WECHAT_OPENID_ERROR));
        } finally {
            try {
                if (bufferReader != null) {
                    bufferReader.close();// 关闭流
                }
                if (connection != null) {
                    connection.disconnect();// 断开连接
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 使用微信支付商品费用
     */
    @ResponseBody
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public JsApiOrderReqData payOrderWithWeChat(HttpServletRequest request, HttpServletResponse response) {
        String orderId = request.getParameter("orderId");
        String userId = request.getParameter("userId");
        if (StringUtils.isBlank(orderId) || StringUtils.isBlank(userId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }

        Order order = orderService.queryOrderById(orderId);
        if (Objects.isNull(order)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), ORDER_INFO_NOT_CORRECT));
        }

        Map<String, Object> resultMap;
        UserProfile user = userProfileService.findById(userId);

        if (Objects.isNull(user) || StringUtils.isBlank(user.getIdpId())) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        resultMap = payRecordService.payOrderWithWeChat(user, order, WechatConfig.AppID, WechatConfig.mchID, WechatConfig.key);
        return dealWechatOrderResponse(response, resultMap);
    }

    @RequestMapping(value = "/pay/notify", method = RequestMethod.POST)
    @ResponseBody
    public void notifyPayResult(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取收到的报文
        BufferedReader reader = request.getReader();
        String line;
        StringBuilder inputString = new StringBuilder();
        WXProtocolData protocolData = new WXProtocolData();
        try {
            while ((line = reader.readLine()) != null) {
                inputString.append(line);
            }
            request.getReader().close();
            String notifyStr = inputString.toString();
            logger.error("微信支付结果通知【" + notifyStr + "】");
            PayNotifyReqData resData = (PayNotifyReqData) Util.getObjectFromXML(notifyStr, PayNotifyReqData.class);
            if ("SUCCESS".equalsIgnoreCase(resData.getResult_code())) {

                PayRecord payRecord = payRecordService.getRecordByTradeNo(resData.getOut_trade_no());
                if (payRecord == null) {
                    protocolData.setReturn_code(WXResCommonData.FAIL_RETURN_CODE);
                    protocolData.setReturn_msg(WXResCommonData.FAIL_RESULT_CODE);
                    returnToWechat(response, protocolData);
                    return;
                }
                if (payRecord.getPayResult()) {
                    protocolData.setReturn_code(WXResCommonData.SUCCESS_RETURN_CODE);
                    protocolData.setReturn_msg(WXResCommonData.SUCCESS_RESULT_CODE);
                    returnToWechat(response, protocolData);
                    return;
                }

                payRecord.setTransactionId(resData.getTransaction_id());
                payRecordService.orderPaySuccess(payRecord);

                returnToWechat(response, protocolData);

            } else {
                protocolData.setReturn_code(WXResCommonData.FAIL_RETURN_CODE);
                protocolData.setReturn_msg(WXResCommonData.FAIL_RESULT_CODE);
                returnToWechat(response, protocolData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            protocolData.setReturn_code(WXResCommonData.FAIL_RETURN_CODE);
            protocolData.setReturn_msg(WXResCommonData.FAIL_RESULT_CODE);
            returnToWechat(response, protocolData);
        }

    }

    private void returnToWechat(HttpServletResponse response, WXProtocolData wxData) throws IOException {
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        String rtWeiXinStr = xStreamForRequestPostData.toXML(wxData);
        logger.error("商户处理后同步返回给微信参数【" + rtWeiXinStr + "】");
        response.setContentType("text/xml;charset=UTF-8");
        response.getWriter().write(rtWeiXinStr);
    }

    private JsApiOrderReqData dealWechatOrderResponse(HttpServletResponse response, Map<String, Object> resultMap) {
        if (StringUtils.equals(String.valueOf(resultMap.get(WebConstant.RESULT)), String.valueOf(WebConstant.FAULT))) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), WECHAT_PAY_ERROR));
        } else {
            UnifiedOrderResData resData = (UnifiedOrderResData) resultMap.get(WebConstant.INFO);
            resData.setAppid(WechatConfig.AppID);
            return new JsApiOrderReqData(resData, WechatConfig.key);
        }
    }

}