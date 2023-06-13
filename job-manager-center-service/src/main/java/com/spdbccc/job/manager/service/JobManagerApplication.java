package com.spdbccc.job.manager.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableConfigurationProperties
@SpringBootApplication
@MapperScan("com.spdbccc.job.manager.db.*")
public class JobManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobManagerApplication.class, args);
    }
}
