package com.baymin.springboot.service.impl;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.pay.wechat.WechatUtil;
import com.baymin.springboot.pay.wechat.param.pojo.BasicTokenResponse;
import com.baymin.springboot.pay.wechat.param.pojo.JsapiTicketResponse;
import com.baymin.springboot.service.IWechatService;
import com.baymin.springboot.store.payload.TicketRequestVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class WechatServiceImpl implements IWechatService {

    private static final String BASIC_ACCESS_TOKEN = "WECHAT_BASIC_ACCESS_TOKEN";
    private static final String JSAPI_TICKET = "WECHAT_JSAPI_TICKET";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String getJsapiTicket(TicketRequestVo requestVo) {
        String ticket = stringRedisTemplate.opsForValue().get(JSAPI_TICKET);
        if (StringUtils.isBlank(ticket)) {
            String accessToken = getBasicAccessToken();
            JsapiTicketResponse response = WechatUtil.getJsapiTicket(accessToken);
            if (Objects.isNull(response) || StringUtils.isBlank(response.getTicket())) {
                throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.server_error.name(), "获取微信jsapi_ticket失败"));
            }
            stringRedisTemplate.opsForValue().set(JSAPI_TICKET, response.getTicket(), response.getExpiresIn() - 100, TimeUnit.SECONDS);
            ticket = response.getTicket();
        }
        return ticket;
    }

    @Override
    public String getBasicAccessToken() {
        String accessToken = stringRedisTemplate.opsForValue().get("WECHAT_BASIC_ACCESS_TOKEN");
        if (StringUtils.isBlank(accessToken)) {
            BasicTokenResponse basicToken = WechatUtil.getBasicToken();
            if (Objects.isNull(basicToken) || StringUtils.isBlank(basicToken.getAccessToken())) {
                throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.server_error.name(), "获取微信token失败"));
            }
            stringRedisTemplate.opsForValue().set("WECHAT_BASIC_ACCESS_TOKEN", basicToken.getAccessToken(), basicToken.getExpiresIn() - 100, TimeUnit.SECONDS);
            accessToken = basicToken.getAccessToken();
        }
        return accessToken;
    }
}
