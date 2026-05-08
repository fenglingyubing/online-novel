package com.fengling.controller.front;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.UserInfoDto;
import com.fengling.entity.dto.UserRegisterReqDto;
import com.fengling.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPathConstants.USER)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 用户注册接口
     * @param userRegisterReqDto
     * @return
     */
    @PostMapping("/register")
    public CommonResult<UserInfoDto> userRegister(@RequestBody UserRegisterReqDto userRegisterReqDto){
        return userService.register(userRegisterReqDto);
    }
}
