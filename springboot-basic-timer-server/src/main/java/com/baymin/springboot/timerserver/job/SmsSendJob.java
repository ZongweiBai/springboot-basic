package com.baymin.springboot.timerserver.job;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baymin.springboot.common.constant.Constant;
import com.baymin.springboot.service.AliyunService;
import com.baymin.springboot.service.ISmsSendRecordService;
import com.baymin.springboot.store.entity.SmsSendRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@DisallowConcurrentExecution
@Slf4j
public class SmsSendJob extends QuartzJobBean {

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private ISmsSendRecordService smsSendRecordService;

    @Autowired
    private AliyunService aliyunService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("开始执行定时任务：发送短信");

        List<SmsSendRecord> recordList = smsSendRecordService.queryUnSendRecords();
        if (CollectionUtils.isNotEmpty(recordList)) {
            ArrayList<Future<String>> futures = new ArrayList<>();

            for (SmsSendRecord record : recordList) {
                SmsSendThread t = new SmsSendThread(record);
                Future<String> f = executorService.submit(t);
                futures.add(f);
            }
            log.info("获取执行结果中...");
            for (Future<String> f : futures) {
                try {
                    f.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            log.info("得到执行结果...");
        }

        log.info("结束执行定时任务：发送短信");
    }

    private class SmsSendThread implements Callable<String> {

        private SmsSendRecord smsSendRecord;

        public SmsSendThread(SmsSendRecord record) {
            this.smsSendRecord = record;
        }

        @Override
        public String call() {
            try {
                SendSmsResponse response = aliyunService.sendSms(smsSendRecord.getMobile(), smsSendRecord.getTemplateId(), smsSendRecord.getTemplateParams());
                if (StringUtils.equals("OK", response.getCode())) {
                    smsSendRecord.setSendResult(true);
                    if (StringUtils.equals(smsSendRecord.getTemplateId(), Constant.AliyunAPI.ORDER_USER_REG)) {
                        stringRedisTemplate.opsForValue().set(smsSendRecord.getMobile() + "_" + "login_sms_code", smsSendRecord.getTemplateParams().get("code"), 5, TimeUnit.MINUTES);
                    }
                } else {
                    smsSendRecord.setSendResult(false);
                    smsSendRecord.setResultDesc(response.getMessage());
                }
            } catch (ClientException | JsonProcessingException e) {
                log.warn("发送短信失败", e);
                smsSendRecord.setResultDesc("发送短信失败，内部异常");
                smsSendRecord.setSendResult(false);
            }
            smsSendRecord.setSendTime(new Date());
            smsSendRecordService.updateSmsCodeRecord(smsSendRecord);

            return "executeComplete";
        }
    }

}
