package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.constant.Constant;
import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.model.TokenVo;
import com.baymin.springboot.common.util.JwtUtil;
import com.baymin.springboot.service.IRedisService;
import com.baymin.springboot.service.IUserProfileService;
import com.baymin.springboot.store.entity.UserProfile;
import com.baymin.springboot.webserver.exception.WebServerException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

import static com.baymin.springboot.common.exception.ErrorDescription.*;

@RestController
@RequestMapping(path = "/")
public class LoginManagementApi {

    private static final Logger log = LoggerFactory.getLogger(LoginManagementApi.class);

    @Autowired
    private IUserProfileService userProfileService;

    @Autowired
    private IRedisService redisService;

    @PostMapping("login")
    @ResponseBody
    public void login(Map<String, String> request) {
        String userAccount = request.get("account");
        String smsCode = request.get("smsCode");
        String password = request.get("password");

        if (StringUtils.isBlank(userAccount) || (StringUtils.isBlank(smsCode) && StringUtils.isBlank(password))) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }

        UserProfile userProfile;
        if (StringUtils.isNotBlank(smsCode)) {
            String smsCodeInDB = redisService.get(userAccount + "_" + "login_sms_code");
            if (!StringUtils.equalsIgnoreCase(smsCode, smsCodeInDB)) {
                throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_SMS_CODE));
            }
            userProfile = userProfileService.findByAccount(userAccount);
            if (Objects.isNull(userProfile)) {
                throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_SMS_CODE));
            }
        } else {
            userProfile = userProfileService.findByAccount(userAccount);
            if (Objects.isNull(userProfile) || StringUtils.equals(password, userProfile.getPassword())) {
                throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_PASSWORD));
            }
        }

        try {
            String subject = JwtUtil.generalSubject(userProfile.getId(), Constant.JWTAPI.JWT_TOKEN);
            String accessToken = JwtUtil.createJWT(Constant.JWTAPI.JWT_ID, subject, Constant.JWTAPI.JWT_TTL);
            subject = JwtUtil.generalSubject(userProfile.getId(), Constant.JWTAPI.JWT_REFRESH_TOKEN);
            String refreshToken = JwtUtil.createJWT(Constant.JWTAPI.JWT_ID, subject, Constant.JWTAPI.JWT_REFRESH_TTL);

            TokenVo tokenVo = new TokenVo();
            tokenVo.setUserId(userProfile.getId());
            tokenVo.setAccessToken(accessToken);
            tokenVo.setRefreshToken(refreshToken);
            tokenVo.setExpiresIn(Constant.JWTAPI.JWT_TTL / 1000);
            tokenVo.setTokenType("bearer");
        } catch (Exception e) {
            log.error("Error occurred", e);
            throw new WebServerException(HttpStatus.INTERNAL_SERVER_ERROR, new ErrorInfo(ErrorCode.server_error.name(), SERVER_ERROR));
        }
    }

}
