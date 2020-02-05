package com.nowcoder.community.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

public class HelloJobSchedulCronTrigger {
    public static void main(String[] args) {
        Date startTime=new Date(System.currentTimeMillis()+1000);
        Date endTime=new Date(System.currentTimeMillis()+5000);
        try {
            Scheduler scheduler= StdSchedulerFactory.getDefaultScheduler();
            JobDetail jobDetail = JobBuilder.newJob(HelloJobCronTrigger.class)
                    .withIdentity("job1", "group1")
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger", "group1")
//                    .startAt(startTime)
                    .withSchedule(CronScheduleBuilder.cronSchedule("30 * * * * ?"))
                    .build();
            scheduler.scheduleJob(jobDetail,trigger);
            scheduler.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
