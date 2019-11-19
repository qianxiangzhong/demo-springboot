package com.qian.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.*;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class TestRedis {
    private static Logger logger = LoggerFactory.getLogger(TestRedis.class);

    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost");

//        jedis.publish("dbs", "mongodb3");
//        jedis.subscribe((new JedisPubSub() {
//            @Override
//            public void onMessage(String channel, String message) {
//                logger.info("received message! channel:{}, message:{}", channel, message);
//            }
//
//            @Override
//            public void onSubscribe(String channel, int subscribedChannels) {
//                logger.info("subscribe! channel:{}, subscribedChannels:{}", channel, subscribedChannels);
//            }
//
//            @Override
//            public void onUnsubscribe(String channel, int subscribedChannels) {
//                logger.info("unscribe message! channel:{}, subscribedChannels:{}", channel, subscribedChannels);
//            }
//        }), "dbs");
//        Transaction tx = jedis.multi();
//        for (int i = 0; i < 10; i++) {
//            tx.set("t" + i, "t" + i);
//        }
//        List<Response<?>> resp = tx.execGetResponse();
//        logger.info("resp:{}", resp);
        jedis.watch("a");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        JedisPoolConfig jedisPoolConfig = getJedisPoolConfig();
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1");
        Jedis jedis = jedisPool.getResource();
        int i = 0;
        long start = System.currentTimeMillis();
        while (true) {
            long end = System.currentTimeMillis();
            if (end - start >= 1000) {
                break;
            }
            i++;
            jedis.lpush("list", i + "");
        }
        List<String> list = jedis.lrange("list", 0, 100000);
        logger.info("list size:{}", list.size());
        logger.info("list:{}", list);
    }

    @Test
    public void test2() {
        RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration().sentinel("127.0.0.1", 6379);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisSentinelConfiguration);
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        Human human1 = new Human();
        human1.setName("Qian");
        human1.setGender("Male");
        human1.setHeight(175f);
        human1.setWeight(75d);
        redisTemplate.opsForValue().set("human1", human1);
        Human human2 = (Human) redisTemplate.opsForValue().get("human1");
        logger.info("human2:{}", human2);
        logger.info("human2:{}", human2);
    }

    public JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(50);
        jedisPoolConfig.setMaxTotal(100);
        jedisPoolConfig.setMaxWaitMillis(20000);
        return jedisPoolConfig;
    }
}

class Human implements Serializable {
    private static final long serialVersionUID = -616360109842419836L;
    private String name;
    private String gender;
    private double height;
    private double weight;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Human{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                '}';
    }
}