package com.fengling.task;

import cn.hutool.json.JSONUtil;
import com.fengling.common.constant.CacheConstants;
import com.fengling.common.util.RedisUtil;
import com.fengling.entity.ReadingHistory;
import com.fengling.mapper.ReadingHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ReadingHistorySyncTask {

    private final RedisUtil redisUtil;
    private final ReadingHistoryMapper readingHistoryMapper;

    @Scheduled(fixedDelay = 60_000)
    public void syncReadingHistory() {
        String pattern = CacheConstants.READING_HISTORY + "*";
        Set<String> keys = redisUtil.scanKeys(pattern, 100);
        if (keys == null || keys.isEmpty()) {
            return;
        }

        for (String key : keys) {
            Map<Object, Object> entries = redisUtil.getHashEntries(key);
            if (entries == null || entries.isEmpty()) {
                continue;
            }

            for (Object value : entries.values()) {
                if (value == null) {
                    continue;
                }

                ReadingHistory readingHistory = JSONUtil.toBean(value.toString(), ReadingHistory.class);
                readingHistoryMapper.upsert(readingHistory);
            }
        }
    }
}
