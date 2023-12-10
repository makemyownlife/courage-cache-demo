package com.courage.cache.service.commonspool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyObjectPoolMain {

    public static void main(String[] args) throws InterruptedException {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(20);
        config.setMinIdle(3);
        config.setTestWhileIdle(true);
        config.setMinEvictableIdleTimeMillis(60000L);
        GenericObjectPool<MyObject> pool = new GenericObjectPool<>(new MyObjectFactory(), config);

        ExecutorService executorService = Executors.newFixedThreadPool(30);
        for (int i = 0; i < 100; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    MyObject myObject = null;
                    try {
                        myObject = pool.borrowObject();
                        System.out.println("get对象" + myObject.getUid() +  " thread:" + Thread.currentThread().getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (myObject != null) {
                                pool.returnObject(myObject);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        }

        Thread.sleep(1000000);
    }

}
