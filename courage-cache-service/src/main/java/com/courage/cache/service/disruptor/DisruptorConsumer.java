package com.courage.cache.service.disruptor;

import com.lmax.disruptor.WorkHandler;

public class DisruptorConsumer<T> implements WorkHandler<DataEvent<T>> {

    private DataEventListener<T> dataEventListener;

    public DisruptorConsumer(DataEventListener dataEventListener) {
        this.dataEventListener = dataEventListener;
    }

    @Override
    public void onEvent(DataEvent<T> dataEvent) throws Exception {
        if (dataEvent != null) {
            dataEventListener.processDataEvent(dataEvent);
        }
    }

}
