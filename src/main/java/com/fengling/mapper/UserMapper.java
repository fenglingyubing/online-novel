package com.fengling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.entity.UserInfo;
import com.fengling.entity.dto.AdminUserManageListRespDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<UserInfo> {

    /**
     * 管理员后台：用户管理列表查询
     *
     * @param page       分页请求参数
     * @param userRole   用户角色
     * @param userStatus 用户状态
     * @return 用户管理列表
     */
    Page<AdminUserManageListRespDto> listUserManage(Page<AdminUserManageListRespDto> page,
                                                    @Param("userRole") Integer userRole,
                                                    @Param("userStatus") Integer userStatus);
}
