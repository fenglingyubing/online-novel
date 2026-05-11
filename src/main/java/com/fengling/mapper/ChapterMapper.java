package com.fengling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fengling.entity.ChapterInfo;
import com.fengling.entity.dto.ChapterListRespDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChapterMapper extends BaseMapper<ChapterInfo> {
    List<ChapterListRespDto> getChapterListByBookId(Long bookId);
}
