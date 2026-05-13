package com.fengling.common.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengling.entity.UserInfo;
import com.fengling.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterUtil {

    private final UserMapper userMapper;

    public UserInfo getUserInfoByUserName(String username){
        return userMapper.selectOne(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getUsername, username));
    }
}
