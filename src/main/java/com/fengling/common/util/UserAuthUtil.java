package com.fengling.common.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengling.common.constant.CommonConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.context.AuthUserInfo;
import com.fengling.common.context.UserContext;
import com.fengling.common.exception.BusinessException;
import com.fengling.entity.UserInfo;
import com.fengling.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAuthUtil {

    private final UserMapper userMapper;

    public AuthUserInfo userAuth() {
        AuthUserInfo authUserInfo = UserContext.getAuthUserInfo();
        if (authUserInfo == null) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED);
        }

        if (authUserInfo.getUserId() == null || authUserInfo.getUserRole() == null) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED);
        }

        UserInfo userInfo = userMapper.selectOne(
                new LambdaQueryWrapper<UserInfo>()
                        .select(UserInfo::getId)
                        .eq(UserInfo::getId, authUserInfo.getUserId())
        );

        if (userInfo == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "用户信息不存在");
        }

        return new AuthUserInfo(authUserInfo.getUserId(), authUserInfo.getUserRole());
    }
}
