package com.fengling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengling.common.constant.CacheConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.exception.BusinessException;
import com.fengling.common.resp.CommonResult;
import com.fengling.common.util.JWTUtil;
import com.fengling.common.util.RedisUtil;
import com.fengling.entity.UserInfo;
import com.fengling.entity.dto.UserInfoDto;
import com.fengling.entity.dto.UserLoginReqDto;
import com.fengling.entity.dto.UserAuthRespDto;
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
    private final JWTUtil jwtUtil;
    private final RedisUtil redisUtil;

    @Override
    public CommonResult<UserAuthRespDto> register(UserRegisterReqDto userRegisterReqDto) {
        // 判断用户是否存在
        UserInfo existUser = userMapper.selectOne(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getUsername, userRegisterReqDto.getUsername())
        );
        if (existUser != null) {
            throw new BusinessException(ResultCodeEnum.USERNAME_EXIST);
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(userRegisterReqDto.getUsername());
        userInfo.setPassword(passwordEncoder.encode(userRegisterReqDto.getPassword()));
        userInfo.setNickName(generateNickname());
        int insert = userMapper.insert(userInfo);
        if (insert != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "注册失败");
        }
        UserInfo queryUserInfo = userMapper.selectOne(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getUsername, userRegisterReqDto.getUsername())
        );
        //生成JWT
        String jwtToken = jwtUtil.createJwtToken(queryUserInfo.getId(), queryUserInfo.getUserRole());
        //将JWT放到Redis
        String key = CacheConstants.AUTH_TOKEN + queryUserInfo.getId();
        redisUtil.addRedisCache(key, jwtToken, jwtUtil.getTtl());
        UserAuthRespDto authRespDto = new UserAuthRespDto();
        authRespDto.setId(queryUserInfo.getId());
        authRespDto.setUserStatus(queryUserInfo.getUserStatus());
        authRespDto.setUserRole(queryUserInfo.getUserRole());
        authRespDto.setToken(jwtToken);
        return CommonResult.success(authRespDto);
    }

    @Override
    public CommonResult<UserAuthRespDto> login(UserLoginReqDto userLoginReqDto) {
        UserInfo userInfo = userMapper.selectOne(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getUsername, userLoginReqDto.getUsername())
        );
        // 用户不存在
        if (userInfo == null) {
            throw new BusinessException(ResultCodeEnum.USER_NOT_EXIST);
        }
        // 密码不正确
        if (!passwordEncoder.matches(userLoginReqDto.getPassword(), userInfo.getPassword())) {
            throw new BusinessException(ResultCodeEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        // 生成JWT
        String jwtToken = jwtUtil.createJwtToken(userInfo.getId(), userInfo.getUserRole());
        // 将JWT加入Redis
        String key = CacheConstants.AUTH_TOKEN + userInfo.getId();
        redisUtil.addRedisCache(key, jwtToken, jwtUtil.getTtl());
        UserAuthRespDto userAuthRespDto = new UserAuthRespDto(userInfo.getId(),
                userInfo.getUserStatus(), userInfo.getUserRole(),jwtToken);
        return CommonResult.success(userAuthRespDto);
    }

    /**
     * 生成随机的用户昵称
     *
     * @return reader_123456
     */
    private String generateNickname() {
        int random = ThreadLocalRandom.current().nextInt(100000, 999999);
        return "reader_" + random;
    }
}
