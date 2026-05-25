package com.fengling.controller.admin;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.AdminUserChangeStatusReqDto;
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
     * 用户状态更新：封禁
     *
     * @param userId 用户id
     * @return 无
     */
    @PutMapping("/update/status/{userId}/disable")
    public CommonResult<Void> updateUserStatusDisable(
            @PathVariable("userId") Long userId,
            @RequestBody AdminUserChangeStatusReqDto userChangeStatusReqDto
    ) {
        return userService.updateUserStatusDisable(userId, userChangeStatusReqDto);
    }

    /**
     * 用户状态更新：解封
     *
     * @param userId 用户id
     * @return 无
     */
    @PutMapping("/update/status/{userId}/enable")
    public CommonResult<Void> updateUserStatusEnable(
            @PathVariable("userId") Long userId
    ) {
        return userService.updateUserStatusEnable(userId);
    }
}
