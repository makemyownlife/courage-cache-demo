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
public class TwoLevelCacheManager implements CacheManager {

    private final static Logger logger = LoggerFactory.getLogger(TwoLevelCacheManager.class);

    Map<String, TwoLevelCacheConfig> configMap = new ConcurrentHashMap<String, TwoLevelCacheConfig>();

    private ConcurrentMap<String, Cache> instanceMap = new ConcurrentHashMap<String, Cache>();

    @Override
    public Cache getCache(String name) {
        Cache cache = instanceMap.get(name);
        if (cache != null) {
            return cache;
        }
        return null;
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(configMap.keySet());
    }

    private Cache createTwoLevelCache(String cacheName , ) {

    }

}
