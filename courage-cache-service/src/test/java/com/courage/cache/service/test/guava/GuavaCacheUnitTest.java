package com.courage.cache.service.test.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class GuavaCacheUnitTest {

    private final static Logger logger = LoggerFactory.getLogger(GuavaCacheUnitTest.class);

    @Test
    public void testHandCache() throws InterruptedException {
        // 测试手工测试
        Cache<String, String> cache = CacheBuilder.newBuilder().
                // 最大容量为20（基于容量进行回收）
                        maximumSize(20)
                // 配置写入后多久使缓存过期
                .expireAfterWrite(10, TimeUnit.SECONDS).build();
        cache.put("hello", "value_HELLO");
        assertEquals("value_HELLO", cache.getIfPresent("hello"));
        Thread.sleep(10000);
        assertNull(cache.getIfPresent("hello"));
    }

    @Test
    public void testLoadingCache() throws InterruptedException, ExecutionException {
        CacheLoader<String, String> cacheLoader = new CacheLoader<String, String>() {
            //自动写缓存数据的方法
            @Override
            public String load(String key) {
                System.out.println("加载 key:" + key);

                return "value_" + key.toUpperCase();
            }

            @Override
            //异步刷新缓存
            public ListenableFuture<String> reload(String key, String oldValue) throws Exception {
                return super.reload(key, oldValue);
            }

        };

        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                // 最大容量为20（基于容量进行回收）
                .maximumSize(20)
                // 配置写入后多久使缓存过期
                .expireAfterWrite(10, TimeUnit.SECONDS)
                //配置写入后多久刷新缓存
                .refreshAfterWrite(1, TimeUnit.SECONDS).build(cacheLoader);

        assertEquals(0, cache.size());
        assertEquals("value_HELLO", cache.getUnchecked("hello"));
        assertEquals(1, cache.size());

        Thread.sleep(11000);
        assertEquals("value_HELLO", cache.getUnchecked("hello"));

        // 通过 Callable 获取
        String key = "mykey";
        String value = cache.get(key, new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "call_" + key;
            }
        });
        System.out.println("call value:" + value);
    }

    @Test
    public void testRefresh() throws InterruptedException, ExecutionException {
        CacheLoader<String, String> cacheLoader = new CacheLoader<String, String>() {
            //自动写缓存数据的方法
            @Override
            public String load(String key) {
                System.out.println(Thread.currentThread().getName() + "加载 key:" + key);

                return "value_" + key.toUpperCase();
            }

            @Override
            //异步刷新缓存
            public ListenableFuture<String> reload(String key, String oldValue) throws Exception {
                System.out.println(Thread.currentThread().getName() + "重新加载 key:" + key + "旧值:" + oldValue);
                return super.reload(key, oldValue);
            }

        };

        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                // 最大容量为20（基于容量进行回收）
                .maximumSize(20)
                // 配置写入后多久使缓存过期
                .expireAfterWrite(10, TimeUnit.SECONDS)
                //配置写入后多久刷新缓存
                .refreshAfterWrite(1, TimeUnit.SECONDS).build(cacheLoader);

        String helloValue = cache.get("hello");
        System.out.println(helloValue);
        Thread.sleep(4000);
        helloValue = cache.getIfPresent("hello");
        System.out.println(helloValue);
    }



}
