package com.courage.cache.service.test.readwritelock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockCache {

    // 创建一个 HashMap 来存储缓存的数据
    private Map<String, String> map = new HashMap<>();

    // 创建读写锁对象
    private ReadWriteLock rw = new ReentrantReadWriteLock();

    // 放对象方法：向缓存中添加一个键值对
    public void put(String key, String value) {
        // 获取写锁，以确保当前操作是独占的
        rw.writeLock().lock();
        try {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 执行写操作，将键值对放入 map
            map.put(key, value);
        } finally {
            // 释放写锁
            rw.writeLock().unlock();
        }
    }

    // 取对象方法：从缓存中获取一个值
    public String get(String key) {
        // 获取读锁，允许并发读操作
        rw.readLock().lock();
        try {
            // 执行读操作，从 map 中获取值
            return map.get(key);
        } finally {
            // 释放读锁
            rw.readLock().unlock();
        }
    }
}
