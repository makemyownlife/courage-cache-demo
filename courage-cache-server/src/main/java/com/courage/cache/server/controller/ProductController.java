package com.courage.cache.server.controller;

import com.courage.cache.common.result.ResponseEntity;
import com.courage.cache.service.GuavaBloomFilterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "商品接口")
@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {

    @Autowired
    private GuavaBloomFilterService guavaBloomFilterService;

    @GetMapping("/test")
    @ApiOperation("test")
    public ResponseEntity test() {
        return ResponseEntity.successResult("mylife");
    }

    @GetMapping("/maycontain")
    @ApiOperation("maycontain")
    public ResponseEntity maycontain(Long id) {
        boolean result = guavaBloomFilterService.maycontain(id);
        return ResponseEntity.successResult(result);
    }

}
