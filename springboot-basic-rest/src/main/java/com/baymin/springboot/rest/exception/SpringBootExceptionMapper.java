package com.baymin.springboot.rest.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by ebaizon on 4/20/2017.
 */
public class SpringBootExceptionMapper implements ExceptionMapper<SpringBootException> {
    @Override
    public Response toResponse(SpringBootException e) {
        return Response.status(e.getStatusCode()).entity(e.getError()).build();
    }
}
