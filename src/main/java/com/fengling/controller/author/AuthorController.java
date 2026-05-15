package com.fengling.controller.author;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.AuthorHomeRespDto;
import com.fengling.entity.dto.AuthorReqDto;
import com.fengling.entity.dto.UserAuthRespDto;
import com.fengling.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPathConstants.AUTHOR)
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    /**
     * 作家注册接口
     *
     * @param authorReqDto 作家注册请求实体
     * @return 用户认证响应结果
     */
    @PostMapping("/register")
    public CommonResult<UserAuthRespDto> authorRegister(@RequestBody AuthorReqDto authorReqDto) {
        return authorService.authorRegister(authorReqDto);
    }

    @GetMapping("/home")
    public CommonResult<AuthorHomeRespDto> getAuthorHomeInfo(){
        return authorService.getAuthorHomeInfo();
    }
}
