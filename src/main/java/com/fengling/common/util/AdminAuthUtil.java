package com.fengling.common.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengling.common.constant.CommonConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.context.UserContext;
import com.fengling.common.exception.BusinessException;
import com.fengling.entity.UserInfo;
import com.fengling.entity.dto.AdminInfoDto;
import com.fengling.entity.dto.UserInfoDto;
import com.fengling.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminAuthUtil {

    private final UserMapper userMapper;

    /**
     * 管理员信息验证
     *
     * @return 管理员基础信息
     */
    public AdminInfoDto adminAuth() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED);
        }
        Integer userRole = UserContext.getUserRole();
        if (!CommonConstants.USER_ROLE_ADMIN.equals(userRole)) {
            throw new BusinessException(ResultCodeEnum.FORBIDDEN);
        }

        UserInfo userInfo = userMapper.selectOne(
                new LambdaQueryWrapper<UserInfo>()
                        .select(UserInfo::getNickName)
                        .eq(UserInfo::getId, userId)
        );
        if (userInfo == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "用户信息不存在");
        }
        return new AdminInfoDto(userId, userInfo.getNickName(), userRole);
    }
}
