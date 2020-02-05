package com.nowcoder.community.quartz;

import org.quartz.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HelloQuartz  implements Job {

    public HelloQuartz() {
        System.out.println("欢迎访问helloJob");
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail=context.getJobDetail();
        Trigger trigger=context.getTrigger();
        JobDataMap jobDataMap=jobDetail.getJobDataMap();
        JobDataMap triggerDataMap=trigger.getJobDataMap();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String job=jobDetail.getKey().getGroup();
        System.out.println("任务调度:组"+job+",工作名："+jobDataMap.get("message")+"时间:"+ simpleDateFormat.format(new Date()));
        System.out.println("触发器:"+trigger.getKey().getName()+"message:"+triggerDataMap.get("message"));
    }
}
