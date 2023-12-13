package com.courage.cache.service.test.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

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
    public void whenEntryIdle_thenEviction() throws InterruptedException {
        CacheLoader<String, String> loader;
        loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                System.out.println("load ....");
                return key.toUpperCase();
            }
        };

        LoadingCache<String, String> cache;
        cache = CacheBuilder.newBuilder().expireAfterAccess(2, TimeUnit.MILLISECONDS).build(loader);

        cache.getUnchecked("hello");
        assertEquals(1, cache.size());

        cache.getUnchecked("hello");
        Thread.sleep(300);

        cache.getUnchecked("test");
        assertEquals(1, cache.size());
        assertNull(cache.getIfPresent("hello"));
    }

    @Test
    public void testRefresh() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CacheLoader<String, String> cacheLoader = new CacheLoader<String, String>() {
            //自动写缓存数据的方法
            @Override
            public String load(String key) {
                System.out.println(Thread.currentThread().getName() + " 加载 key:" + key);
                return "value_" + key.toUpperCase();
            }

            @Override
            //异步刷新缓存
            public ListenableFuture<String> reload(String key, String oldValue) throws Exception {
                System.out.println(Thread.currentThread().getName() + " 重新加载 key:" + key + " oldValue=" + oldValue);
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

        String key = "hello";
        // 第一次加载
        String value = cache.get(key);
        System.out.println(value);
        Thread.sleep(2000);

        for(int i= 0 ;i <10 ;i ++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String value2 = cache.get(key);
                        System.out.println(Thread.currentThread().getName()  + value2);
                        // 第二次加载
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        Thread.sleep(2000);

    }

}
