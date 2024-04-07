package com.courage.cache.server.controller;

import com.courage.cache.common.result.ResponseEntity;
import com.courage.cache.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.Record;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.*;

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

    @GetMapping("/hmget")
    @ApiOperation("hmget")
    public ResponseEntity hmget() {
        String key = "myhashkey";
        // 分别设置key：myhashkey ，a ,b , c 三个field 的值
        redisTemplate.opsForHash().put(key, "a", "nihao");
        redisTemplate.opsForHash().put(key, "b", "张勇");
        redisTemplate.opsForHash().put(key, "c", "高慧");
        // 批量查询 定义 List 对象用来存储 需要查询key的集合
        List<Object> fields = new ArrayList<Object>(4);
        fields.add("a");
        fields.add("b");
        fields.add("d");      // field ：d 不存在
        fields.add("c");
        List<Object> result = redisTemplate.opsForHash().multiGet(key, fields);
        return ResponseEntity.successResult(result);
    }


    @GetMapping("/pipeline")
    @ApiOperation("pipeline")
    public ResponseEntity pipeline() {

        // 分别设置key：a ,b , c 三个key 的值
        redisTemplate.opsForValue().set("a", "string米好");
        redisTemplate.opsForValue().set("b", "张勇string");
        redisTemplate.opsForValue().set("c", "高慧string");

        String key = "myhashkey";
        // 分别设置key：myhashkey ，a ,b , c 三个field 的值
        redisTemplate.opsForHash().put(key, "a", "hash你好");
        redisTemplate.opsForHash().put(key, "b", "hash张勇");
        redisTemplate.opsForHash().put(key, "c", "hash高慧");

        // 此时 results 包含了每个命令的执行结果
        List<Object> results = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            // 字符串：尝试获取 a,b,d,c 这四个key对应的值
            connection.get("a".getBytes());
            connection.get("b".getBytes());
            connection.get("d".getBytes());
            connection.get("c".getBytes());

            // 哈希表 hashkey 为myhashkey ， a,b,d,c 这四个Field对应的值
            String hashkey = "myhashkey";
            connection.hGet(hashkey.getBytes() , "a".getBytes());
            connection.hGet(hashkey.getBytes() , "b".getBytes());
            connection.hGet(hashkey.getBytes() , "d".getBytes());
            connection.hGet(hashkey.getBytes() , "c".getBytes());

            return null;
        });

        return ResponseEntity.successResult(results);
    }

    @GetMapping("/lua")
    @ApiOperation("lua")
    public ResponseEntity lua() throws UnsupportedEncodingException {

        // 分别设置key：a ,b , c 三个key 的值
        redisTemplate.opsForValue().set("a", "string米好");
        redisTemplate.opsForValue().set("b", "张勇string");
        redisTemplate.opsForValue().set("c", "高慧string");

        String key = "myhashkey";
        // 分别设置key：myhashkey ，a ,b , c 三个field 的值
        redisTemplate.opsForHash().put(key, "a", "hash你好");
        redisTemplate.opsForHash().put(key, "b", "hash张勇");
        redisTemplate.opsForHash().put(key, "c", "hash高慧");


        String luaScript =
                "local result = {}\n" +
                "\n" +
                "-- 获取字符串键的值\n" +
                "result[1] = redis.call('GET', 'a')\n" +
                "result[2] = redis.call('GET', 'b')\n" +
                "result[3] = redis.call('GET', 'd')\n" +
                "result[4] = redis.call('GET', 'c')\n" +
                "\n" +
                "-- 获取哈希表字段的值\n" +
                "local hashKey = 'myhashkey'\n" +
                "result[5] = redis.call('HGET', hashKey, 'a')\n" +
                "result[6] = redis.call('HGET', hashKey, 'b')\n" +
                "result[7] = redis.call('HGET', hashKey, 'd')\n" +
                "result[8] = redis.call('HGET', hashKey, 'c')\n" +
                "\n" +
                "return result\n" ;

        List<Object> results = redisTemplate.execute((RedisCallback<List<Object>>) connection ->
                connection.eval(luaScript.getBytes(), ReturnType.fromJavaType(List.class), 0)
        );

        for (Object result : results) {
            if(result != null) {
                String stringValue = new String((byte[]) result, "UTF-8");
                System.out.println(stringValue);
            }
        }

        return ResponseEntity.successResult(null);
    }

    @GetMapping("/stream")
    @ApiOperation("stream")
    public ResponseEntity stream() {
        Map<String ,String> map = new HashMap<>();
        map.put("name" , "zhangyogn");
        Record record = StreamRecords.newRecord().ofMap(map).withStreamKey("mystream").withId(RecordId.autoGenerate());

        RecordId recordId = redisTemplate.opsForStream()
                .add(record);
        return  ResponseEntity.successResult(recordId.getValue());
    }

}
