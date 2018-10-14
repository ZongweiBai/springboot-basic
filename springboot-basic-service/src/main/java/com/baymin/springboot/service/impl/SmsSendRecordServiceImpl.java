package com.baymin.springboot.service.impl;

import com.baymin.springboot.service.ISmsSendRecordService;
import com.baymin.springboot.store.entity.SmsSendRecord;
import com.baymin.springboot.store.repository.ISmsSendRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SmsSendRecordServiceImpl implements ISmsSendRecordService {

    @Autowired
    private ISmsSendRecordRepository smsSendRecordRepository;

    @Override
    public void addSmsSendRecord(String mobile, String templateId, Map<String, String> templateParam) {
        SmsSendRecord smsSendRecord = new SmsSendRecord();
        smsSendRecord.setCreateTime(new Date());
        smsSendRecord.setMobile(mobile);
        smsSendRecord.setTemplateParams(templateParam);
        smsSendRecord.setTemplateId(templateId);
        smsSendRecordRepository.save(smsSendRecord);
    }

    @Override
    public List<SmsSendRecord> queryUnSendRecords() {
        return smsSendRecordRepository.findUnsendRecord();
    }

    @Override
    public void updateSmsCodeRecord(SmsSendRecord smsSendRecord) {
        smsSendRecordRepository.save(smsSendRecord);
    }
}
