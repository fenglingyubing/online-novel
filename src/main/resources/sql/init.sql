-- 用户表
CREATE TABLE `user_info`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户id',
    `username`     VARCHAR(20)  NOT NULL COMMENT '用户名（登录名）',
    `password`     VARCHAR(255) NOT NULL COMMENT 'BCrypt加密后的密码',
    `user_sex`     TINYINT               DEFAULT NULL COMMENT '性别（0-男，1-女）',
    `nick_name`    VARCHAR(32)           DEFAULT NULL COMMENT '昵称',
    `user_role`    TINYINT      NOT NULL DEFAULT 1 COMMENT '用户角色（0-管理员，1-读者，2-作家）',
    `user_photo`   VARCHAR(255)          DEFAULT NULL COMMENT '用户头像（存OSS链接）',
    `user_balance` INT          NOT NULL DEFAULT 0 COMMENT '用户书币余额',
    `user_status`  TINYINT      NOT NULL DEFAULT 0 COMMENT '用户状态（0-正常，1-禁用）',
    `create_time`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 小说分类表
CREATE TABLE `book_category`
(
    `id`            smallint unsigned NOT NULL AUTO_INCREMENT COMMENT '小说分类ID',
    `parent_id`     smallint unsigned NOT NULL DEFAULT 0 COMMENT '父分类ID，0表示一级分类',
    `category_name` varchar(50) NOT NULL COMMENT '小说分类名称',
    `sort`          smallint unsigned NOT NULL DEFAULT 0 COMMENT '排序值，越小越靠前',
    `status`        tinyint unsigned NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1启用',
    `create_time`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_book_category_parent_name` (`parent_id`, `category_name`),
    KEY             `idx_book_category_parent_status_sort` (`parent_id`, `status`, `sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='小说分类表';

-- 小说详情表
CREATE TABLE book_info
(
    id                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '小说ID',
    book_name           VARCHAR(255) NOT NULL COMMENT '小说名称',
    cover_url           VARCHAR(500)          DEFAULT NULL COMMENT '小说封面',
    author_id           BIGINT UNSIGNED NOT NULL COMMENT '作者ID',
    category_id         SMALLINT UNSIGNED NOT NULL COMMENT '小说分类ID',
    publish_status      TINYINT      NOT NULL DEFAULT 1 COMMENT '发布状态：0-下架，1-上架',
    update_status       TINYINT      NOT NULL DEFAULT 0 COMMENT '更新状态：0-连载中，1-已完结',
    book_intro          TEXT COMMENT '小说简介',
    chapter_count       INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '小说章节数',
    word_count          INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '总字数',
    latest_chapter_id   BIGINT UNSIGNED DEFAULT NULL COMMENT '最新章节ID',
    latest_chapter_name VARCHAR(255)          DEFAULT NULL COMMENT '最新章节名称',
    last_chapter_time   DATETIME              DEFAULT NULL COMMENT '最新章节更新时间',
    create_time         DATETIME     NOT NULL COMMENT '创建时间',
    update_time         DATETIME     NOT NULL COMMENT '修改时间',
    PRIMARY KEY (id),
    KEY                 idx_author_id (author_id),
    KEY                 idx_category_id (category_id),
    KEY                 idx_category_publish_update_time (category_id, publish_status, update_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='小说信息表';

-- 小说章节表
CREATE TABLE book_chapter
(
    id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '小说章节ID',
    book_id         BIGINT UNSIGNED NOT NULL COMMENT '小说ID',
    chapter_num     INT UNSIGNED NOT NULL COMMENT '章节序号',
    chapter_name    VARCHAR(255) NOT NULL COMMENT '小说章节名',
    chapter_content MEDIUMTEXT   NOT NULL COMMENT '小说正文',
    word_count      INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '章节字数',
    chapter_status  TINYINT      NOT NULL DEFAULT 1 COMMENT '章节状态：0-草稿，1-已发布，2-下架，3-审核中',
    publish_time    DATETIME              DEFAULT NULL COMMENT '发布时间',
    audit_remark    VARCHAR(500)          DEFAULT NULL COMMENT '审核备注',
    audit_time      DATETIME              DEFAULT NULL COMMENT '审核时间',
    audit_admin_id  BIGINT                DEFAULT NULL COMMENT '审核人',
    create_time     DATETIME     NOT NULL COMMENT '创建时间',
    update_time     DATETIME     NOT NULL COMMENT '修改时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_book_chapter_num (book_id, chapter_num),
    KEY             idx_book_id (book_id),
    KEY             idx_book_status_num (book_id, chapter_status, chapter_num)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='小说章节表';

-- 作家表
CREATE TABLE author_info
(
    id            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '作者ID',
    user_id       BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    author_name   VARCHAR(255) NOT NULL COMMENT '作者笔名',
    author_intro  TEXT                  DEFAULT NULL COMMENT '作者简介',
    author_status TINYINT      NOT NULL DEFAULT 1 COMMENT '作者状态：0-禁用，1-正常，2-审核中',
    book_count    INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '作品数',
    word_count    INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '累计字数',
    create_time   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_id (user_id),
    UNIQUE KEY uk_author_name (author_name)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='作家表';

-- 书架表
CREATE TABLE book_shelf
(
    id                   BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '书架id',
    user_id              BIGINT   NOT NULL COMMENT '用户id',
    book_id              BIGINT   NOT NULL COMMENT '小说id',
    last_read_chapter_id BIGINT NULL COMMENT '上次阅读章节id',
    last_read_time       DATETIME NULL COMMENT '上次阅读时间',
    create_time          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',

    UNIQUE KEY uk_user_book (user_id, book_id),
    KEY                  idx_user_update_time (user_id, update_time),
    KEY                  idx_book_id (book_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='书架';

-- 小说信息变更审核表
CREATE TABLE `book_info_change`
(
    `id`             BIGINT   NOT NULL AUTO_INCREMENT COMMENT '变更信息id',
    `book_id`        BIGINT            DEFAULT NULL COMMENT '变更小说id',
    `author_id`      BIGINT   NOT NULL COMMENT '作者id',
    `audit_admin_id` BIGINT            DEFAULT NULL COMMENT '审核管理员id',
    `book_name`      VARCHAR(500)      DEFAULT NULL COMMENT '变更小说名称',
    `book_intro`     TEXT              DEFAULT NULL COMMENT '变更小说简介',
    `cover_url`      VARCHAR(255)      DEFAULT NULL COMMENT '变更图片链接',
    `publish_status` TINYINT           DEFAULT NULL COMMENT '变更发布状态（只有从下架到上架需要审核）',
    `category_id`    INT NULL comment '分类id',
    `audit_type`     TINYINT  NOT NULL DEFAULT 1 COMMENT '审核类型（1-新建作品，2-信息变更，3-上架申请）',
    `audit_status`   TINYINT  NOT NULL DEFAULT 0 COMMENT '审核状态：0-待审核，1-已通过，2-已驳回',
    `apply_status`   TINYINT  NOT NULL DEFAULT 0 COMMENT '应用状态：0-未应用，1-已应用',
    `audit_remark`   VARCHAR(500)      DEFAULT NULL COMMENT '审核备注',
    `audit_time`     DATETIME          DEFAULT NULL COMMENT '审核时间',
    `create_time`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY              `idx_book_id` (`book_id`),
    KEY              `idx_author_id` (`author_id`),
    KEY              `idx_audit_status` (`audit_status`),
    KEY              `idx_apply_status` (`apply_status`),
    KEY              `idx_book_audit_status` (`book_id`, `audit_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小说信息变更';

-- 章节审核表
CREATE TABLE `chapter_audit`
(
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '审核id',
    `chapter_id`     BIGINT UNSIGNED NOT NULL COMMENT '章节id',
    `book_id`        BIGINT UNSIGNED NOT NULL COMMENT '小说id',
    `author_id`      BIGINT UNSIGNED NOT NULL COMMENT '作者id',
    `audit_admin_id` BIGINT UNSIGNED          DEFAULT NULL COMMENT '审核管理员id',
    `audit_remark`   VARCHAR(500)             DEFAULT NULL COMMENT '审核备注',
    `audit_time`     DATETIME                 DEFAULT NULL COMMENT '审核时间',
    `audit_status`   TINYINT         NOT NULL DEFAULT 0 COMMENT '审核状态：0-待审核，1-已通过，2-已驳回',
    `apply_status`   TINYINT         NOT NULL DEFAULT 0 COMMENT '应用状态：0-未应用，1-已应用',
    `create_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_chapter_id` (`chapter_id`),
    KEY `idx_book_id` (`book_id`),
    KEY `idx_author_id` (`author_id`),
    KEY `idx_audit_status` (`audit_status`),
    KEY `idx_apply_status` (`apply_status`),
    KEY `idx_author_audit_status` (`author_id`, `audit_status`),
    KEY `idx_chapter_audit_status` (`chapter_id`, `audit_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='章节审核表';

-- 封禁信息表
CREATE TABLE `user_disable_info`
(
    `id`                 BIGINT   NOT NULL AUTO_INCREMENT COMMENT '封禁id',
    `user_id`            BIGINT   NOT NULL COMMENT '用户id',
    `disable_info`       VARCHAR(255) NOT NULL COMMENT '封禁原因',
    `disable_days`       INT      NOT NULL COMMENT '封禁天数（-1永久封禁）',
    `disable_admin_id`   BIGINT   NOT NULL COMMENT '操作管理员id',
    `disable_remark`     VARCHAR(500) DEFAULT NULL COMMENT '封禁备注',
    `disable_start_time` DATETIME NOT NULL COMMENT '封禁开始时间',
    `disable_end_time`   DATETIME     DEFAULT NULL COMMENT '封禁结束时间',
    `disable_status`     TINYINT  NOT NULL DEFAULT 1 COMMENT '封禁状态（1-封禁中，2-已解封）',
    `enable_time`        DATETIME     DEFAULT NULL COMMENT '解封时间',
    `enable_admin_id`    BIGINT       DEFAULT NULL COMMENT '解封管理员id',
    `create_time`        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_disable_status` (`disable_status`),
    KEY `idx_user_disable_status` (`user_id`, `disable_status`),
    KEY `idx_disable_end_time` (`disable_end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户封禁信息表';
