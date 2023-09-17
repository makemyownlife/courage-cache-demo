package com.courage.cache.service.disruptor;

import com.lmax.disruptor.WorkHandler;

public class DisruptorConsumer<T> implements WorkHandler<DataEvent<T>> {

    private ConsumerListener<T> consumerListener;

    public DisruptorConsumer(ConsumerListener consumerListener) {
        this.consumerListener = consumerListener;
    }

    @Override
    public void onEvent(DataEvent<T> dataEvent) throws Exception {
        if (dataEvent != null) {
            consumerListener.processDataEvent(dataEvent);
        }
    }

}
