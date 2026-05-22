package com.fengling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.entity.BookInfoChange;
import com.fengling.entity.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BookInfoChangeMapper extends BaseMapper<BookInfoChange> {

    /**
     * 查询小说变更信息审核列表
     *
     * @param page     分页请求参数
     * @param authorId 作者id
     * @return 审核信息列表
     */
    Page<AuthorBookInfoAuditRespDto> listBookInfoAudits(Page<AuthorBookInfoAuditRespDto> page,
                                                        @Param("authorId") Long authorId);

    /**
     * 变更信息详情查询
     *
     * @param auditId  变更信息审核id
     * @param authorId 作家id
     * @return 变更信息详情
     */
    AuthorAuditInfoRespDto getAuditInfo(@Param("auditId") Long auditId,
                                        @Param("authorId") Long authorId);

    /**
     * 管理员后台-变更信息审核列表查询
     *
     * @param page        分页请求参数
     * @param auditStatus 审核状态id
     * @return 变更信息审核列表
     */
    Page<AdminAuditListRespDto> listAdminAuditList(Page<AdminAuditListRespDto> page,
                                                   @Param("auditStatus") Integer auditStatus);

    /**
     * 新建作品审核列表查询
     *
     * @param page        分页请求参数
     * @param auditStatus 审核状态
     * @return 新建作品审核列表
     */
    Page<AdminAuditCreateListRespDto> listAdminAuditCreateList(Page<AdminAuditCreateListRespDto> page,
                                                               @Param("auditStatus") Integer auditStatus);

    /**
     * 章节审核列表查询
     *
     * @param page        分页请求参数
     * @param auditStatus 审核状态
     * @return 章节审核列表
     */
    Page<AdminAuditChaptersListRespDto> listAdminAuditChaptersList(Page<AdminAuditChaptersListRespDto> page,
                                                                   @Param("auditStatus") Integer auditStatus);

    /**
     * 变更信息审核信息详情查询
     *
     * @param auditId     审核id
     * @param authorId    作家id
     * @param bookId      小说id
     * @param auditStatus 审核状态
     * @return 审核信息详情
     */
    AdminAuditInfoRespDto getAdminAuditInfo(@Param("auditId") Long auditId,
                                            @Param("authorId") Long authorId,
                                            @Param("bookId") Long bookId,
                                            @Param("auditStatus") Integer auditStatus);
}
