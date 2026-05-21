package com.fengling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.ChapterAudit;
import com.fengling.entity.dto.AdminAuditInfoReqDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChapterAuditMapper extends BaseMapper<ChapterAudit> {
}
