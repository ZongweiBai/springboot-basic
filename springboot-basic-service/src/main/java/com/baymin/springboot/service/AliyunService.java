package com.baymin.springboot.service;

import com.aliyuncs.dycdpapi.model.v20170525.ChargeResponse;
import com.aliyuncs.dycdpapi.model.v20170525.QueryChargeDetailResponse;
import com.aliyuncs.dycdpapi.model.v20170525.QueryGradesResponse;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;

/**
 * Created by ebaizon on 11/28/2017.
 */
public interface AliyunService {

    ChargeResponse chargeData(String phoneNumber, String outId, Integer dataNum) throws ClientException;

    QueryGradesResponse queryGrades(String phoneNumber) throws ClientException;

    QueryChargeDetailResponse queryChargeDetail(String outId) throws ClientException;

    SendSmsResponse sendSms(String mobilePhone, String code, String codeTemplate) throws ClientException;

    QuerySendDetailsResponse querySendDetails(String bizId) throws ClientException;

}
