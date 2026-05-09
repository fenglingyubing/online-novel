package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户状态
     */
    private Integer status;
    /**
     * 用户角色
     */
    private Integer userRole;
}
