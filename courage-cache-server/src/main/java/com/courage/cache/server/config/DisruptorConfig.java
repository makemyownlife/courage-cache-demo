package com.courage.cache.server.config;

import com.courage.cache.service.disruptor.DisruptorManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DisruptorConfig {

    @Bean
    public DisruptorManager createManage() {
        DisruptorManager disruptorManage = new DisruptorManager(null);
        disruptorManage.start();
        return disruptorManage;
    }

}
