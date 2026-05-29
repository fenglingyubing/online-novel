package com.fengling.service;


import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.*;

import java.util.List;

public interface AuthorService {

    /**
     * 作家注册接口
     *
     * @param authorReqDto 作家注册请求实体
     * @return 用户认证响应响应结果
     */
    CommonResult<UserAuthRespDto> authorRegister(AuthorReqDto authorReqDto);

    /**
     * 作家首页信息查询
     *
     * @return 作家主页响应结果
     */
    CommonResult<AuthorHomeRespDto> getAuthorHomeInfo();

    /**
     * 作家后台作品管理查询
     *
     * @param pageReqDto 作家后台作品管理接口查询
     * @return 作品列表
     */
    CommonResult<PageRespDto<AuthorNovelsListRespDto>> listAuthorNovelsList(PageReqDto pageReqDto);

    /**
     * 作家所有草稿查询
     *
     * @param page 分页请求参数
     * @return 草稿列表
     */
    CommonResult<PageRespDto<AuthorDraftsRespDto>> listAuthorDrafts(PageReqDto page);

    /**
     * 编辑页小说列表获取
     *
     * @return 小说列表
     */
    CommonResult<List<AuthorEditBookListResp>> listAuthorEditBook();

    /**
     * 获取审核中的章节列表
     *
     * @param pageReqDto 分页请求参数
     * @return 作家审核章节响应结果
     */
    CommonResult<PageRespDto<AuthorAuditListRespDto>> listAudits(PageReqDto pageReqDto);

    /**
     * 公告列表查询
     *
     * @param pageReqDto 分页请求参数
     * @return 公告列表
     */
    CommonResult<PageRespDto<AnnouncementRespDto>> listAnnouncement(PageReqDto pageReqDto);
}
