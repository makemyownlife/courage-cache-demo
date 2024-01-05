package com.courage.cache.domain.mapper;

import com.courage.cache.domain.po.User;
import org.springframework.stereotype.Repository;


@Repository
public interface UserMapper {

    User getUserById(Long id);

}
