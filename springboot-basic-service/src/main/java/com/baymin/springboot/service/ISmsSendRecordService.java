package com.baymin.springboot.service;

import com.baymin.springboot.store.entity.SmsSendRecord;

import java.util.List;
import java.util.Map;

public interface ISmsSendRecordService {

    void addSmsSendRecord(String mobile, String templateId, Map<String, String> templateParam);

    List<SmsSendRecord> queryUnSendRecords();

    void updateSmsCodeRecord(SmsSendRecord smsSendRecord);
}
