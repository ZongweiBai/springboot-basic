package com.baymin.springboot.webserver.api;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baymin.springboot.common.constant.Constant;
import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.common.util.JwtUtil;
import com.baymin.springboot.pay.wechat.param.RandomStringGenerator;
import com.baymin.springboot.service.AliyunService;
import com.baymin.springboot.service.IRedisService;
import com.baymin.springboot.service.IStaffService;
import com.baymin.springboot.service.IUserProfileService;
import com.baymin.springboot.store.entity.ServiceStaff;
import com.baymin.springboot.store.entity.UserProfile;
import com.baymin.springboot.store.entity.WechatUserInfo;
import com.baymin.springboot.store.payload.LoginRequestVo;
import com.baymin.springboot.store.payload.TokenVo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.baymin.springboot.common.exception.ErrorDescription.*;

@Api(value = "登陆及Token", tags = "登陆及Token")
@RestController
@RequestMapping(path = "/api/")
public class LoginManagementApi {

    private static final Logger log = LoggerFactory.getLogger(LoginManagementApi.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private IUserProfileService userProfileService;

    @Autowired
    private IStaffService staffService;

    @Autowired
    private IRedisService redisService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AliyunService aliyunService;

    @ApiOperation(value = "获取验证码")
    @GetMapping("login/smscode")
    @ResponseBody
    public void sendSmsCode(@RequestParam String mobilePhone) {
        if (StringUtils.isBlank(mobilePhone)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }

        String smsCode = RandomStringGenerator.randomNumString(4);
        try {
            SendSmsResponse response = aliyunService.sendSms(mobilePhone, smsCode, Constant.AliyunAPI.ORDER_USER_REG);
            if (StringUtils.equals("OK", response.getCode())) {
                stringRedisTemplate.opsForValue().set(mobilePhone + "_" + "login_sms_code", smsCode, 30, TimeUnit.MINUTES);
            } else {
                throw new WebServerException(HttpStatus.INTERNAL_SERVER_ERROR, new ErrorInfo(ErrorCode.server_error.name(), response.getMessage()));
            }
        } catch (ClientException e) {
            log.error("发送短信验证码失败", e);
            throw new WebServerException(HttpStatus.INTERNAL_SERVER_ERROR, new ErrorInfo(ErrorCode.server_error.name(), SMS_SEND_ERROR));
        }
    }

    @ApiOperation(value = "登陆并绑定微信")
    @PostMapping("login")
    @ResponseBody
    public TokenVo login(@RequestBody LoginRequestVo loginRequest) {
        String userAccount = loginRequest.getAccount();
        String smsCode = loginRequest.getSmsCode();
        String wechatId = loginRequest.getWechatId();

        if (StringUtils.isBlank(userAccount) || (StringUtils.isBlank(smsCode)) || StringUtils.isBlank(wechatId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }

        UserProfile userProfile;
//        String smsCodeInDB = redisService.get(userAccount + "_" + "login_sms_code");
//        if (!StringUtils.equalsIgnoreCase(smsCode, smsCodeInDB)) {
//            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_SMS_CODE));
//        }

        TokenVo tokenVo;
        try {
            ServiceStaff staff = staffService.findByMobile(userAccount);
            if (Objects.nonNull(staff)) {
                if (StringUtils.isNotBlank(wechatId)) {
                    staffService.updateIdpId(staff.getId(), wechatId);
                }

                tokenVo = userProfileService.getTokenVo(staff.getId(), "S");

            } else {
                userProfile = userProfileService.findByAccount(userAccount);
                if (Objects.isNull(userProfile)) {
                    WechatUserInfo userInfo = userProfileService.getWechatUserInfoById(wechatId);

                    userProfile = new UserProfile();
                    userProfile.setRegisterTime(new Date());
                    userProfile.setOrderCount(0);
                    userProfile.setNickName(Objects.nonNull(userInfo) ? userInfo.getNickname() : null);
                    userProfile.setIdpNickName(Objects.nonNull(userInfo) ? userInfo.getNickname() : null);
                    userProfile.setIdpId(wechatId);
                    userProfile.setAccount(userAccount);
                }
                userProfile.setLastLoginTime(new Date());
                userProfileService.saveUserProfile(userProfile);

                tokenVo = userProfileService.getTokenVo(userProfile.getId(), "U");
            }
            return tokenVo;
        } catch (Exception e) {
            log.error("Error occurred", e);
            throw new WebServerException(HttpStatus.INTERNAL_SERVER_ERROR, new ErrorInfo(ErrorCode.server_error.name(), SERVER_ERROR));
        }
    }

    @ApiOperation(value = "刷新Token")
    @RequestMapping(value = "/token/refresh", method = RequestMethod.POST)
    @ResponseBody
    public TokenVo refreshAccessToken(String refreshToken) {
        if (refreshToken == null) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        try {
            Claims claims = JwtUtil.parseJWT(refreshToken);
            Map<String, Object> accountMap = objectMapper.readValue(claims.getSubject(), new TypeReference<Map<String, Object>>() {
            });
            if (!StringUtils.equals(Constant.JWTAPI.JWT_REFRESH_TOKEN, String.valueOf(accountMap.get("tokenType")))) {
                throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), TOKEN_INVALID));
            }
            if (Objects.isNull(accountMap.get("userType"))) {
                throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), TOKEN_INVALID));
            }

            String subject;
            if (StringUtils.equals("U", String.valueOf(accountMap.get("userType")))) {
                UserProfile userProfile = userProfileService.findById(String.valueOf(accountMap.get("userId")));
                if (userProfile == null) {
                    throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), TOKEN_INVALID));
                }
                subject = JwtUtil.generalSubject(userProfile.getId(), "U", Constant.JWTAPI.JWT_TOKEN);
            } else if (StringUtils.equals("S", String.valueOf(accountMap.get("userType")))) {
                ServiceStaff staff = staffService.findById(String.valueOf(accountMap.get("userId")));
                if (staff == null) {
                    throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), TOKEN_INVALID));
                }
                subject = JwtUtil.generalSubject(staff.getId(), "S", Constant.JWTAPI.JWT_TOKEN);
            } else {
                throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), TOKEN_INVALID));
            }

            String accessToken = JwtUtil.createJWT(Constant.JWTAPI.JWT_ID, subject, Constant.JWTAPI.JWT_TTL);
            TokenVo tokenVo = new TokenVo();
            tokenVo.setAccessToken(accessToken);
            tokenVo.setRefreshToken(refreshToken);
            tokenVo.setExpiresIn(Constant.JWTAPI.JWT_TTL / 1000);
            tokenVo.setTokenType("bearer");
            return tokenVo;
        } catch (SignatureException | ExpiredJwtException | IOException e) {
            if (e instanceof SignatureException) {
                throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), TOKEN_INVALID));
            } else if (e instanceof ExpiredJwtException) {
                throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), TOKEN_EXPIRED));
            } else {
                throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), SERVER_ERROR));
            }
        }
    }

}
