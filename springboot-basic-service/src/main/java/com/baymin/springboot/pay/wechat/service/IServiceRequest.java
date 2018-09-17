package com.baymin.springboot.pay.wechat.service;

public interface IServiceRequest {

	String sendPost(String api_url, Object xmlObj) throws Exception;

}