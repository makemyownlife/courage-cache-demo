package com.courage.cache.server.config;

import com.courage.cache.service.cache.MultiLevelCacheConfig;
import com.courage.cache.service.cache.MultiLevelCacheManager;
import com.courage.cache.service.cache.MultiLevelChannel;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.config.TransportMode;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class MyCacheConfig {

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.setTransportMode(TransportMode.NIO);
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

    @Bean
    public Caffeine<Object, Object> caffeine() {
        return Caffeine.newBuilder().initialCapacity(100)
                .maximumSize(500)
                .expireAfterWrite(40, TimeUnit.SECONDS)
                .weakKeys().recordStats();
    }

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient, Caffeine caffeine) {
        MultiLevelChannel multiLevelChannel = new MultiLevelChannel(caffeine, redissonClient);
        Map<String, MultiLevelCacheConfig> config = new ConcurrentHashMap<String, MultiLevelCacheConfig>();
        config.put("user_cache", new MultiLevelCacheConfig(20 * 60 * 1000, 10 * 60 * 1000));
        //初始化管理器
        return new MultiLevelCacheManager(multiLevelChannel, config);
    }


}
