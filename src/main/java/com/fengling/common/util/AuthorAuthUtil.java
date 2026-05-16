package com.fengling.common.util;

import com.fengling.common.constant.CommonConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.context.UserContext;
import com.fengling.common.exception.BusinessException;
import com.fengling.entity.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorAuthUtil {
    /**
     * 作者认证基础信息
     *
     * @return UserInfoDto 用户基础信息
     */
    public UserInfoDto authorAuth() {
        // 从JWT获取用户信息
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED);
        }
        Integer userRole = UserContext.getUserRole();
        if (!CommonConstants.USER_ROLE_AUTHOR.equals(userRole)) {
            throw new BusinessException(ResultCodeEnum.FORBIDDEN);
        }

        UserInfoDto userInfo = new UserInfoDto();
        userInfo.setId(userId);
        userInfo.setUserRole(userRole);
        return userInfo;
    }
}
