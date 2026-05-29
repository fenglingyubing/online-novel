package com.fengling.controller.author;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.*;
import com.fengling.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 作家首页信息查询
     *
     * @return 作家主页响应结果
     */
    @GetMapping("/home")
    public CommonResult<AuthorHomeRespDto> getAuthorHomeInfo() {
        return authorService.getAuthorHomeInfo();
    }

    /**
     * 作家后台作品管理查询
     *
     * @param pageReqDto 分页请求参数
     * @return 作品列表
     */
    @GetMapping("/novels")
    public CommonResult<PageRespDto<AuthorNovelsListRespDto>> listAuthorNovelsList(PageReqDto pageReqDto) {
        return authorService.listAuthorNovelsList(pageReqDto);
    }

    /**
     * 作家所有草稿查询
     *
     * @param page 分页请求参数
     * @return 草稿列表
     */
    @GetMapping("/drafts")
    public CommonResult<PageRespDto<AuthorDraftsRespDto>> listAuthorDrafts(PageReqDto page) {
        return authorService.listAuthorDrafts(page);
    }

    /**
     * 编辑页小说列表获取
     *
     * @return 小说列表
     */
    @GetMapping("/edit/booklist")
    public CommonResult<List<AuthorEditBookListResp>> listAuthorEditBook() {
        return authorService.listAuthorEditBook();
    }

    /**
     * 审核列表查询
     *
     * @param pageReqDto 分页请求参数
     * @return 审核列表
     */
    @GetMapping("/audit" + ApiPathConstants.LIST)
    public CommonResult<PageRespDto<AuthorAuditListRespDto>> listAudits(PageReqDto pageReqDto) {
        return authorService.listAudits(pageReqDto);
    }

    /**
     * 公告列表查询
     *
     * @param pageReqDto 分页请求参数
     * @return 公告列表
     */
    @GetMapping(ApiPathConstants.LIST + "/announcement")
    public CommonResult<PageRespDto<AnnouncementRespDto>> listAnnouncement(PageReqDto pageReqDto) {
        return authorService.listAnnouncement(pageReqDto);
    }

    /**
     * 公告详情查询
     *
     * @param announcementId 公告id
     * @return 公告详情
     */
    @GetMapping(ApiPathConstants.LIST + "/announcement/{announcementId}")
    public CommonResult<AnnouncementInfoRespDto> getAnnouncementInfo(
            @PathVariable("announcementId") Long announcementId
    ) {
        return authorService.getAnnouncementInfo(announcementId);
    }
}
