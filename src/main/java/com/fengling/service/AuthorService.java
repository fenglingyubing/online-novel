package com.fengling.service;


import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.AuthorReqDto;
import com.fengling.entity.dto.UserAuthRespDto;

public interface AuthorService {
    CommonResult<UserAuthRespDto> authorRegister(AuthorReqDto authorReqDto);
}
