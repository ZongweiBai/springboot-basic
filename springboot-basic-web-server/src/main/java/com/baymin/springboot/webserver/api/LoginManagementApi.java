package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.constant.Constant;
import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.common.model.TokenVo;
import com.baymin.springboot.common.util.JwtUtil;
import com.baymin.springboot.service.IRedisService;
import com.baymin.springboot.service.IUserProfileService;
import com.baymin.springboot.store.entity.UserProfile;
import com.baymin.springboot.webserver.request.LoginRequest;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

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
    private IRedisService redisService;

    @ApiOperation(value = "登陆")
    @PostMapping("login")
    @ResponseBody
    public TokenVo login(@RequestBody LoginRequest loginRequest) {
        String userAccount = loginRequest.getAccount();
        String smsCode = loginRequest.getSmsCode();
        String password = loginRequest.getPassword();

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
            UserProfile userProfile = userProfileService.findById(String.valueOf(accountMap.get("userId")));
            if (userProfile == null) {
                throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), TOKEN_INVALID));
            }
            String subject = JwtUtil.generalSubject(userProfile.getId(), Constant.JWTAPI.JWT_TOKEN);
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
