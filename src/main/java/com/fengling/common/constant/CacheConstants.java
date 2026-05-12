package com.fengling.common.constant;

public class CacheConstants {
    /**
     * redis远程缓存前缀
     */
    public static final String REDIS_PREFIX = "novel:";
    /**
     * jwt令牌缓存前缀
     */
    public static final String AUTH_TOKEN = REDIS_PREFIX + "jwt:";
}
