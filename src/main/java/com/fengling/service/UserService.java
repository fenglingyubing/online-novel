package com.fengling.service;

import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.*;

public interface UserService {
    /**
     * 用户注册接口
     *
     * @param userRegisterReqDto
     * @return UserInfoDto
     */
    CommonResult<UserAuthRespDto> register(UserRegisterReqDto userRegisterReqDto);

    /**
     * 用户登录接口
     *
     * @param userLoginReqDto
     * @return UserInfoDto
     */
    CommonResult<UserAuthRespDto> login(UserLoginReqDto userLoginReqDto);

    /**
     * 退出登录
     *
     * @return 无
     */
    CommonResult<Void> userLoginOut();

    CommonResult<UserInfoMineRespDto> getMineUserInfo();
}
