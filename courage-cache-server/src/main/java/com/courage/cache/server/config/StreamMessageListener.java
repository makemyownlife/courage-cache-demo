package com.courage.cache.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

@Component
public class StreamMessageListener implements StreamListener<String, MapRecord<String, String, String>> {

    private final static Logger logger = LoggerFactory.getLogger(StreamMessageListener.class);

    @Override
    public void onMessage(MapRecord<String, String, String> entries) {
        logger.info("message id " + entries.getId());
        logger.info("stream " + entries.getStream());
        logger.info("body " + entries.getValue());
    }
}