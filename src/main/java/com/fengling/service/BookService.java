package com.fengling.service;

import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {
    /**
     * 查询分类下的小说（分页）
     *
     * @param categoryId 分类id
     * @param pageReqDto 分页实体
     * @return 小说列表
     */
    CommonResult<PageRespDto<BookListRespDto>> listCategoryNovel(Integer categoryId, PageReqDto pageReqDto);

    /**
     * 查询小说详情
     *
     * @param bookId 小说id
     * @return BookInfoRespDto
     */
    CommonResult<BookInfoRespDto> getBookInfoById(Long bookId);

    /**
     * 根据小说id和章节id查询小说正文
     *
     * @param bookId    小说id
     * @param chapterId 章节id
     * @return 小说正文实体
     */
    CommonResult<ChapterContentRespDto> getBookContentById(Long bookId, Long chapterId);

    /**
     * 查询最新上架小说
     *
     * @return 最新小说列表
     */
    CommonResult<List<BookRecentListRespDto>> listRecentBookList();

    /**
     * 作家下的某个小说详情查询
     *
     * @param bookId     小说id
     * @param pageReqDto
     * @return 小说详情
     */
    CommonResult<AuthorBookInfoRespDto> getAuthorBookInfo(Long bookId, PageReqDto pageReqDto);

    /**
     * 小说变更信息提交审核
     *
     * @param bookId         小说id
     * @param bookInfoReqDto 变更信息请求参数
     * @return 无
     */
    CommonResult<Void> saveChangeBookInfo(Long bookId, AuthorBookInfoReqDto bookInfoReqDto);

    /**
     * 小说封面变更
     *
     * @param bookId   小说id
     * @param file     上传的文件
     * @param coverUrl 图片链接
     * @return 无
     */
    CommonResult<Void> saveBookCoverUrl(Long bookId, MultipartFile file, String coverUrl);

    /**
     * 查询小说变更信息审核列表
     *
     * @return 审核信息列表
     */
    CommonResult<PageRespDto<AuthorBookInfoAuditRespDto>> listBookInfoAudits(PageReqDto pageReqDto);

    /**
     * 作家更新小说信息-无需审核
     *
     * @param bookId                 小说id
     * @param bookInfoNotAuditReqDto 更新信息参数
     * @param pageReqDto             分页请求参数
     * @return 无
     */
    CommonResult<Void> updateAuthorBookInfo(Long bookId, AuthorBookInfoNotAuditReqDto bookInfoNotAuditReqDto, PageReqDto pageReqDto);

    /**
     * 作家新建作品
     *
     * @param createBookReqDto 新建作品请求参数
     * @param file             上传的图片文件
     * @param coverUrl         图片链接
     * @return 无
     */
    CommonResult<Void> saveCreateBookInfo(AuthorCreateBookReqDto createBookReqDto, MultipartFile file, String coverUrl);

    /**
     * 删除信息变更
     *
     * @param auditId 审核信息id
     * @return 无
     */
    CommonResult<Void> deleteAuditInfo(Long auditId);

    /**
     * 变更信息详情查看
     *
     * @param auditId 变更信息审核id
     * @return 变更信息详情
     */
    CommonResult<AuthorAuditInfoRespDto> getAuditInfo(Long auditId);
}
