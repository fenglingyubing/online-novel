package com.fengling.entity.dto;

import com.fengling.common.dto.PageReqDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户管理列表请求参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserManageListReqDto extends PageReqDto {

    /**
     * 用户角色
     */
    private Integer userRole;

    /**
     * 用户状态
     */
    private Integer userStatus;
}
