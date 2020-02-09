package com.yumc.recipecenter.adminservice.usermanage.quartz.config;

import com.yumc.recipecenter.adminservice.usermanage.quartz.AlphaJob;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * 配置->数据库->调用
 */
@Configuration
public class QuartzConfig {

    @Bean
    public JobDetailFactoryBean alphaJobDetail(){
        JobDetailFactoryBean factoryBean=new JobDetailFactoryBean();
        factoryBean.setJobClass(AlphaJob.class);
        factoryBean.setName("alphaJob");
        factoryBean.setGroup("alphaGroup");
        factoryBean.setDurability(true);
        factoryBean.setRequestsRecovery(true);
        return factoryBean;
    }
    @Bean
    public SimpleTriggerFactoryBean alphaSimpleTrigger(JobDetail alphaJobDetail){
        SimpleTriggerFactoryBean factoryBean=new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(alphaJobDetail);
        factoryBean.setName("alphaSimpleTrigger");
        factoryBean.setGroup("alphaSimpleTriggerGroup");
        factoryBean.setRepeatInterval(3000);
        factoryBean.setJobDataMap(new JobDataMap());
        return factoryBean;
    }
    @Bean
    public CronTriggerFactoryBean alphaCronTrigger(JobDetail alphaJobDetail){
        CronTriggerFactoryBean factoryBean=new CronTriggerFactoryBean();
        factoryBean.setJobDetail(alphaJobDetail);
        factoryBean.setName("alphaCronTrigger");
        factoryBean.setGroup("alphaCronTriggerGroup");
        factoryBean.setCronExpression("0/5 * * * * ? *");
        factoryBean.setJobDataMap(new JobDataMap());
        return factoryBean;
    }

}
