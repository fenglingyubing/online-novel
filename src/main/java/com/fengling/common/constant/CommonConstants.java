package com.fengling.common.constant;

public class CommonConstants {
    /**
     * 用户状态
     */
    public static final Integer USER_STATUS_NORMAL = 0;

    /**
     * 用户角色-作者
     */
    public static final Integer USER_ROLE_AUTHOR = 2;

    /**
     * 用户角色-读者
     */
    public static final Integer USER_ROLE_READER = 1;

    /**
     * 用户余额
     */
    public static final Integer USER_DEFAULT_BALANCE = 0;

    /**
     * 用户头像路径名
     */
    public static final String USER_PATH_NAME = "userPhoto";

    /**
     * 图片后缀-jpg
     */
    public static final String IMAGE_JPG = ".jpg";

    /**
     * 图片后缀-png
     */
    public static final String IMAGE_PNG = ".png";

    /**
     * 图片后缀-jpeg
     */
    public static final String IMAGE_JPEG = ".jpeg";

    /**
     * 图片后缀-webp
     */
    public static final String IMAGE_WEBP = ".webp";

    /**
     * 图片链接前缀-http://
     */
    public static final String IMAGE_PREFIX_HTTP = "http://";

    /**
     * 图片链接前缀-https://
     */
    public static final String IMAGE_PREFIX_HTTPS = "https://";

    /**
     * 首页最新上架小说的数量
     */
    public static final Integer NOVEL_RECENT_LIMIT = 4;

    /**
     * 章节状态：草稿
     */
    public static final Integer CHAPTER_STATUS_DRAFTS = 0;

    /**
     * 章节状态：审核中
     */
    public static final Integer CHAPTER_STATUS_AUDIT = 3;

    /**
     * 章节状态：已发布
     */
    public static final Integer CHAPTER_STATUS_RELEASE = 1;
}
