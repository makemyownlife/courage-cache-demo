package com.courage.cache.service.test.readwritelock;

public class ReadWriteLockTest {

    public static void main(String[] args) throws InterruptedException {
        ReadWriteLockCache readWriteLockCache = new ReadWriteLockCache();
        Thread writeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                readWriteLockCache.put("hello", "mylife");
            }
        });
        writeThread.setName("writeThread");
        writeThread.start();
        Thread.sleep(400);
        Thread readThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String value = readWriteLockCache.get("hello");
                System.out.println(value);
            }
        });
        readThread.setName("readThread");
        readThread.start();
        Thread.sleep(10000);
    }

}
