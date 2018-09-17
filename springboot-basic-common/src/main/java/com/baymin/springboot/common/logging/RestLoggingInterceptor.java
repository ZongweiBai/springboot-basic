package com.baymin.springboot.common.logging;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Created by ebaizon on 5/16/2018.
 */
public class RestLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RestLoggingInterceptor.class);

    private String requestId;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        requestId = LoggingMessage.nextId();
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        return logResponse(response);
    }

    private void logRequest(HttpRequest request, byte[] body) throws IOException {
        if (Objects.isNull(logger) || !logger.isTraceEnabled()) {
            return;
        }
        StringBuilder logBuilder = new StringBuilder("Inbound Message\n--------------------------------------");
        logBuilder.append("\nID: ").append(requestId);
        logBuilder.append("\nAddress: ").append(request.getURI());
        logBuilder.append("\nHttp-Method: ").append(request.getMethod());
        logBuilder.append("\nHeaders: ").append(request.getHeaders());
        String payload = new String(body, "UTF-8");
        if (StringUtils.isNotBlank(payload)) {
            logBuilder.append("\nPayload: ").append(payload);
        }
        logBuilder.append("\n--------------------------------------");
        logger.trace(logBuilder.toString());
    }

    private ClientHttpResponse logResponse(ClientHttpResponse response) throws IOException {
        if (Objects.isNull(logger) || !logger.isTraceEnabled()) {
            return response;
        }

        final ClientHttpResponse responseCopy = new BufferingClientHttpResponseWrapper(response);
        StringBuilder logBuilder = new StringBuilder("Outbound Message\n--------------------------------------");
        logBuilder.append("\nID: ").append(requestId);
        logBuilder.append("\nResponse-Code: ").append(response.getStatusCode());
        logBuilder.append("\nHeaders: ").append(response.getHeaders());
        String payload = StreamUtils.copyToString(responseCopy.getBody(), Charset.defaultCharset());
        if (StringUtils.isNotBlank(payload)) {
            logBuilder.append("\nPayload: ").append(payload);
        }
        logBuilder.append("\n--------------------------------------");
        logger.trace(logBuilder.toString());
        return responseCopy;
    }

    private class BufferingClientHttpResponseWrapper implements ClientHttpResponse {

        private final ClientHttpResponse response;

        private byte[] body;

        public BufferingClientHttpResponseWrapper(ClientHttpResponse response) {
            this.response = response;
        }

        @Override
        public HttpStatus getStatusCode() throws IOException {
            return this.response.getStatusCode();
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return this.response.getRawStatusCode();
        }

        @Override
        public String getStatusText() throws IOException {
            return this.response.getStatusText();
        }

        @Override
        public void close() {
            this.response.close();
        }

        @Override
        public InputStream getBody() throws IOException {
            if (this.body == null) {
                this.body = StreamUtils.copyToByteArray(this.response.getBody());
            }
            return new ByteArrayInputStream(this.body);
        }

        @Override
        public HttpHeaders getHeaders() {
            return this.response.getHeaders();
        }
    }

}
