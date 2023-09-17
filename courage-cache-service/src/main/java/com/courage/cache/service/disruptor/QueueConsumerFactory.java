package com.courage.cache.service.disruptor;

public interface QueueConsumerFactory<T> {

    void processDataEvent(DataEvent<T> dataEvent);

}

