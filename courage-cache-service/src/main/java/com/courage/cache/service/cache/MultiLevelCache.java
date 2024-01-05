package com.courage.cache.service.cache;

import org.redisson.spring.cache.RedissonCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

/**
 * Created by zhangyong on 2021/12/5.
 */
public class MultiLevelCache implements Cache {

    private final static Logger logger = LoggerFactory.getLogger(MultiLevelCache.class);

    private String cacheName;

    private com.github.benmanes.caffeine.cache.Cache localCache;

    private RedissonCache redissonCache;

    public MultiLevelCache(String cacheName, com.github.benmanes.caffeine.cache.Cache localCache, RedissonCache redissonCache) {
        this.cacheName = cacheName;
        this.localCache = localCache;
        this.redissonCache = redissonCache;
    }

    @Override
    public String getName() {
        return this.cacheName;
    }

    @Override
    public Object getNativeCache() {
        return this.redissonCache.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        return null;
    }

    public Object getRawResult(Object key) {
        logger.info("从一级缓存查询key:" + key);
        Object result = localCache.getIfPresent(key);
        if (result != null) {
            return result;
        }
        logger.info("从二级缓存查询key:" + key);
        result = redissonCache.getNativeCache().get(key);
        if (result != null) {
            localCache.put(key, result);
        }
        return result;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        Object rawResult = getRawResult(key);
        if (rawResult != null && type.isInstance(rawResult)) {
            return type.cast(rawResult);
        }
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        try {
            Object result = getRawResult(key);
            if (result != null) {
                return (T) result;
            }
            T loadedValue = valueLoader.call();
            // Put the loaded value into the local cache
            localCache.put(key, loadedValue);
            // Optionally, put the loaded value into the secondary cache (redissonCache)
            return loadedValue;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void put(Object key, Object value) {
        logger.info("写入一级缓存 key:" + key);
        localCache.put(key, value);
        logger.info("写入二级缓存 key:" + key);
        redissonCache.put(key, value);
    }

    @Override
    public void evict(Object key) {
        localCache.invalidate(key);
        redissonCache.getNativeCache().remove(key);
    }

    @Override
    public void clear() {

    }

}
