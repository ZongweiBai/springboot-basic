package com.baymin.springboot.webserver.config;

import com.baymin.springboot.common.logging.LoggingFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by ebaizon on 11/30/2017.
 */
@Component
@Configuration
@EnableAutoConfiguration
public class Config {

    @Bean
    public FilterRegistrationBean filterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new LoggingFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }

}
