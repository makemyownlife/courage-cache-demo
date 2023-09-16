package com.courage.cache.service.test.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

/**
 * 多个事件处理器
 * Created by zhangyong on 2023/9/7.
 */
public class TwoEventHandlerMain {

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
                    System.out.println("currentThread:" + Thread.currentThread().getName() + " 不可重复消费1 消息=" + longEvent.getValue());
                    Thread.sleep(100);
                    if(longEvent.getValue() == 0) {
                        Thread.sleep(15000);
                    }
                }, (longEvent) -> {
                    System.out.println("currentThread:" + Thread.currentThread().getName() + " 不可重复消费2 消息=" + longEvent.getValue());
                    Thread.sleep(50);
                },
                (longEvent) -> {
                    System.out.println("currentThread:" + Thread.currentThread().getName() + " 不可重复消费3 消息=" + longEvent.getValue());
                });
        disruptor.start();

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; true; l++) {
            bb.putLong(0, l);
            ringBuffer.publishEvent((event, sequence, buffer) -> event.set(buffer.getLong(0)), bb);
            Thread.sleep(10);
        }

    }

}
