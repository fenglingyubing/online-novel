package com.fengling.common.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    private final StringRedisTemplate redisTemplate;

    public RedisUtil(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addRedisCache(String key, String value, long ttl) {
        redisTemplate.opsForValue().set(key, value, ttl, TimeUnit.MILLISECONDS);
    }

    public String getValueForKey(String Key) {
        return redisTemplate.opsForValue().get(Key);
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    public void addRedisCacheHash(String key,String hashKey, String value, long ttl) {
        redisTemplate.opsForHash().put(key, hashKey, value);
        redisTemplate.expire(key, ttl, TimeUnit.MILLISECONDS);
    }

    public String getHashValue(String key, String hashKey) {
        Object value = redisTemplate.opsForHash().get(key, hashKey);
        return value == null ? null : value.toString();
    }

    public Map<Object, Object> getHashEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }
}
