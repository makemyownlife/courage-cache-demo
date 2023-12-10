package com.courage.cache.service.disruptor;

public interface DataEventListener<T> {

    void processDataEvent(DataEvent<T> dataEvent) throws InterruptedException;

}

