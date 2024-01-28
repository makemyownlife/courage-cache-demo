package com.courage.cache.server.controller;

import com.courage.cache.common.result.ResponseEntity;
import com.courage.cache.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试接口")
@RestController
@RequestMapping("/hello")
@Slf4j
public class TestController {

    @Autowired
    private UserService userService;

    @GetMapping("/test")
    @ApiOperation("test")
    public ResponseEntity test() {
        return ResponseEntity.successResult(userService.getUserById(1L));
    }

}
