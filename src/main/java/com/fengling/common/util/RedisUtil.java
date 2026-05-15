package com.fengling.common.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    private final StringRedisTemplate redisTemplate;

    public RedisUtil(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public void addRedisCache(String key, String value, long ttl){
        redisTemplate.opsForValue().set(key, value, ttl, TimeUnit.MILLISECONDS);
    }

    public String getValueForKey(String Key){
        return redisTemplate.opsForValue().get(Key);
    }

    public void deleteKey(String key){
        redisTemplate.delete(key);
    }
}
