package com.baymin.springboot.webserver.exception;

import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebServerException.class)
    public @ResponseBody
    ErrorInfo handleEmployeeNotFoundException(HttpServletRequest request, HttpServletResponse response, WebServerException exception) {

        ErrorInfo errorInfo = exception.getError();
        response.setStatus(exception.getStatusCode().value());

        return errorInfo;
    }

}
