package com.fengling.common.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.*;
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

    public void addRedisCacheHash(String key, String hashKey, String value, long ttl) {
        redisTemplate.opsForHash().put(key, hashKey, value);
        redisTemplate.expire(key, ttl, TimeUnit.MILLISECONDS);
    }

    public String getHashValue(String key, String hashKey) {
        Object value = redisTemplate.opsForHash().get(key, hashKey);
        return value == null ? null : value.toString();
    }

    public List<String> getHashValues(String key, List<String> hashKeys) {
        List<Object> objects = redisTemplate.opsForHash().multiGet(key, new ArrayList<>(hashKeys));
        if (objects == null) {
            return List.of();
        }
        return objects.stream()
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .toList();
    }

    public Map<Object, Object> getHashEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * zset 获取脏数据并移除
     *
     * @param key      键
     * @param maxScore 当前时间戳往前10秒钟
     * @param count    数量
     * @return 脏数据列表
     */
    @SuppressWarnings("rawtypes")
    public List<String> popZSetByScore(String key, double maxScore, long count) {
        String lua = """
                local values = redis.call('ZRANGE', KEYS[1], '-inf', ARGV[1], 'BYSCORE', 'LIMIT', 0, ARGV[2])
                if #values > 0 then
                    redis.call('ZREM', KEYS[1], unpack(values))
                end
                return values
                """;
        DefaultRedisScript<List> script = new DefaultRedisScript<>(lua, List.class);
        List<?> values = redisTemplate.execute(
                script,
                List.of(key),
                String.valueOf(maxScore),
                String.valueOf(count)
        );

        return Optional.ofNullable(values)
                .orElse(List.of())
                .stream()
                .map(String::valueOf)
                .toList();
    }

    /**
     * zset 添加元素方法
     *
     * @param key      键
     * @param value    值
     * @param maxScore 当前时间戳
     */
    public void addZSet(String key, String value, double maxScore) {
        redisTemplate.opsForZSet().add(key, value, maxScore);
    }

    /**
     * zset 获取长度
     *
     * @param key 键
     * @return 长度
     */
    public Long getZSetSize(String key) {
        Long size = redisTemplate.opsForZSet().zCard(key);
        return size == null ? 0L : size;
    }


    /**
     * 倒序获取zset 中的元素
     *
     * @param key   键
     * @param start 起始坐标
     * @param end   结束坐标
     * @return 元素列表
     */
    public List<String> reverseRangeZSet(String key, Long start, Long end) {
        Set<String> values = redisTemplate.opsForZSet().reverseRange(key, start, end);
        return values == null ? List.of() : new ArrayList<>(values);
    }
}
