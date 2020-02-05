package com.nowcoder.community.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QuarzTest {
    public static void main(String[] args) {
        try {
            //调度器
            Scheduler scheduler= StdSchedulerFactory.getDefaultScheduler();
            //触发器
            Trigger trigger= TriggerBuilder.newTrigger()
                    .withIdentity("trigger1","group1")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(5)
                            .repeatForever())
                    .usingJobData("message","simpleTrigger")
                    .build();

            JobDetail job=JobBuilder.newJob(HelloQuartz.class)
                    .withIdentity("job1","group1")
                    .usingJobData("message","打印job")
                    .build();
            scheduler.scheduleJob(job,trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }
}
