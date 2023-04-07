package com.courage.cache.service;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.stereotype.Service;

/**
 * Created by zhangyong on 2023/4/7.
 */
@Service
public class BloomFilterService {

    private static BloomFilter<Long> BLOOM_FILTER = BloomFilter.create(
            Funnels.longFunnel(),
            500,
            0.01);




}
