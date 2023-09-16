package com.courage.cache.service.test.disruptor;

import java.nio.ByteBuffer;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

public class EventWorkPoolMain {

    public static void main(String[] args) throws Exception {
        int bufferSize = 2;
        Disruptor<LongEvent> disruptor =
                new Disruptor<>(
                        new LongEventFactory(),
                        bufferSize,
                        DaemonThreadFactory.INSTANCE,
                        ProducerType.SINGLE,
                        new BlockingWaitStrategy());
        // disruptor.handleEventsWith(new LongEventHandler(), new AnotherEventHandler());
        //初始化三个共同消费者
        EventHandlerGroup<LongEvent> orderEventEventHandlerGroup = disruptor.handleEventsWithWorkerPool((longEvent) -> {
                    System.out.println("不可重复消费1 消息=" + longEvent.getValue());
                }, (longEvent) -> {
                    System.out.println("不可重复消费2 消息=" + longEvent.getValue());
                },
                (longEvent) -> {
                    System.out.println("不可重复消费3 消息=" + longEvent.getValue());
                });
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
