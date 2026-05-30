package com.fengling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.entity.ReadingHistory;
import com.fengling.entity.dto.ReadingHistoryRespDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReadingHistoryMapper extends BaseMapper<ReadingHistory> {

    /**
     * 插入/更新阅读历史
     *
     * @param readingHistory 阅读历史请求参数
     */
    void upsert(ReadingHistory readingHistory);

    /**
     * 阅读历史列表查询
     *
     * @param page   分页请求参数
     * @param userId 用户id
     * @return 阅读历史列表
     */
    Page<ReadingHistoryRespDto> listReadingHistory(Page<ReadingHistoryRespDto> page,
                                                   @Param("userId") Long userId);
}
