package com.baymin.springboot.timerserver.config;

import com.baymin.springboot.timerserver.job.SmsSendJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfig {

    @Bean
    public JobDetail smsSendJobDetail() {
        return JobBuilder.newJob(SmsSendJob.class).withIdentity("SmsSendJob")
                .storeDurably().build();
    }

    @Bean
    public Trigger smsSendJobTrigger() {
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(30).repeatForever();
        return TriggerBuilder.newTrigger().forJob(smsSendJobDetail()).withIdentity("SmsSendTrigger").withSchedule(simpleScheduleBuilder).build();
    }

}
