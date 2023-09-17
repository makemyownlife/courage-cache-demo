package com.courage.cache.server.controller;

import com.courage.cache.common.result.ResponseEntity;
import com.courage.cache.service.disruptor.DisruptorProducer;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/disruptor")
public class DisruptorController {

    @Autowired
    private DisruptorProducer<String> producer;

    @GetMapping("/pushlog")
    public ResponseEntity pushlog(String log) {
        producer.onData(log);
        return ResponseEntity.successResult(null);
    }

}
