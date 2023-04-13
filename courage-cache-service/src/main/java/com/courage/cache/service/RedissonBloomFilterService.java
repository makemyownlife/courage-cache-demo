package com.courage.cache.service;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class RedissonBloomFilterService {

    private static Logger logger = LoggerFactory.getLogger(RedissonBloomFilterService.class);

    @Autowired
    private RedissonClient redissonClient;

    private RBloomFilter<Long> bloomFilter;

    @PostConstruct
    public void addProduct() {
        bloomFilter = redissonClient.getBloomFilter("bloomFilter");
        bloomFilter.tryInit(500, 0.003);
        bloomFilter.add(1L);
        bloomFilter.add(2L);
        bloomFilter.add(3L);
        bloomFilter.add(4L);
    }

    public boolean mightcontain(Long id) {
        return bloomFilter.contains(id);
    }

}
