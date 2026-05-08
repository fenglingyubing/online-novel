
-- 用户表
CREATE TABLE `user_info` (
                             `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户id',
                             `username` VARCHAR(20) NOT NULL COMMENT '用户名（登录名）',
                             `password` VARCHAR(255) NOT NULL COMMENT '密码-加密',
                             `user_sex` TINYINT DEFAULT NULL COMMENT '性别（0-男，1-女）',
                             `nick_name` VARCHAR(32) DEFAULT NULL COMMENT '昵称',
                             `user_role` TINYINT NOT NULL DEFAULT 1 COMMENT '用户角色（0-管理员，1-读者，2-作家）',
                             `user_photo` VARCHAR(255) DEFAULT NULL COMMENT '用户头像（存OSS链接）',
                             `user_balance` INT NOT NULL DEFAULT 0 COMMENT '用户书币余额',
                             `user_status` TINYINT NOT NULL DEFAULT 0 COMMENT '用户状态（0-正常，1-禁用）',
                             `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
