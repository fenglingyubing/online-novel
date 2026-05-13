package com.fengling.controller.auhtor;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.AuthorReqDto;
import com.fengling.entity.dto.UserAuthRespDto;
import com.fengling.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPathConstants.AUTHOR)
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping("/register")
    public CommonResult<UserAuthRespDto> authorRegister(@RequestBody AuthorReqDto authorReqDto){
        return authorService.authorRegister(authorReqDto);
    }
}
