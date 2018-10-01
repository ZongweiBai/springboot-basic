package com.baymin.springboot.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dycdpapi.model.v20170525.*;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baymin.springboot.common.constant.Constant;
import com.baymin.springboot.service.AliyunService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ebaizon on 11/28/2017.
 */
@Service
@Transactional
public class AliyunServiceImpl implements AliyunService {

    @Override
    public ChargeResponse chargeData(String phoneNumber, String outId, Integer dataNum) throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", Constant.AliyunAPI.accessKeyId, Constant.AliyunAPI.accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", Constant.AliyunAPI.dycdpProduct, Constant.AliyunAPI.dycdpDomain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        ChargeRequest request = new ChargeRequest();
        //必填-待充值的号码
        request.setPhoneNumber(phoneNumber);
        //必填-充值档位,需要先通过档位查询接口查询当前待充值手机号码所支持的档位
        request.setGrade(String.valueOf(dataNum));
        //可选-是否分省默认为false
        request.setIsProvince("false");
        //可选-0全国流量 1省内流量-默认不填为0
        request.setScope("0");
        //可选-充值备注
        request.setReason("remark");
        //必填-调用方提供的充值流水号,服务提供方针对同一流水号只做一次充值动作。
        request.setOutId(outId);

        //hint 此处可能会抛出异常，注意catch

        return acsClient.getAcsResponse(request);
    }

    @Override
    public QueryGradesResponse queryGrades(String phoneNumber) throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", Constant.AliyunAPI.accessKeyId, Constant.AliyunAPI.accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", Constant.AliyunAPI.dycdpProduct, Constant.AliyunAPI.dycdpDomain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        QueryGradesRequest request = new QueryGradesRequest();
        //可选-待充值的号码,如果不填,会返回各个运营商支持的流量档位情况
        request.setPhoneNumber(phoneNumber);
        //hint 此处可能会抛出异常，注意catch
        return acsClient.getAcsResponse(request);
    }

    @Override
    public QueryChargeDetailResponse queryChargeDetail(String outId) throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", Constant.AliyunAPI.accessKeyId, Constant.AliyunAPI.accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", Constant.AliyunAPI.dycdpProduct, Constant.AliyunAPI.dycdpDomain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        QueryChargeDetailRequest request = new QueryChargeDetailRequest();
        //必填-调用方提供的充值流水号
        request.setOutId(outId);

        //hint 此处可能会抛出异常，注意catch

        return acsClient.getAcsResponse(request);
    }

    @Override
    public SendSmsResponse sendSms(String mobilePhone, String code, String codeTemplate) throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", Constant.AliyunAPI.accessKeyId, Constant.AliyunAPI.accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", Constant.AliyunAPI.dysmsProduct, Constant.AliyunAPI.dysmsDomain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(mobilePhone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(Constant.AliyunAPI.signName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(codeTemplate);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
//        request.setTemplateParam("{\"code\":\"" + code + "\"}");

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch

        return acsClient.getAcsResponse(request);
    }

    @Override
    public QuerySendDetailsResponse querySendDetails(String bizId) throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", Constant.AliyunAPI.accessKeyId, Constant.AliyunAPI.accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", Constant.AliyunAPI.dysmsProduct, Constant.AliyunAPI.dysmsDomain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber("15000000000");
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        //hint 此处可能会抛出异常，注意catch

        return acsClient.getAcsResponse(request);
    }
}
