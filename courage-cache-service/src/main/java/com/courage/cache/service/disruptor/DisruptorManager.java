package com.courage.cache.service.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class DisruptorManager<T> {

    private static final Integer DEFAULT_CONSUMER_SIZE = Runtime.getRuntime().availableProcessors() << 1;

    public static final Integer DEFAULT_SIZE = 4096 << 1 << 1;

    private ConsumerListener<T> consumerListener;

    private DisruptorProducer<T> producer;

    private int ringBufferSize;

    private int consumerSize;

    public DisruptorManager(ConsumerListener<T> consumerListener) {
        this(consumerListener, DEFAULT_CONSUMER_SIZE, DEFAULT_SIZE);
    }

    public DisruptorManager(ConsumerListener<T> consumerListener, final int consumerSize, final int ringBufferSize) {
        this.consumerListener = consumerListener;
        this.ringBufferSize = ringBufferSize;
        this.consumerSize = consumerSize;
    }

    public void start() {
        EventFactory<DataEvent<T>> eventFactory = new DisruptorEventFactory<>();
        Disruptor<DataEvent<T>> disruptor = new Disruptor<>(
                eventFactory,
                ringBufferSize,
                DisruptorThreadFactory.create("consumer-thread", false),
                ProducerType.MULTI,
                new BlockingWaitStrategy()
        );
        DisruptorConsumer<T>[] consumers = new DisruptorConsumer[consumerSize];
        for (int i = 0; i < consumerSize; i++) {
            consumers[i] = new DisruptorConsumer<>(consumerListener);
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
