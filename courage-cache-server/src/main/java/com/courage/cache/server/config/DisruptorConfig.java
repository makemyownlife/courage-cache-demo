package com.courage.cache.server.config;

import com.courage.cache.service.disruptor.DataEventListener;
import com.courage.cache.service.disruptor.DataEvent;
import com.courage.cache.service.disruptor.DisruptorManager;
import com.courage.cache.service.disruptor.DisruptorProducer;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore(RedisConfig.class)
public class DisruptorConfig {

    private final static Logger logger = LoggerFactory.getLogger(DisruptorConfig.class);

    private final static String LIST_KEY = "disruptor:list";

    @Autowired
    private RedissonClient redissonClient;

    @Bean
    public DataEventListener<String> createConsumerListener() {
        DataEventListener<String> dataEventListener = new DataEventListener<String>() {
            @Override
            public void processDataEvent(DataEvent<String> dataEvent) throws InterruptedException {
                logger.info("processDateEvent data:" + dataEvent.getData());
                redissonClient.getList(LIST_KEY).add(dataEvent.getData());
            }
        };
        return dataEventListener;
    }

    @Bean
    public DisruptorProducer<String> createProducer(DataEventListener dataEventListener) {
        DisruptorManager disruptorManage = new DisruptorManager(dataEventListener,
                8,
                1024 * 1024);
        disruptorManage.start();
        return disruptorManage.getProducer();
    }

}
