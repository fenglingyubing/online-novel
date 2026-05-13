package com.fengling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengling.common.constant.CacheConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.exception.BusinessException;
import com.fengling.common.resp.CommonResult;
import com.fengling.common.util.JWTUtil;
import com.fengling.common.util.RedisUtil;
import com.fengling.common.util.RegisterUtil;
import com.fengling.entity.AuthorInfo;
import com.fengling.entity.UserInfo;
import com.fengling.entity.dto.AuthorReqDto;
import com.fengling.entity.dto.UserAuthRespDto;
import com.fengling.mapper.AuthorMapper;
import com.fengling.mapper.UserMapper;
import com.fengling.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorMapper authorMapper;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RegisterUtil registerUtil;
    private final JWTUtil jwtUtil;
    private final RedisUtil redisUtil;

    @Transactional
    @Override
    public CommonResult<UserAuthRespDto> authorRegister(AuthorReqDto authorReqDto) {
        //判断用户名是否存在
        UserInfo one = registerUtil.getUserInfoByUserName(authorReqDto.getUsername());
        if (one != null) {
            throw new BusinessException(ResultCodeEnum.USERNAME_EXIST);
        }
        if (authorReqDto.getUsername() != null && authorReqDto.getPassword() != null){
            UserInfo registerUser = new UserInfo();
            registerUser.setUsername(authorReqDto.getUsername());
            //加密密码
            registerUser.setPassword(passwordEncoder.encode(authorReqDto.getPassword()));
            int insert = userMapper.insert(registerUser);
            if (insert != 1) {
                throw new BusinessException(ResultCodeEnum.FAIL, "注册失败");
            }
        }

        UserInfo userInfo = registerUtil.getUserInfoByUserName(authorReqDto.getUsername());
        int author = authorMapper.insert(new AuthorInfo(authorReqDto.getAuthorName(),
                userInfo.getId()));
        if(author != 1){
            throw new BusinessException(ResultCodeEnum.FAIL, "注册失败");
        }
        // 生成JWT令牌
        String jwtToken = jwtUtil.createJwtToken(userInfo.getId(), userInfo.getUserRole());
        // 将JWT令牌放到Redis
        String key = CacheConstants.AUTH_TOKEN + userInfo.getId();
        redisUtil.addRedisCache(key, jwtToken, jwtUtil.getTtl());
        UserAuthRespDto userAuthRespDto = new UserAuthRespDto(
                userInfo.getId(),
                userInfo.getUserStatus(),
                userInfo.getUserRole(),
                jwtToken
        );
        return CommonResult.success(userAuthRespDto);
    }
}
