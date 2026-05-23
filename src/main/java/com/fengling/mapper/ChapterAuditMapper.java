package com.fengling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fengling.entity.ChapterAudit;
import com.fengling.entity.dto.AdminAuditChapterRespDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChapterAuditMapper extends BaseMapper<ChapterAudit> {

    /**
     * 章节审核信息详情查询
     *
     * @param auditId     审核id
     * @param auditStatus 审核状态
     * @return 章节审核信息详情
     */
    AdminAuditChapterRespDto getAuditChapterInfo(@Param("auditId") Long auditId,
                                                 @Param("auditStatus") Integer auditStatus);
}
