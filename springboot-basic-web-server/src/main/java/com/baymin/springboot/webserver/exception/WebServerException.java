package com.baymin.springboot.webserver.exception;

import lombok.Data;

import com.baymin.springboot.common.exception.ErrorInfo;
import org.springframework.http.HttpStatus;

/**
 * Created by Baymin on 2017/4/10.
 */
@Data
public class WebServerException extends RuntimeException {

    private HttpStatus statusCode;
    private ErrorInfo error;

    public WebServerException() {
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public WebServerException(ErrorInfo error, String message) {
        super(message);
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        this.error = error;
    }

    public WebServerException(HttpStatus statusCode, ErrorInfo error) {
        super(error.getErrorDescription());
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        this.statusCode = statusCode;
        this.error = error;
    }

    public WebServerException(HttpStatus statusCode, ErrorInfo error, String message) {
        super(message);
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        this.statusCode = statusCode;
        this.error = error;
    }

    public WebServerException(HttpStatus statusCode, ErrorInfo error, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        this.statusCode = statusCode;
        this.error = error;
    }

}
