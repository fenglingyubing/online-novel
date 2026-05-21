package com.fengling.common.constant;

public class CommonConstants {
    private CommonConstants() {
        /* This utility class should not be instantiated */
    }

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
     * 用户角色-管理员
     */
    public static final Integer USER_ROLE_ADMIN = 0;

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

    /**
     * 作品审核状态:待审核
     */
    public static final Integer AUDIT_STATUS_AUDIT = 0;

    /**
     * 作品发布状态：上架
     */
    public static final Integer PUBLISH_STATUS_SHELVES = 1;

    /**
     * 作品发布状态：下架
     */
    public static final Integer PUBLISH_STATUS_UNDERCARRIAGE = 0;

    /**
     * 作品更新状态：连载中
     */
    public static final Integer UPDATE_STATUS_SERIALIZED = 0;

    /**
     * 作品更新状态：已完结
     */
    public static final Integer UPDATE_STATUS_CLOSED = 1;

    /**
     * 审核类型：信息变更
     */
    public static final Integer AUDIT_TYPE_INFORMATION_CHANGE = 2;

    /**
     * 审核类型：新建作品
     */
    public static final Integer AUDIT_TYPE_CREATE_WORK = 1;

    /**
     * 章节审核类型：待审核
     */
    public static final Integer CHAPTER_AUDIT_STATUS_AUDIT = 0;

    /**
     * 章节审核类型：已通过
     */
    public static final Integer CHAPTER_AUDIT_STATUS_PASS = 1;

    /**
     * 章节审核类型：已驳回
     */
    public static final Integer CHAPTER_AUDIT_STATUS_REJECTED = 2;

    /**
     * 信息变更审核状态：待审核
     */
    public static final Integer INFO_CHANGE_AUDIT = 0;

    /**
     * 信息变更审核状态：已通过
     */
    public static final Integer INFO_CHANGE_PASS = 1;

    /**
     * 信息变更审核状态：已驳回
     */
    public static final Integer INFO_CHANGE_REJECTED = 2;

    /**
     * 应用状态：已应用
     */
    public static final Integer APPLY_STATUS_APPLY = 1;

    /**
     * 应用状态：未应用
     */
    public static final Integer APPLY_STATUS_NOT_APPLY = 0;
}
