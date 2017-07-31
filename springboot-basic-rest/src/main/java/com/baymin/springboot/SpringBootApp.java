package com.baymin.springboot;

import com.baymin.springboot.rest.exception.SpringBootExceptionMapper;
import com.baymin.springboot.rest.api.UserProfileApi;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

/**
 * Created by Baymin on 2017/4/9.
 */
@SpringBootApplication
public class SpringBootApp {

    @Autowired
    private Bus bus;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApp.class, args);
    }

    @Bean
    public UserProfileApi getUserProfileApi() {
        return new UserProfileApi();
    }

    @Bean
    public Server rsServer() {
        JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setBus(bus);
//        endpoint.setServiceBeans(Arrays.asList(getUserProfileApi()));
        endpoint.setAddress("/");

//        endpoint.setInInterceptors(Arrays.asList(new LoggingInInterceptor()));
//        endpoint.setInFaultInterceptors(Arrays.asList(new LoggingInInterceptor()));
//        endpoint.setOutInterceptors(Arrays.asList(new LoggingOutInterceptor()));
//        endpoint.setOutFaultInterceptors(Arrays.asList(new LoggingOutInterceptor()));

        endpoint.setProviders(Arrays.asList(new JacksonJaxbJsonProvider(), new SpringBootExceptionMapper()));
        return endpoint.create();
    }
}
