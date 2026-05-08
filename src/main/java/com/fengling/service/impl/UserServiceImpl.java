package com.fengling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.exception.BusinessException;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.UserInfo;
import com.fengling.entity.dto.UserInfoDto;
import com.fengling.entity.dto.UserRegisterReqDto;
import com.fengling.mapper.UserMapper;
import com.fengling.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CommonResult<UserInfoDto> register(UserRegisterReqDto userRegisterReqDto) {
        // 判断用户是否存在
        UserInfo existUser = userMapper.selectOne(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getUsername, userRegisterReqDto.getUsername())
        );
        if (existUser != null ){
            throw new BusinessException(ResultCodeEnum.USERNAME_EXIST);
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(userRegisterReqDto.getUsername());
        userInfo.setPassword(passwordEncoder.encode(userRegisterReqDto.getPassword()));
        userInfo.setNickName(generateNickname());
        int insert = userMapper.insert(userInfo);
        if (insert != 1){
            throw new BusinessException(ResultCodeEnum.FAIL,"注册失败");
        }
        UserInfo queryUserInfo = userMapper.selectOne(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getUsername, userRegisterReqDto.getUsername())
        );
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setId(queryUserInfo.getId());
        userInfoDto.setStatus(queryUserInfo.getUserStatus());
        return CommonResult.success(userInfoDto);
    }

    /**
     * 生成随机的用户昵称
     * @return reader_123456
     */
    private String generateNickname(){
        int random = ThreadLocalRandom.current().nextInt(100000,999999);
        return "reader_" + random;
    }
}
