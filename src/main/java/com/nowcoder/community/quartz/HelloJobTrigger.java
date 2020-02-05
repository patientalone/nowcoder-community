package com.nowcoder.community.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HelloJobTrigger implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println("<工作内容>,当前时间:"+simpleDateFormat.format(new Date()));

        System.out.println(context.getTrigger().getKey());
        System.out.println(simpleDateFormat.format(context.getTrigger().getStartTime()));
        System.out.println(simpleDateFormat.format(context.getTrigger().getEndTime()));
    }
}
