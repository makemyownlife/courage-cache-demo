package com.courage.cache.service.cache;

import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

/**
 * Created by zhangyong on 2021/12/5.
 */
public class MultiLevelCache implements Cache {

    public MultiLevelCache() {


    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Object getNativeCache() {
        return null;
    }

    @Override
    public ValueWrapper get(Object key) {
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {

    }

    @Override
    public void evict(Object key) {

    }

    @Override
    public void clear() {

    }

}
