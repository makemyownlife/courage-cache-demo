package com.courage.cache.service.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DisruptorManage<T> {

    private static final Integer DEFAULT_CONSUMER_SIZE = Runtime.getRuntime().availableProcessors() << 1;

    public static final Integer DEFAULT_SIZE = 4096 << 1 << 1;

    private DisruptorProducer<T> producer;

    private int ringBufferSize;

    private int consumerSize;

    public DisruptorManage() {
        this(DEFAULT_CONSUMER_SIZE, DEFAULT_SIZE);
    }

    public DisruptorManage(final int consumerSize, final int ringBufferSize) {
        this.ringBufferSize = ringBufferSize;
        this.consumerSize = consumerSize;
    }

    public void start() {
        ThreadPoolExecutor consumerExecutor = new ThreadPoolExecutor(consumerSize, consumerSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), DisruptorThreadFactory.create("disruptor_consumer", false), new ThreadPoolExecutor.AbortPolicy());
        EventFactory<DataEvent<T>> eventFactory = new DisruptorEventFactory<>();
        Disruptor<DataEvent<T>> disruptor = new Disruptor<>(eventFactory, ringBufferSize, DisruptorThreadFactory.create("disruptor_provider", false), ProducerType.MULTI, new BlockingWaitStrategy());
        QueueConsumer<T>[] consumers = new QueueConsumer[consumerSize];
        for (int i = 0; i < consumerSize; i++) {
            consumers[i] = new QueueConsumer<>(consumerExecutor);
        }
        disruptor.handleEventsWithWorkerPool(consumers);
        disruptor.start();
        RingBuffer<DataEvent<T>> ringBuffer = disruptor.getRingBuffer();
        this.producer = new DisruptorProducer<>(ringBuffer, disruptor);
    }

    public DisruptorProducer getProducer() {
        return this.producer;
    }

}
