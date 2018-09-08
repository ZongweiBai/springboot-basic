package com.baymin.springboot.webserver.interceptor;

import com.baymin.springboot.common.constant.Constant;
import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorDescription;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.common.util.JwtUtil;
import com.baymin.springboot.service.IUserProfileService;
import com.baymin.springboot.store.entity.UserProfile;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
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
public class AuthorizationInterceptor implements HandlerInterceptor {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private IUserProfileService userProfileService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isBlank(authorization)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            throw new WebServerException(HttpStatus.UNAUTHORIZED, new ErrorInfo(ErrorCode.unauthorized.name(), ErrorDescription.TOKEN_INVALID));
        }
        try {
            Claims claims = JwtUtil.parseJWT(authorization.replace("Bearer ", ""));
            Map<String, Object> accountMap = objectMapper.readValue(claims.getSubject(), new TypeReference<Map<String, Object>>() {
            });
            if (!StringUtils.equals(Constant.JWTAPI.JWT_TOKEN, String.valueOf(accountMap.get("tokenType")))) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                throw new WebServerException(HttpStatus.UNAUTHORIZED, new ErrorInfo(ErrorCode.unauthorized.name(), ErrorDescription.TOKEN_INVALID));
            }
            String accountFromHeader = String.valueOf(accountMap.get("userId"));
            UserProfile userDetails = userProfileService.findById(accountFromHeader);
            if (Objects.isNull(userDetails)) {
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
