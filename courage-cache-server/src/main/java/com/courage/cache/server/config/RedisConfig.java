package com.courage.cache.server.config;

import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig {

    private final static Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }


    @Autowired
    private StreamMessageListener streamListener;


    @Bean
    public Subscription subscription(RedisConnectionFactory factory, RedisTemplate<String, Object> redisTemplate) {
        var options = StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(1))
                .build();

        // 创建 group
        boolean hasKey = redisTemplate.hasKey("mystream");
        if(!hasKey) {
            redisTemplate.opsForStream().createGroup("mystream", "mygroup");
        }

        var listenerContainer = StreamMessageListenerContainer.create(factory, options);
        var subscription = listenerContainer.receiveAutoAck(
                Consumer.from("mygroup", "courage-cache-demo"),
                StreamOffset.create(
                        "mystream", ReadOffset.lastConsumed()
                ),
                streamListener);
        listenerContainer.start();
        return subscription;
    }


}
