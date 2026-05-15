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

    /**
     * 首页书籍分类
     */
    public static final String CATEGORY = REDIS_PREFIX + "category";

    /**
     * 书架分类缓存时间
     */
    public static final Long CATEGORY_TTL = 1000 * 60 * 60 * 24L;

    /**
     * 最新上架小说缓存
     */
    public static final String NOVEL_RECENT = REDIS_PREFIX + "recent";

    /**
     * 最新上架小说缓存时间
     */
    public static final Long NOVEL_RECENT_TTL = 1000 * 60 * 60 * 24L;
}
