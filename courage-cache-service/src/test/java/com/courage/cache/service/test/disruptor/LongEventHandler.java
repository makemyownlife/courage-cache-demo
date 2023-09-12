package com.courage.cache.service.test.disruptor;


import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;

/**
 * Created by zhangyong on 2023/9/7.
 */
public class LongEventHandler implements EventHandler<LongEvent> {
    /**
     * Called when a publisher has published an event to the {@link RingBuffer}
     *
     * @param longEvent  published to the {@link RingBuffer}
     * @param sequence   of the event being processed
     * @param endOfBatch flag to indicate if this is the last event in a batch from the {@link RingBuffer}
     * @throws Exception if the EventHandler would like the exception handled further up the chain.
     */
    @Override
    public void onEvent(LongEvent longEvent, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("currentThread:" + Thread.currentThread().getName() + "Event: " + longEvent);
    }

}
