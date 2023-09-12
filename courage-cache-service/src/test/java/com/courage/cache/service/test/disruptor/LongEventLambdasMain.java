package com.courage.cache.service.test.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

public class LongEventLambdasMain {

    public static void main(String[] args) throws Exception{
        int bufferSize = 1024;
        Disruptor<LongEvent> disruptor =
                new Disruptor<>(
                        new LongEventFactory(),
                        bufferSize,
                        DaemonThreadFactory.INSTANCE,
                        ProducerType.SINGLE,
                        new BlockingWaitStrategy());
        disruptor.handleEventsWith((event, sequence, endOfBatch) ->
                System.out.println("currentThread:" + Thread.currentThread().getName() + " Event: " + event));
        disruptor.start();

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; true; l++) {
            bb.putLong(0, l);
            ringBuffer.publishEvent((event, sequence, buffer) -> event.set(buffer.getLong(0)), bb);
            Thread.sleep(1000);
        }
    }

}