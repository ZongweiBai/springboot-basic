package com.baymin.springboot.service;

import com.baymin.springboot.store.payload.TicketRequestVo;

import java.util.Map;

public interface IWechatService {

    String getBasicAccessToken();

    String getJsapiTicket(TicketRequestVo requestVo);

    void sendTemplateMsg(String idpId, String templateId, Map<String, String> extension);
}
