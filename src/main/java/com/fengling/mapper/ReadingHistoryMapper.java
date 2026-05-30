package com.fengling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fengling.entity.ReadingHistory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReadingHistoryMapper extends BaseMapper<ReadingHistory> {

    /**
     * 插入/更新阅读历史
     *
     * @param readingHistory 阅读历史请求参数
     */
    void upsert(ReadingHistory readingHistory);
}
