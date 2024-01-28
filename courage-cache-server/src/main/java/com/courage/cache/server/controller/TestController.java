package com.courage.cache.server.controller;

import com.courage.cache.common.result.ResponseEntity;
import com.courage.cache.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Api(tags = "测试接口")
@RestController
@RequestMapping("/hello")
@Slf4j
public class TestController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/test")
    @ApiOperation("test")
    public ResponseEntity test() {
        return ResponseEntity.successResult(userService.getUserById(1L));
    }

    @GetMapping("/multiget")
    @ApiOperation("multiget")
    public ResponseEntity multget() {
        // 分别设置key：a ,b , c 三个key 的值
        redisTemplate.opsForValue().set("a", "nihao");
        redisTemplate.opsForValue().set("b", "张勇");
        redisTemplate.opsForValue().set("c", "高慧");
        // 批量查询 定义 List 对象用来存储 需要查询key的集合
        List<String> keys = new ArrayList<String>(4);
        keys.add("a");
        keys.add("b");
        keys.add("d");  //
        keys.add("c");
        List<String> result = redisTemplate.opsForValue().multiGet(keys);
        return ResponseEntity.successResult(result);
    }

}
