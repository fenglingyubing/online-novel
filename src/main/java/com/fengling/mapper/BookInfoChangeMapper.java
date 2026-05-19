package com.fengling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.entity.BookInfoChange;
import com.fengling.entity.dto.AuthorAuditInfoRespDto;
import com.fengling.entity.dto.AuthorBookInfoAuditRespDto;
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
}
