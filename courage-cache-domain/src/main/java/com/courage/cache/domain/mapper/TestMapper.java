package com.courage.cache.domain.mapper;

import com.courage.cache.domain.po.TestPo;
import org.springframework.stereotype.Repository;

@Repository
public interface TestMapper {

    TestPo getTestById(Long id);

}
