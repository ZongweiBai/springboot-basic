package com.baymin.springboot.webserver.config;

import com.baymin.springboot.common.logging.RestLoggingInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
public class RestTemplateConfig {

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(60);
        manager.setDefaultMaxPerRoute(20);
        manager.closeExpiredConnections();
        manager.closeIdleConnections(1, TimeUnit.SECONDS);
        return manager;
    }

    @Bean
    public RequestConfig requestConfig() {
        int timeout = 5000;
        RequestConfig customRequestConfig = RequestConfig.custom()
                .setSocketTimeout(timeout)
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .build();
        return customRequestConfig;
    }

    @Bean
    public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager, RequestConfig requestConfig) {
        CloseableHttpClient result = HttpClientBuilder
                .create()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
        return result;
    }

    @Bean
    public RestTemplate restTemplate(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setInterceptors(Collections.singletonList(new RestLoggingInterceptor()));
        return restTemplate;
    }
}
