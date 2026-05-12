package com.fengling.common.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserInfo {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户角色
     */
    private Integer userRole;
}
