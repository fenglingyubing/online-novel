package com.fengling.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.common.constant.CacheConstants;
import com.fengling.common.constant.CommonConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.context.UserContext;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.exception.BusinessException;
import com.fengling.common.resp.CommonResult;
import com.fengling.common.util.*;
import com.fengling.entity.UserInfo;
import com.fengling.entity.dto.*;
import com.fengling.mapper.UserMapper;
import com.fengling.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Locale;
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
    private final OSSUtil ossUtil;
    private final AdminAuthUtil adminAuthUtil;
    private final PageAuthUtil pageAuthUtil;

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
        userInfo.setNickName(registerUtil.generateNickname());
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

    @Override
    public CommonResult<UserUploadPhotoRespDto> uploadUserPhoto(MultipartFile file, String imageUrl) {
        boolean hasFile = file != null && !file.isEmpty();
        boolean hasImageUrl = imageUrl != null && !imageUrl.isBlank();
        // 判断是不是两个都没传或者两个都传了
        if (hasFile == hasImageUrl) {
            throw new BusinessException(ResultCodeEnum.FAIL, "请选择一种头像上传方式");
        }
        Long userId = UserContext.getUserId();
        UserInfo oldUserInfo = userMapper.selectOne(
                new LambdaQueryWrapper<UserInfo>()
                        .select(UserInfo::getUserPhoto)
                        .eq(UserInfo::getId, userId)
        );
        String oldUserPhoto = oldUserInfo == null ? null : oldUserInfo.getUserPhoto();
        log.info("旧头像链接: {}",oldUserPhoto);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        String userPhoto;
        if (hasFile) {
            userPhoto = ossUtil.upload(file, CommonConstants.USER_PATH_NAME);
        } else {
            userPhoto = imageUrl.trim();
            String lower = userPhoto.toLowerCase(Locale.ROOT);
            // 校验图片链接格式是否正确
            if (
                    !lower.startsWith(CommonConstants.IMAGE_PREFIX_HTTP) &&
                    !lower.startsWith(CommonConstants.IMAGE_PREFIX_HTTPS)
            ) {
                throw new BusinessException(ResultCodeEnum.FAIL, "图片链接格式错误");
            }
        }
        userInfo.setUserPhoto(userPhoto);
        int i = userMapper.updateById(userInfo);
        if (i != 1) {
            // 数据库更新失败并且用的是文件上传
            if (hasFile) {
                ossUtil.delete(userPhoto);
            }
            throw new BusinessException(ResultCodeEnum.FAIL, "头像更新失败");
        }
        if (oldUserPhoto != null && !oldUserPhoto.isBlank() && !oldUserPhoto.equals(userPhoto)) {
            log.info("开始删除旧头像");
            ossUtil.delete(oldUserPhoto);
            log.info("删除成功");
        }
        return CommonResult.success(new UserUploadPhotoRespDto(userPhoto));
    }

    @Override
    public CommonResult<PageRespDto<AdminUserManageListRespDto>> listUserManage(PageReqDto pageReqDto) {
        adminAuthUtil.adminAuth();
        pageAuthUtil.pageAuth(pageReqDto);

        Page<AdminUserManageListRespDto> page = new Page<>(
                pageReqDto.getPageNum(),
                pageReqDto.getPageSize()
        );
        Page<AdminUserManageListRespDto> pageUserManage = userMapper.listUserManage(page);
        return CommonResult.success(PageRespDto.of(pageUserManage));
    }
}
