package com.courage.cache.service;

import com.courage.cache.domain.mapper.TestMapper;
import com.courage.cache.domain.po.TestPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Autowired
    private TestMapper TestMapper;

}
