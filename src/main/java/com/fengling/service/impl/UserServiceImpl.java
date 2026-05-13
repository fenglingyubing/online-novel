package com.fengling.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengling.common.constant.CacheConstants;
import com.fengling.common.constant.CommonConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.context.UserContext;
import com.fengling.common.exception.BusinessException;
import com.fengling.common.resp.CommonResult;
import com.fengling.common.util.JWTUtil;
import com.fengling.common.util.RedisUtil;
import com.fengling.common.util.RegisterUtil;
import com.fengling.entity.UserInfo;
import com.fengling.entity.dto.*;
import com.fengling.mapper.UserMapper;
import com.fengling.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final RegisterUtil registerUtil;

    @Override
    public CommonResult<UserAuthRespDto> register(UserRegisterReqDto userRegisterReqDto) {
        if (userRegisterReqDto == null) {
            throw new BusinessException(ResultCodeEnum.FAIL);
        }

        String username = userRegisterReqDto.getUsername();
        String password = userRegisterReqDto.getPassword();

        if (username == null || username.isBlank()) {
            throw new BusinessException(ResultCodeEnum.FAIL, "用户名为空");
        }
        if (password == null || password.isBlank()) {
            throw new BusinessException(ResultCodeEnum.FAIL, "密码为空");
        }
        // 去除空格
        username = username.trim();
        // 判断用户是否存在
        UserInfo existUser = registerUtil.getUserInfoByUserName(username);
        if (existUser != null) {
            throw new BusinessException(ResultCodeEnum.USERNAME_EXIST);
        }
        UserInfo userInfo = new UserInfo();

        userInfo.setUsername(username);
        userInfo.setPassword(passwordEncoder.encode(password));
        userInfo.setNickName(generateNickname());
        userInfo.setUserRole(CommonConstants.USER_ROLE_READER);
        userInfo.setUserStatus(CommonConstants.USER_STATUS_NORMAL);
        userInfo.setUserBalance(CommonConstants.USER_DEFAULT_BALANCE);
        int insert = userMapper.insert(userInfo);
        if (insert != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "注册失败");
        }
        //生成JWT
        Long userId = userInfo.getId();
        Integer userRole = userInfo.getUserRole();
        String jwtToken = jwtUtil.createJwtToken(userId, userRole);
        //将JWT放到Redis
        String key = CacheConstants.AUTH_TOKEN + userId;
        redisUtil.addRedisCache(key, jwtToken, jwtUtil.getTtl());
        UserAuthRespDto userAuthRespDto = BeanUtil.copyProperties(
                userInfo,
                UserAuthRespDto.class
        );
        userAuthRespDto.setToken(jwtToken);
        return CommonResult.success(userAuthRespDto);
    }

    @Override
    public CommonResult<UserAuthRespDto> login(UserLoginReqDto userLoginReqDto) {
        if (userLoginReqDto == null) {
            throw new BusinessException(ResultCodeEnum.FAIL);
        }

        String username = userLoginReqDto.getUsername();
        String password = userLoginReqDto.getPassword();
        if (username == null || username.isBlank()) {
            throw new BusinessException(ResultCodeEnum.FAIL, "用户名为空");
        }
        if (password == null || password.isBlank()) {
            throw new BusinessException(ResultCodeEnum.FAIL, "密码为空");
        }
        username = username.trim();
        UserInfo userInfo = registerUtil.getUserInfoByUserName(username);
        // 用户不存在
        if (userInfo == null) {
            throw new BusinessException(ResultCodeEnum.USER_NOT_EXIST);
        }
        // 密码不正确
        if (!passwordEncoder.matches(password, userInfo.getPassword())) {
            throw new BusinessException(ResultCodeEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        if (!CommonConstants.USER_STATUS_NORMAL.equals(userInfo.getUserStatus())) {
            throw new BusinessException(ResultCodeEnum.ACCOUNT_DISABLED);
        }
        // 生成JWT
        String jwtToken = jwtUtil.createJwtToken(userInfo.getId(), userInfo.getUserRole());
        // 将JWT加入Redis
        String key = CacheConstants.AUTH_TOKEN + userInfo.getId();
        redisUtil.addRedisCache(key, jwtToken, jwtUtil.getTtl());
        UserAuthRespDto userAuthRespDto = BeanUtil.copyProperties(
                userInfo,
                UserAuthRespDto.class
        );
        userAuthRespDto.setToken(jwtToken);
        return CommonResult.success(userAuthRespDto);
    }

    @Override
    public CommonResult<Void> userLoginOut() {
        Long userId = UserContext.getUserId();
        if (userId != null) {
            String key = CacheConstants.AUTH_TOKEN + userId;
            String redisToken = redisUtil.getValueForKey(key);
            if (redisToken != null && !redisToken.isBlank()) {
                redisUtil.deleteKey(key);
            }
        }
        return CommonResult.success();
    }

    @Override
    public CommonResult<UserInfoMineRespDto> getMineUserInfo() {
        Long userId = UserContext.getUserId();
        UserInfo userInfo = userMapper.selectById(userId);
        UserInfoMineRespDto userInfoMineRespDto = BeanUtil.copyProperties(userInfo, UserInfoMineRespDto.class);
        return CommonResult.success(userInfoMineRespDto);
    }

    @Override
    public CommonResult<Void> updateUserInfo(UserInfoMineReqDto userInfoMineReqDto) {
        UserInfo userInfo = new UserInfo();
        Long userId = UserContext.getUserId();
        userInfo.setId(userId);
        userInfo.setUpdateTime(LocalDateTime.now());
        BeanUtil.copyProperties(
                userInfoMineReqDto,
                userInfo,
                CopyOptions.create().ignoreNullValue()
        );
        int i = userMapper.updateById(userInfo);
        if (i != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "更新失败");
        }
        return CommonResult.success();
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
