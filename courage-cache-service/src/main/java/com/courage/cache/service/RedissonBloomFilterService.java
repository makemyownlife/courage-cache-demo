package com.courage.cache.service;

import org.redisson.api.RBitSet;
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
        bloomFilter = redissonClient.getBloomFilter("myBloomFilter");
        bloomFilter.tryInit(500, 0.003);
        for (int i = 0; i < 500; i++) {
            bloomFilter.add(Long.valueOf(i));
        }
        bloomFilter.add(1L);
        bloomFilter.add(2L);
        bloomFilter.add(3L);
        bloomFilter.add(4L);

        RBitSet bitSet = redissonClient.getBitSet("mybitSet");
        bitSet.set(3);
        bitSet.set(5);
        bitSet.set(6);
        bitSet.set(8);
    }

    public boolean mightcontain(Long id) {
        return bloomFilter.contains(id);
    }

    public static void main(String[] args) {
        String m = "";
        System.out.println(m.length());
    }

}
