package com.courage.cache.service;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Guava 实现
 * Created by zhangyong on 2023/4/7.
 */
@Service
public class GuavaBloomFilterService {

    private static Logger logger = LoggerFactory.getLogger(GuavaBloomFilterService.class);

    private static BloomFilter<Long> filter = BloomFilter.create(Funnels.longFunnel(), 10000, 0.001);

    @PostConstruct
    public void addProduct() {
        logger.info("初始化Guava布隆过滤器数据开始");
        filter.put(1L);
        filter.put(2L);
        filter.put(3L);
        filter.put(4L);
        logger.info("初始化Guava布隆过滤器数据结束");
    }

    public boolean mightcontain(Long id) {
        return filter.mightContain(id);
    }

}
