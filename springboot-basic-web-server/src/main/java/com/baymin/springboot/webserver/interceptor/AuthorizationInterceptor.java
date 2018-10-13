package com.baymin.springboot.webserver.interceptor;

import com.baymin.springboot.common.constant.Constant;
import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorDescription;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.common.util.JwtUtil;
import com.baymin.springboot.service.IStaffService;
import com.baymin.springboot.service.IUserProfileService;
import com.baymin.springboot.store.entity.ServiceStaff;
import com.baymin.springboot.store.entity.UserProfile;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private IUserProfileService userProfileService;

    @Autowired
    private IStaffService staffService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isBlank(authorization)) {
            log.error("Authorization 信息为空");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            throw new WebServerException(HttpStatus.UNAUTHORIZED, new ErrorInfo(ErrorCode.unauthorized.name(), ErrorDescription.TOKEN_INVALID));
        }
        try {
            Claims claims = JwtUtil.parseJWT(authorization.replace("Bearer ", ""));
            Map<String, Object> accountMap = objectMapper.readValue(claims.getSubject(), new TypeReference<Map<String, Object>>() {
            });
            if (!StringUtils.equals(Constant.JWTAPI.JWT_TOKEN, String.valueOf(accountMap.get("tokenType")))) {
                log.error("AccessToken 类型错误");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                throw new WebServerException(HttpStatus.UNAUTHORIZED, new ErrorInfo(ErrorCode.unauthorized.name(), ErrorDescription.TOKEN_INVALID));
            }
            String accountFromHeader = String.valueOf(accountMap.get("userId"));
            String userType = String.valueOf(accountMap.get("userType"));
            if (StringUtils.equals("U", userType)) {
                UserProfile userDetails = userProfileService.findById(accountFromHeader);
                if (Objects.isNull(userDetails)) {
                    log.error("用户ID：{}无法找到", accountFromHeader);
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    throw new WebServerException(HttpStatus.UNAUTHORIZED, new ErrorInfo(ErrorCode.unauthorized.name(), ErrorDescription.TOKEN_INVALID));
                }
            } else if (StringUtils.equals("S", userType)) {
                ServiceStaff staff = staffService.findById(String.valueOf(accountMap.get("userId")));
                if (staff == null) {
                    log.error("护士/护工ID：{}无法找到", accountFromHeader);
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    throw new WebServerException(HttpStatus.UNAUTHORIZED, new ErrorInfo(ErrorCode.unauthorized.name(), ErrorDescription.TOKEN_INVALID));
                }
            } else {
                log.error("accessToken所属的userType：{}错误", userType);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                throw new WebServerException(HttpStatus.UNAUTHORIZED, new ErrorInfo(ErrorCode.unauthorized.name(), ErrorDescription.TOKEN_INVALID));
            }

            MDC.put("USER_ID_IN_HEADER", accountFromHeader);

            return true;
        } catch (SignatureException | ExpiredJwtException | IOException e) {
            if (e instanceof SignatureException) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                throw new WebServerException(HttpStatus.UNAUTHORIZED, new ErrorInfo(ErrorCode.unauthorized.name(), ErrorDescription.TOKEN_INVALID));
            } else if (e instanceof ExpiredJwtException) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                throw new WebServerException(HttpStatus.UNAUTHORIZED, new ErrorInfo(ErrorCode.unauthorized.name(), ErrorDescription.TOKEN_EXPIRED));
            } else {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                throw new WebServerException(HttpStatus.INTERNAL_SERVER_ERROR, new ErrorInfo(ErrorCode.server_error.name(), ErrorDescription.SERVER_ERROR));
            }
        }
    }
}
