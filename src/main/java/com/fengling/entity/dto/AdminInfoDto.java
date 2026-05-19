package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理员基础信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminInfoDto {

    /**
     * 管理员id
     */
    private Long id;

    /**
     * 管理员昵称
     */
    private String nickName;

    /**
     * 角色
     */
    private Integer userRole;
}
