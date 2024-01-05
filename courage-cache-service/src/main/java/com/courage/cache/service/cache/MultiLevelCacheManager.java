package com.courage.cache.service.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 两级缓存
 * Created by zhangyong on 2021/12/5.
 */
public class MultiLevelCacheManager implements CacheManager {

    private final static Logger logger = LoggerFactory.getLogger(MultiLevelCacheManager.class);

    private Map<String, MultiLevelCacheConfig> configMap;

    private ConcurrentMap<String, Cache> instanceMap = new ConcurrentHashMap<String, Cache>();

    private MultiLevelChannel multiLevelChannel;

    public MultiLevelCacheManager(MultiLevelChannel multiLevelChannel, Map<String, MultiLevelCacheConfig> configMap) {
        this.multiLevelChannel = multiLevelChannel;
        this.configMap = configMap;
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = instanceMap.get(name);
        if (cache != null) {
            return cache;
        }
        MultiLevelCacheConfig config = configMap.get(name);
        cache = createMultiLevelCache(name, config);
        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(configMap.keySet());
    }

    private synchronized Cache createMultiLevelCache(String cacheName, MultiLevelCacheConfig multiLevelCacheConfig) {
        com.github.benmanes.caffeine.cache.Cache localCache = multiLevelChannel.getCaffeine().build();

        return null;
    }

}
