package com.courage.cache.service.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

/**
 * Created by zhangyong on 2023/9/7.
 */
public class LongEventLambdasMain {

    public static void main(String[] args) throws Exception{
        // 1. 指定环形缓冲区的大小，必须是2的幂次方。
        // 2. 构建 Disruptor。
        // 3. 连接处理程序。
        // 4. 启动 Disruptor，启动所有线程运行。
        // 5. 从 Disruptor 获取用于发布的环形缓冲区。
        int bufferSize = 1024;
        Disruptor<LongEvent> disruptor =
                new Disruptor<>(LongEvent::new, bufferSize, DaemonThreadFactory.INSTANCE);
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
