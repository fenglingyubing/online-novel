package com.fengling.task;

import cn.hutool.json.JSONUtil;
import com.fengling.common.constant.CacheConstants;
import com.fengling.common.util.RedisUtil;
import com.fengling.entity.ReadingHistory;
import com.fengling.mapper.ReadingHistoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReadingHistorySyncTask {

    private final RedisUtil redisUtil;
    private final ReadingHistoryMapper readingHistoryMapper;

    @Scheduled(fixedDelay = 60_000)
    public void syncReadingHistory() {
        long maxScore = System.currentTimeMillis() - 10_000;

        List<String> dirtyValues = redisUtil.popZSetByScore(
                CacheConstants.READING_HISTORY_DIRTY,
                maxScore,
                100
        );

        if (dirtyValues.isEmpty()) {
            return;
        }

        for (String dirtyValue : dirtyValues) {
            try {
                String[] parts = dirtyValue.split(":", 2);
                if (parts.length != 2) {
                    continue;
                }
                String userId = parts[0];
                String bookId = parts[1];

                String key = CacheConstants.READING_HISTORY + userId;
                String hashValue = redisUtil.getHashValue(key, bookId);
                if (hashValue == null) {
                    continue;
                }
                ReadingHistory readingHistory = JSONUtil.toBean(hashValue, ReadingHistory.class);
                readingHistoryMapper.upsert(readingHistory);
            } catch (Exception e) {
                log.error("同步阅读历史失败，dirtyValue={}", dirtyValue, e);
                redisUtil.addZSet(
                        CacheConstants.READING_HISTORY_DIRTY,
                        dirtyValue,
                        System.currentTimeMillis()
                );
            }
        }
    }
}
