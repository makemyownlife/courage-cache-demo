package com.courage.cache.service;

import com.courage.cache.domain.mapper.UserMapper;
import com.courage.cache.domain.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserMapper userMapper;

    public User getUserById(Long id) {
        return userMapper.getUserById(id);
    }

}
