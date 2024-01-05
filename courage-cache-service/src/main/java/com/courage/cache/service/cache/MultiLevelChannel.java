package com.courage.cache.service.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.redisson.api.RedissonClient;

public class MultiLevelChannel {

    private Caffeine caffeine;

    private RedissonClient redissonClient;

    public MultiLevelChannel(Caffeine caffeine, RedissonClient redissonClient) {
        this.caffeine = caffeine;
        this.redissonClient = redissonClient;
    }

    public Caffeine getCaffeine() {
        return caffeine;
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }
    
}
