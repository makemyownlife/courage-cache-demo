package com.courage.cache.service.disruptor;

import com.lmax.disruptor.WorkHandler;

public class QueueConsumer<T> implements WorkHandler<DataEvent<T>> {

    private QueueConsumerFactory<T> queueConsumerFactory;

    public QueueConsumer(QueueConsumerFactory queueConsumerFactory) {
        this.queueConsumerFactory = queueConsumerFactory;
    }

    @Override
    public void onEvent(DataEvent<T> dataEvent) throws Exception {
        if (dataEvent != null) {
            queueConsumerFactory.processDataEvent(dataEvent);
        }
    }

}
