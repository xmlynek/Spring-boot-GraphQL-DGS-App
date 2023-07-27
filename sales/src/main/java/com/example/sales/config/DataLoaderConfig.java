package com.example.sales.config;

import com.example.sales.contants.DataLoaderConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class DataLoaderConfig {

    @Bean(name = DataLoaderConstants.THREAD_POOL_EXECUTOR_NAME)
    public Executor dataLoaderThreadPoolExecutor() {
        return Executors.newCachedThreadPool();
    }
}
