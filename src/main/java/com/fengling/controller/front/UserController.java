package com.fengling.controller.front;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.UserInfoMineRespDto;
import com.fengling.entity.dto.UserLoginReqDto;
import com.fengling.entity.dto.UserAuthRespDto;
import com.fengling.entity.dto.UserRegisterReqDto;
import com.fengling.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPathConstants.USER)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 用户注册接口
     *
     * @param userRegisterReqDto 注册请求实体
     * @return 用户认证响应实体
     */
    @PostMapping("/register")
    public CommonResult<UserAuthRespDto> userRegister(@RequestBody UserRegisterReqDto userRegisterReqDto) {
        return userService.register(userRegisterReqDto);
    }

    /**
     * 用户登录接口
     *
     * @param userLoginReqDto 登录请求实体
     * @return 用户认证响应实体
     */
    @PostMapping("/login")
    public CommonResult<UserAuthRespDto> userLogin(@RequestBody UserLoginReqDto userLoginReqDto) {
        return userService.login(userLoginReqDto);
    }

    /**
     * 退出登录
     *
     * @return 无
     */
    @DeleteMapping("/logout")
    public CommonResult<Void> userLoginOut() {
        return userService.userLoginOut();
    }

    @GetMapping("/mine")
    public CommonResult<UserInfoMineRespDto> getMineUserInfo(){
        return userService.getMineUserInfo();
    }
}
