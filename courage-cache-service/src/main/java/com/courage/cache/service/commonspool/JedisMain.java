package com.courage.cache.service.commonspool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisMain {

    public static void main(String[] args) throws Exception{
        // 创建连接池配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(100);
        config.setMaxIdle(20);

        // 创建连接池
        JedisPool pool = new JedisPool(config, "localhost", 6379);

        // 获取连接
        Jedis jedis = pool.getResource();

        jedis.set("hello" , "张勇");

        // 使用连接
        String value = jedis.get("hello");

        System.out.println(value);

        // 归还连接
        jedis.close();

        // 关闭连接池
       // pool.close();

        Thread.sleep(5000);
    }

}
