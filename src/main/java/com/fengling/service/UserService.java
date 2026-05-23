package com.fengling.service;

import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.*;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 查询个人主页信息
     *
     * @return 个人信息响应实体
     */
    CommonResult<UserInfoMineRespDto> getMineUserInfo();

    /**
     * 更新个人信息
     *
     * @param userInfoMineReqDto 用户信息实体
     * @return 无
     */
    CommonResult<Void> updateUserInfo(UserInfoMineReqDto userInfoMineReqDto);

    /**
     * 上传用户头像
     *
     * @param file     上传的头像文件
     * @param imageUrl 头像链接
     * @return 图像链接
     */
    CommonResult<UserUploadPhotoRespDto> uploadUserPhoto(MultipartFile file, String imageUrl);

    /**
     * 用户管理列表查询
     *
     * @param pageReqDto 分页请求参数
     * @return 用户管理列表
     */
    CommonResult<PageRespDto<AdminUserManageListRespDto>> listUserManage(PageReqDto pageReqDto);
}
