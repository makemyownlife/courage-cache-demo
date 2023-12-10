package com.courage.cache.service.commonspool;

import java.util.UUID;

public class MyObject {

    private String uid = UUID.randomUUID().toString();

    private volatile boolean valid = true;

    public void initialize() {
        System.out.println("初始化对象" + uid + " thread:" + Thread.currentThread().getName());
        valid = true;
    }

    public void destroy() {
        System.out.println("销毁对象" + uid + " thread:" + Thread.currentThread().getName());
        valid = false;
    }

    // ========================================================================    get method ========================================================================
    public boolean isValid() {
        return valid;
    }

    public String getUid() {
        return uid;
    }

}
