package com.qian.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Set;

public class TestRedis {
    private static Logger logger = LoggerFactory.getLogger(TestRedis.class);

    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost");
        String aValue = jedis.get("new_a");
        logger.info("aValue:{}",aValue);
        Set<String> keys = jedis.keys("*");
        logger.info("keys:{}", keys);
    }
}
