package com.courage.cache.service.test.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * Created by zhangyong on 2023/9/7.
 */
public class LongEventFactory implements EventFactory<LongEvent> {

    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }

}
