package com.fengling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.entity.UserInfo;
import com.fengling.entity.dto.AdminUserManageListRespDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserInfo> {

    /**
     * 管理员后台：用户管理列表查询
     *
     * @param page 分页请求参数
     * @return 用户管理列表
     */
    Page<AdminUserManageListRespDto> listUserManage(Page<AdminUserManageListRespDto> page);
}
