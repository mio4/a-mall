package com.example.redisdemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisDemoApplicationTests {

    @Autowired
//    private RedisTemplate<String,String> redisTemplate;
    private StringRedisTemplate redisTemplate;

    @Test
    public void contextLoads() {
        redisTemplate.opsForValue().set("test","hello world");

        String test = redisTemplate.opsForValue().get("test");
        System.out.println(test);

        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps("user:1234");
        ops.put("name","biu");
        ops.put("age","22");
        System.out.println(ops.get("name"));

        Map<Object, Object> entries = ops.entries();
        System.out.println(entries);
    }

}
