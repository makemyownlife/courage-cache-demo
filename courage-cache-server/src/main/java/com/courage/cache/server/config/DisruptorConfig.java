package com.courage.cache.server.config;

import com.courage.cache.service.disruptor.DataEvent;
import com.courage.cache.service.disruptor.DisruptorManager;
import com.courage.cache.service.disruptor.ConsumerListener;
import com.courage.cache.service.disruptor.DisruptorProducer;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@AutoConfigureBefore(RedisConfig.class)
public class DisruptorConfig {

    private final static Logger logger = LoggerFactory.getLogger(DisruptorConfig.class);

    private final static String LIST_KEY = "disruptor:list";

    @Autowired
    private RedissonClient redissonClient;

    @Bean
    public ConsumerListener<String> createConsumerListener() {
        ConsumerListener<String> consumerListener = new ConsumerListener<String>() {
            @Override
            public void processDataEvent(DataEvent<String> dataEvent) {
                logger.info("processDateEvent data:" + dataEvent.getData());
                redissonClient.getList(LIST_KEY).add(dataEvent.getData());
            }
        };
        return consumerListener;
    }

    @Bean
    public DisruptorProducer<String> createProducer(ConsumerListener consumerListener) {
        DisruptorManager disruptorManage = new DisruptorManager(consumerListener);
        disruptorManage.start();
        return disruptorManage.getProducer();
    }

}
