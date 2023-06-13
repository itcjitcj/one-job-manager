package com.spdbccc.job.manager.service.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Configuration
public class ThreadPoolConfig {

    @Bean(name = "threadPool")
    public ExecutorService threadPool() {
        return Executors.newFixedThreadPool(10);
    }
}
