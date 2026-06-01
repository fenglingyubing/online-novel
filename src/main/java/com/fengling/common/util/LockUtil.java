package com.fengling.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class LockUtil {

    private final StringRedisTemplate redisTemplate;

    public boolean tryLock(String key, String requestId, Long ttl) {
        if (ttl == null || ttl <= 0) {
            return false;
        }

        Boolean isSuccess = redisTemplate.opsForValue().setIfAbsent(key, requestId, ttl, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(isSuccess);
    }

    public boolean unlock(String key, String requestId) {
        String script = """
                    if redis.call('get', KEYS[1]) == ARGV[1] then
                        return redis.call('del', KEYS[1])
                    else 
                        return 0
                    end
                """;
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(script);
        redisScript.setResultType(Long.class);
        Long res = redisTemplate.execute(
                redisScript,
                Collections.singletonList(key),
                requestId
        );
        return Long.valueOf(1).equals(res);
    }
}
