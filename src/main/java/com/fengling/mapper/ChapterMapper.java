package com.fengling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.entity.ChapterInfo;
import com.fengling.entity.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChapterMapper extends BaseMapper<ChapterInfo> {
    /**
     * 根据小说id查询小说目录
     *
     * @param bookId 小说id
     * @return 小说目录列表
     */
    List<ChapterListRespDto> getChapterListByBookId(Long bookId);

    /**
     * 根据小说id和章节id查询小说正文
     *
     * @param bookId    小说id
     * @param chapterId 章节id
     * @return 小说正文实体
     */
    ChapterContentRespDto getBookContentById(Long bookId, Long chapterId);

    /**
     * 获取上一章id
     *
     * @param bookId     小说章节id
     * @param chapterNum 小说章节数
     * @return 上一章id
     */
    Long getPreChapterId(@Param("bookId") Long bookId,
                         @Param("chapterNum") Integer chapterNum);

    /**
     * 获取上一章id
     *
     * @param bookId     小说章节id
     * @param chapterNum 小说章节数
     * @return 上一章id
     */
    Long getNextChapterId(@Param("bookId") Long bookId,
                          @Param("chapterNum") Integer chapterNum);

    /**
     * 查询作家草稿
     *
     * @param draftsPage 分页请求参数
     * @param bookIdList 小说Id集合
     * @return 草稿列表
     */
    Page<AuthorDraftsRespDto> listAuthorDrafts(Page<AuthorDraftsRespDto> draftsPage,
                                               @Param("bookIdList") List<Long> bookIdList);

    /**
     * 作家章节编辑信息查询
     *
     * @param bookId    小说id
     * @param chapterId 章节id
     * @param authorId  作者id
     * @return 章节信息
     */
    ChapterEditInfoRespDto getChapterInfo(@Param("bookId") Long bookId,
                                          @Param("chapterId") Long chapterId,
                                          @Param("authorId") Long authorId);

    /**
     * 更新章节信息
     *
     * @param bookId              小说id
     * @param chapterId           章节id
     * @param authorId            作家id
     * @param chapterUpdateReqDto 小说更新参数
     * @return 更新条数
     */
    int updateChapterInfo(@Param("bookId") Long bookId,
                          @Param("chapterId") Long chapterId,
                          @Param("authorId") Long authorId,
                          @Param("chapterUpdateReqDto") ChapterUpdateReqDto chapterUpdateReqDto);

    /**
     * 新增章节
     *
     * @param bookId      小说id
     * @param authorId    作者id
     * @param chapterInfo 章节请求参数
     * @return 影响行数
     */
    int saveChapterInfo(@Param("bookId") Long bookId,
                        @Param("authorId") Long authorId,
                        @Param("chapterInfo") ChapterInfo chapterInfo);

    /**
     * 删除章节
     *
     * @param bookId        小说id
     * @param authorId      作者id
     * @param chapterIdList 章节id列表
     * @return 影响行数
     */
    int deleteChapters(@Param("bookId") Long bookId,
                       @Param("authorId") Long authorId,
                       @Param("chapterIdList") List<Long> chapterIdList);

    /**
     * 查询审核中的章节
     *
     * @param page     分页请求参数
     * @param authorId 作家id
     * @return 审核章节列表
     */
    Page<AuthorAuditListRespDto> listAudits(Page<AuthorAuditListRespDto> page,
                                            @Param("authorId") Long authorId);

    /**
     * 查询作家下某作品详情的小说目录
     *
     * @param page     分页参数
     * @param bookId   小说id
     * @param authorId 作家id
     * @return 目录列表
     */
    Page<BookChapterListRespDto> listChapters(Page<BookChapterListRespDto> page,
                                              @Param("bookId") Long bookId,
                                              @Param("authorId") Long authorId);

    /**
     * 撤回审核中的章节
     *
     * @param bookId    小说id
     * @param chapterId 章节id
     * @param authorId  作者id
     * @return 影响行数
     */
    int updateChapterStatus(@Param("bookId") Long bookId,
                            @Param("chapterId") Long chapterId,
                            @Param("authorId") Long authorId);
}
