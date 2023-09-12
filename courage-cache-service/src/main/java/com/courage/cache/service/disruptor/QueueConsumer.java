package com.courage.cache.service.disruptor;

import com.lmax.disruptor.WorkHandler;

import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

public class QueueConsumer<T> implements WorkHandler<DataEvent<T>> {
    
    @Override
    public void onEvent(DataEvent<T> dataEvent) throws Exception {
        if (Objects.nonNull(dataEvent)) {
        }
    }

}
