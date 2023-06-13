package com.spdbccc.job.manager.executor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableConfigurationProperties
@SpringBootApplication
@MapperScan("com.spdbccc.job.manager.db.*")
public class JobManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobManagerApplication.class, args);
    }

    @Configuration
    public static class SchedulerFactoryBeanConfig implements SchedulerFactoryBeanCustomizer {

        @Override
        public void customize(SchedulerFactoryBean schedulerFactoryBean) {
            schedulerFactoryBean.setBeanName("JobManagerScheduler");
        }

    }
}