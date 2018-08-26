package com.baymin.springboot.rest.exception;

import lombok.Data;

import javax.ws.rs.core.Response.Status;
import com.baymin.springboot.common.exception.ErrorInfo;

/**
 * Created by Baymin on 2017/4/10.
 */
@Data
public class SpringBootException extends RuntimeException {

    private Status statusCode;
    private ErrorInfo error;

    public SpringBootException() {
        this.statusCode = Status.INTERNAL_SERVER_ERROR;
    }

    public SpringBootException(ErrorInfo error, String message) {
        super(message);
        this.statusCode = Status.INTERNAL_SERVER_ERROR;
        this.error = error;
    }

    public SpringBootException(Status statusCode, ErrorInfo error) {
        super(error.getErrorDescription());
        this.statusCode = Status.INTERNAL_SERVER_ERROR;
        this.statusCode = statusCode;
        this.error = error;
    }

    public SpringBootException(Status statusCode, ErrorInfo error, String message) {
        super(message);
        this.statusCode = Status.INTERNAL_SERVER_ERROR;
        this.statusCode = statusCode;
        this.error = error;
    }

    public SpringBootException(Status statusCode, ErrorInfo error, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = Status.INTERNAL_SERVER_ERROR;
        this.statusCode = statusCode;
        this.error = error;
    }

}
