package com.courage.cache.service.disruptor;

import com.lmax.disruptor.WorkHandler;

import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

public class QueueConsumer<T> implements WorkHandler<DataEvent<T>> {

    private static final Integer DEFAULT_CONSUMER_SIZE = Runtime.getRuntime().availableProcessors() << 1;

    @Override
    public void onEvent(DataEvent<T> dataEvent) throws Exception {
        if (Objects.nonNull(dataEvent)) {
        }
    }

    public static void main(String[] args) {
        System.out.println(DEFAULT_CONSUMER_SIZE);
    }

}
