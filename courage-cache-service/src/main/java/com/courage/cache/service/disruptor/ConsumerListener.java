package com.courage.cache.service.disruptor;

public interface ConsumerListener<T> {

    void processDataEvent(DataEvent<T> dataEvent);

}

