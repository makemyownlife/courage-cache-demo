package com.courage.cache.service.test.disruptor;

import com.lmax.disruptor.EventHandler;

public class AnotherEventHandler implements EventHandler<LongEvent> {

    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("AnotherEventHandler currentThread:" + Thread.currentThread().getName() + " Event: " + event);
    }

}
