package com.baymin.springboot.webserver.config;

import com.baymin.springboot.common.logging.LoggingFilter;
import com.baymin.springboot.webserver.interceptor.AuthorizationInterceptor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * Created by ebaizon on 11/30/2017.
 */
@Component
@Configuration
@EnableAutoConfiguration
public class Config implements WebMvcConfigurer {

    @Resource
    private AuthorizationInterceptor authorizationInterceptor;

    @Bean
    public FilterRegistrationBean filterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new LoggingFilter());
        registration.addUrlPatterns("/api/*");
        return registration;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/login", "/api/login/smscode", "/api/token/refresh", "/api/wechat/*");
    }

}
