package com.fengling.service;


import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.AuthorHomeRespDto;
import com.fengling.entity.dto.AuthorReqDto;
import com.fengling.entity.dto.UserAuthRespDto;

public interface AuthorService {

    /**
     * 作家注册接口
     * @param authorReqDto 作家注册请求实体
     * @return 用户认证响应响应结果
     */
    CommonResult<UserAuthRespDto> authorRegister(AuthorReqDto authorReqDto);

    CommonResult<AuthorHomeRespDto> getAuthorHomeInfo();
}
