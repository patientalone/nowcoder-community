package com.nowcoder.community.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

public class HelloSchedulerTrigger {
    public static void main(String[] args) {
        Date startTime=new Date(System.currentTimeMillis()+1000);
        Date endTime=new Date(System.currentTimeMillis()+5000);
        try {
            //调度器
            Scheduler scheduler= StdSchedulerFactory.getDefaultScheduler();
            //触发器
            Trigger trigger= TriggerBuilder.newTrigger()
                    .withIdentity("trigger1","group1")
//                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(5)
                            .repeatForever())
                    .startAt(startTime)
                    .endAt(endTime)
                    .build();

            JobDetail job=JobBuilder.newJob(HelloJobTrigger.class)
                    .withIdentity("job1","group1")
                    .usingJobData("message","打印日志")
                    .build();
            scheduler.scheduleJob(job,trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }
}
