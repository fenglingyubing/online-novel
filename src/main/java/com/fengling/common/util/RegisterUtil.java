package com.fengling.common.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengling.entity.UserInfo;
import com.fengling.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class RegisterUtil {

    private final UserMapper userMapper;

    public UserInfo getUserInfoByUserName(String username) {
        return userMapper.selectOne(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getUsername, username));
    }

    /**
     * 生成随机的用户昵称
     *
     * @return reader_123456
     */
    public String generateNickname() {
        int random = ThreadLocalRandom.current().nextInt(100000, 999999);
        return "reader_" + random;
    }
}
