package com.fengling.common.util;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
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

    public Map<Object, Object> getHashEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public Set<String> scanKeys(String pattern, long count) {
        return redisTemplate.execute(
                (RedisCallback<Set<String>>) connection -> {
                    Set<String> keys = new HashSet<>();
                    ScanOptions options = ScanOptions.scanOptions()
                            .match(pattern)
                            .count(count)
                            .build();

                    try (Cursor<byte[]> cursor = connection.keyCommands().scan(options)) {
                        while (cursor.hasNext()) {
                            keys.add(new String(cursor.next(), StandardCharsets.UTF_8));
                        }
                    }

                    return keys;
                }
        );
    }

    /**
     * zset 获取脏数据并移除
     *
     * @param key      键
     * @param maxScore 当前时间戳
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
}
