package com.baymin.springboot.common.logging;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ebaizon on 1/29/2018.
 */
public class RequestWrapper extends HttpServletRequestWrapper {
    private InputStream inputStream;

    public RequestWrapper(HttpServletRequest request, byte[] payload) {
        super(request);
        inputStream = new ByteArrayInputStream(payload);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }
        };
    }
}
