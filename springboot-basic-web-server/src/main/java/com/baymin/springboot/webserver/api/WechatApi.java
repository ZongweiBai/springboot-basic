package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.constant.Constant;
import com.baymin.springboot.common.constant.WebConstant;
import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.common.model.TokenVo;
import com.baymin.springboot.common.util.HttpClientUtil;
import com.baymin.springboot.common.util.JwtUtil;
import com.baymin.springboot.pay.wechat.WechatConfig;
import com.baymin.springboot.pay.wechat.param.Util;
import com.baymin.springboot.pay.wechat.param.WXProtocolData;
import com.baymin.springboot.pay.wechat.param.WXResCommonData;
import com.baymin.springboot.pay.wechat.param.jsApi.JsApiOrderReqData;
import com.baymin.springboot.pay.wechat.param.paynotify.PayNotifyReqData;
import com.baymin.springboot.pay.wechat.param.pojo.TokenResponse;
import com.baymin.springboot.pay.wechat.param.pojo.UserInfoResponse;
import com.baymin.springboot.pay.wechat.param.unifiedorder.UnifiedOrderResData;
import com.baymin.springboot.service.IOrderService;
import com.baymin.springboot.service.IPayRecordService;
import com.baymin.springboot.service.IUserProfileService;
import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.entity.PayRecord;
import com.baymin.springboot.store.entity.UserProfile;
import com.baymin.springboot.store.payload.PayRequestVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.baymin.springboot.common.exception.ErrorDescription.*;
import static com.baymin.springboot.pay.wechat.WechatConfig.WECHAT_WEB_ACCESS_TOKEN_URL;
import static com.baymin.springboot.pay.wechat.WechatConfig.WECHAT_WEB_USER_INFO_URL;

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

    @ApiOperation(value = "根据code获取用户openId并注册用户")
    @ResponseBody
    @GetMapping(value = "/openId")
    public TokenVo getUserOpenId(String code, String accountId, HttpServletResponse response) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String urlAddr = String.format(WECHAT_WEB_ACCESS_TOKEN_URL, WechatConfig.AppID, WechatConfig.AppSecret, code);
            String accessTokenResponse = HttpClientUtil.sendGet(urlAddr, null);
            if (StringUtils.isBlank(accessTokenResponse)) {
                throw new WebServerException(HttpStatus.INTERNAL_SERVER_ERROR, new ErrorInfo(ErrorCode.server_error.name(), GET_WECHAT_OPENID_ERROR));
            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                TokenResponse tokenResponse = objectMapper.readValue(accessTokenResponse, TokenResponse.class);
                if (tokenResponse.getOpenid() == null) {
                    throw new WebServerException(HttpStatus.INTERNAL_SERVER_ERROR, new ErrorInfo(ErrorCode.server_error.name(), GET_WECHAT_OPENID_ERROR));
                } else {
                    UserInfoResponse userInfoResponse = null;
                    String userInfoUrl = String.format(WECHAT_WEB_USER_INFO_URL, tokenResponse.getAccessToken(), tokenResponse.getOpenid());
                    String userInfo = HttpClientUtil.sendGet(userInfoUrl, null);
                    if (StringUtils.isNotBlank(userInfo)) {
                        userInfoResponse = objectMapper.readValue(userInfo, UserInfoResponse.class);
                    }

                    UserProfile user;
                    if (StringUtils.isNotBlank(accountId)) {
                        user = userProfileService.findById(accountId);
                    } else {
                        user = userProfileService.findByIdpId(tokenResponse.getOpenid());
                    }

                    if (Objects.isNull(user)) {
                        user = new UserProfile();
                        user.setOrderCount(0);
                        user.setRegisterTime(new Date());
                    }
                    user.setIdpId(tokenResponse.getOpenid());
                    if (Objects.nonNull(userInfoResponse)) {
                        user.setIdpNickName(userInfoResponse.getNickname());
                        user.setNickName(userInfoResponse.getNickname());
                    }

                    user = userProfileService.saveUserProfile(user);
                    String subject = JwtUtil.generalSubject(user.getId(), Constant.JWTAPI.JWT_TOKEN);
                    String accessToken = JwtUtil.createJWT(Constant.JWTAPI.JWT_ID, subject, Constant.JWTAPI.JWT_TTL);
                    subject = JwtUtil.generalSubject(user.getId(), Constant.JWTAPI.JWT_REFRESH_TOKEN);
                    String refreshToken = JwtUtil.createJWT(Constant.JWTAPI.JWT_ID, subject, Constant.JWTAPI.JWT_REFRESH_TTL);

                    TokenVo tokenVo = new TokenVo();
                    tokenVo.setUserId(user.getId());
                    tokenVo.setAccessToken(accessToken);
                    tokenVo.setRefreshToken(refreshToken);
                    tokenVo.setExpiresIn(Constant.JWTAPI.JWT_TTL / 1000);
                    tokenVo.setTokenType("bearer");
                    return tokenVo;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebServerException(HttpStatus.INTERNAL_SERVER_ERROR, new ErrorInfo(ErrorCode.server_error.name(), GET_WECHAT_OPENID_ERROR));
        }
    }

    /**
     * 使用微信支付商品费用
     */
    @ApiOperation(value = "微信支付预下单")
    @ResponseBody
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public JsApiOrderReqData payOrderWithWeChat(@RequestBody PayRequestVo requestVo, HttpServletResponse response) {
        if (Objects.isNull(requestVo) || StringUtils.isBlank(requestVo.getOrderId())
                || StringUtils.isBlank(requestVo.getUserId())) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }

        String orderId = requestVo.getOrderId();
        String userId = requestVo.getUserId();
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

    @ApiOperation(value = "微信支付回调接口")
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
