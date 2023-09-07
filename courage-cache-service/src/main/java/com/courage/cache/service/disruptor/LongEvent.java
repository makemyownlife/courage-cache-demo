package com.courage.cache.service.disruptor;

/**
 * 日志事件
 * Created by zhangyong on 2023/9/7.
 */
public class LongEvent {

    private long value;

    public void set(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LongEvent{" + "value=" + value + '}';
    }

}
