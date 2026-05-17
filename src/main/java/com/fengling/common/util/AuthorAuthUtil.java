package com.fengling.common.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengling.common.constant.CommonConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.context.UserContext;
import com.fengling.common.exception.BusinessException;
import com.fengling.entity.AuthorInfo;
import com.fengling.entity.dto.UserInfoDto;
import com.fengling.mapper.AuthorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorAuthUtil {

    private final AuthorMapper authorMapper;

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

    /**
     * 验证当前登录用户作者身份，并获取作者id
     *
     * @return 当前登录作者id
     */
    public Long getCurrentAuthorId() {
        UserInfoDto userInfoDto = authorAuth();
        AuthorInfo authorInfo = authorMapper.selectOne(
                new LambdaQueryWrapper<AuthorInfo>()
                        .select(AuthorInfo::getId)
                        .eq(AuthorInfo::getUserId, userInfoDto.getId())
        );
        if (authorInfo == null) {
            throw new BusinessException(ResultCodeEnum.FORBIDDEN);
        }
        return authorInfo.getId();
    }
}
