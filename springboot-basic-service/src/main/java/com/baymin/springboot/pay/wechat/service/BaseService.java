package com.baymin.springboot.pay.wechat.service;

import com.baymin.springboot.pay.wechat.param.Configure;

public class BaseService {

    private String apiURL;
    private IServiceRequest serviceRequest;

    public void setServiceRequest(IServiceRequest request) {
        serviceRequest = request;
    }

    public BaseService(String api) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        this.apiURL = api;
        Class<?> c = Class.forName(Configure.getHttpsRequestClassName());
        this.serviceRequest = (IServiceRequest) c.newInstance();
    }

    protected String sendPost(Object xmlObj) throws Exception {
        return serviceRequest.sendPost(apiURL, xmlObj);
    }

}