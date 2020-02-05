package com.nowcoder.community.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

public class HelloJobSchedulSimpleTrigger {
    public static void main(String[] args) {
        Date startTime=new Date(System.currentTimeMillis()+1000);
        Date endTime=new Date(System.currentTimeMillis()+5000);
        try {
            Scheduler scheduler= StdSchedulerFactory.getDefaultScheduler();
            JobDetail jobDetail = JobBuilder.newJob(HelloJobSimpleTrigger.class)
                    .withIdentity("job1", "group1")
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger", "group1")
                    .startAt(startTime)
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(5)
                            .withRepeatCount(5))
                    .build();
            scheduler.scheduleJob(jobDetail,trigger);
            scheduler.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
