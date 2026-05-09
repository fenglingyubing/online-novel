package com.fengling.service;

import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.UserInfoDto;
import com.fengling.entity.dto.UserLoginDto;
import com.fengling.entity.dto.UserRegisterReqDto;

public interface UserService {
    /**
     * 用户注册接口
     * @param userRegisterReqDto
     * @return UserInfoDto
     */
    CommonResult<UserInfoDto> register(UserRegisterReqDto userRegisterReqDto);

    /**
     * 用户登录接口
     * @param userLoginDto
     * @return
     */
    CommonResult<UserInfoDto> login(UserLoginDto userLoginDto);
}
