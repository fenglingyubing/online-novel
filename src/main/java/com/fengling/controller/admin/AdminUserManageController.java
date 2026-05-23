package com.fengling.controller.admin;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.AdminUserManageListReqDto;
import com.fengling.entity.dto.AdminUserManageListRespDto;
import com.fengling.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPathConstants.USER_MANAGE)
@RequiredArgsConstructor
public class AdminUserManageController {

    private final UserService userService;

    /**
     * 用户管理列表查询
     *
     * @param reqDto 用户管理列表请求参数
     * @return 用户管理列表
     */
    @GetMapping(ApiPathConstants.LIST)
    public CommonResult<PageRespDto<AdminUserManageListRespDto>> listUserManage(
            AdminUserManageListReqDto reqDto
    ) {
        return userService.listUserManage(reqDto);
    }

    /**
     * 用户状态更新
     *
     * @param userId     用户id
     * @param userStatus 用户状态
     * @return 无
     */
    @PutMapping("/update/status/{userId}/{userStatus}")
    public CommonResult<Void> updateUserStatus(
            @PathVariable("userId") Long userId,
            @PathVariable("userStatus") Integer userStatus
    ) {
        return userService.updateUserStatus(userId, userStatus);
    }
}
