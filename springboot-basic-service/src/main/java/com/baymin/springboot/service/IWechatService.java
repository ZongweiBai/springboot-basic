package com.baymin.springboot.service;

import com.baymin.springboot.store.payload.TicketRequestVo;

public interface IWechatService {

    String getBasicAccessToken();

    String getJsapiTicket(TicketRequestVo requestVo);
}
