package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.constant.WebConstant;
import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.common.util.HttpClientUtil;
import com.baymin.springboot.pay.wechat.WechatConfig;
import com.baymin.springboot.pay.wechat.WechatUtil;
import com.baymin.springboot.pay.wechat.param.Util;
import com.baymin.springboot.pay.wechat.param.WXProtocolData;
import com.baymin.springboot.pay.wechat.param.WXResCommonData;
import com.baymin.springboot.pay.wechat.param.jsApi.JsApiOrderReqData;
import com.baymin.springboot.pay.wechat.param.paynotify.PayNotifyReqData;
import com.baymin.springboot.pay.wechat.param.pojo.JssdkSignResponse;
import com.baymin.springboot.pay.wechat.param.pojo.TokenResponse;
import com.baymin.springboot.pay.wechat.param.pojo.UserInfoResponse;
import com.baymin.springboot.pay.wechat.param.unifiedorder.UnifiedOrderResData;
import com.baymin.springboot.service.*;
import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.enumconstant.OrderStatus;
import com.baymin.springboot.store.enumconstant.PayWay;
import com.baymin.springboot.store.payload.PayRequestVo;
import com.baymin.springboot.store.payload.TicketRequestVo;
import com.baymin.springboot.store.payload.TokenVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
    private IStaffService staffService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IPayRecordService payRecordService;

    @Autowired
    private IWechatService wechatService;

    @ApiOperation(value = "根据code获取用户openId")
    @ResponseBody
    @GetMapping(value = "/openId")
    public TokenVo getUserOpenId(String code, HttpServletResponse response) {
        try {
            String urlAddr = String.format(WECHAT_WEB_ACCESS_TOKEN_URL, WechatConfig.AppID, WechatConfig.AppSecret, code);
            String accessTokenResponse = HttpClientUtil.sendGet(urlAddr, null);
            logger.debug("获取到的openId信息是：{}", accessTokenResponse);
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
                    if (StringUtils.isBlank(userInfo)) {
                        throw new WebServerException(HttpStatus.INTERNAL_SERVER_ERROR, new ErrorInfo(ErrorCode.server_error.name(), GET_WECHAT_OPENID_ERROR));
                    }
                    userInfoResponse = objectMapper.readValue(userInfo, UserInfoResponse.class);

                    WechatUserInfo wechatUserInfo = userProfileService.saveWechatUserInfo(userInfoResponse);

                    ServiceStaff staff = staffService.findByIdpId(wechatUserInfo.getOpenid());
                    if (Objects.nonNull(staff)) {
                        staff.setIdpId(wechatUserInfo.getOpenid());
                        staff.setImgUrl(wechatUserInfo.getHeadimgurl());
                        staffService.updateStaff(staff);
                        return userProfileService.getTokenVo(staff.getId(), "S");
                    }

                    UserProfile userProfile = userProfileService.findByIdpId(wechatUserInfo.getOpenid());
                    if (Objects.nonNull(userProfile)) {
                        userProfile.setIdpId(wechatUserInfo.getOpenid());
                        userProfile.setImgUrl(wechatUserInfo.getHeadimgurl());
                        userProfileService.saveUserProfile(userProfile);
                        return userProfileService.getTokenVo(userProfile.getId(), "U");
                    }

                    TokenVo tokenVo = new TokenVo();
                    tokenVo.setWechatId(wechatUserInfo.getId());
                    return tokenVo;
                }
            }
        } catch (Exception e) {
            logger.error("获取微信用户信息失败！", e);
            throw new WebServerException(HttpStatus.INTERNAL_SERVER_ERROR, new ErrorInfo(ErrorCode.server_error.name(), GET_WECHAT_OPENID_ERROR));
        }
    }

    /**
     * 获取微信js-sdk签名信息
     */
    @ApiOperation(value = "获取微信js-sdk签名信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
    @ResponseBody
    @RequestMapping(value = "/jssdk/sign", method = RequestMethod.POST)
    public JssdkSignResponse getJsapiTicket(@RequestBody TicketRequestVo requestVo, HttpServletResponse response) {
        if (Objects.isNull(requestVo) || StringUtils.isBlank(requestVo.getUrl())) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }

        String jsapi_ticket = wechatService.getJsapiTicket(requestVo);

        return WechatUtil.sign(jsapi_ticket, requestVo.getUrl());
    }

    /**
     * 使用微信支付商品费用
     */
    @ApiOperation(value = "微信支付预下单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
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
        if (order.getPayWay() != PayWay.PAY_ONLINE_WITH_WECHAT) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), "订单支付方式不是微信支付，不能发起线上支付"));
        }
        if (order.getStatus() != OrderStatus.ORDER_UN_PAY && Objects.nonNull(order.getPayTime())) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.order_already_payed.name(), "订单已支付成功！"));
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
    @ResponseBody()
    public String notifyPayResult(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
                    logger.error("支付记录不存在");
                    protocolData.setReturn_code(WXResCommonData.FAIL_RETURN_CODE);
                    protocolData.setReturn_msg(WXResCommonData.FAIL_RESULT_CODE);
                    return returnToWechat(response, protocolData);
                }
                if (payRecord.getPayResult()) {
                    logger.info("订单{}已支付成功", payRecord.getOrderId());
                    protocolData.setReturn_code(WXResCommonData.SUCCESS_RETURN_CODE);
                    protocolData.setReturn_msg(WXResCommonData.SUCCESS_RESULT_CODE);
                    return returnToWechat(response, protocolData);
                }

                payRecord.setTransactionId(resData.getTransaction_id());
                payRecordService.orderPaySuccess(payRecord);

                return returnToWechat(response, protocolData);

            } else {
                protocolData.setReturn_code(WXResCommonData.FAIL_RETURN_CODE);
                protocolData.setReturn_msg(WXResCommonData.FAIL_RESULT_CODE);
                return returnToWechat(response, protocolData);
            }
        } catch (Exception e) {
            logger.error("error occurred when wechatpay notify");
            protocolData.setReturn_code(WXResCommonData.FAIL_RETURN_CODE);
            protocolData.setReturn_msg(WXResCommonData.FAIL_RESULT_CODE);
            return returnToWechat(response, protocolData);
        }

    }

    private String returnToWechat(HttpServletResponse response, WXProtocolData wxData) throws IOException {
        response.setContentType("text/xml");
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        String rtWeiXinStr = xStreamForRequestPostData.toXML(wxData);
        logger.info("商户处理后同步返回给微信参数【" + rtWeiXinStr + "】");
        return rtWeiXinStr;
    }

    private JsApiOrderReqData dealWechatOrderResponse(HttpServletResponse response, Map<String, Object> resultMap) {
        if (StringUtils.equals(String.valueOf(resultMap.get(WebConstant.RESULT)), String.valueOf(WebConstant.FAULT))) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), String.valueOf(resultMap.get(WebConstant.MESSAGE))));
        } else {
            UnifiedOrderResData resData = (UnifiedOrderResData) resultMap.get(WebConstant.INFO);
            resData.setAppid(WechatConfig.AppID);
            return new JsApiOrderReqData(resData, WechatConfig.key);
        }
    }

}
