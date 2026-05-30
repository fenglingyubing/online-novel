package com.fengling.service.impl;

import cn.hutool.json.JSONUtil;
import com.fengling.common.constant.CacheConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.context.AuthUserInfo;
import com.fengling.common.exception.BusinessException;
import com.fengling.common.resp.CommonResult;
import com.fengling.common.util.RedisUtil;
import com.fengling.common.util.UserAuthUtil;
import com.fengling.entity.ReadingHistory;
import com.fengling.entity.dto.ReadingHistoryReqDto;
import com.fengling.service.ReadingHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReadingHistoryServiceImpl implements ReadingHistoryService {

    private final UserAuthUtil userAuthUtil;
    private final RedisUtil redisUtil;

    @Override
    public CommonResult<Void> updateReadingHistory(Long bookId, ReadingHistoryReqDto reqDto) {
        if (reqDto == null || bookId <= 0) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        Long chapterId = reqDto.getChapterId();
        String chapterName = reqDto.getChapterName();
        if (chapterId == null || chapterId <= 0 || chapterName == null || chapterName.isBlank()) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        AuthUserInfo authUserInfo = userAuthUtil.userAuth();
        Long userId = authUserInfo.getUserId();

        ReadingHistory readingHistory = new ReadingHistory();
        readingHistory.setUserId(userId);
        readingHistory.setBookId(bookId);
        readingHistory.setLastChapterId(chapterId);
        readingHistory.setLastChapterName(chapterName);
        readingHistory.setUpdateTime(LocalDateTime.now());

        // Redis key ：novel:reading:history:userId
        String key = CacheConstants.READING_HISTORY + userId;
        String hashKey = String.valueOf(bookId);
        String value = JSONUtil.toJsonStr(readingHistory);

        redisUtil.addRedisCacheHash(key, hashKey, value, CacheConstants.READING_HISTORY_TTL);

        // 标记脏数据
        String dirtyValue = userId + ":" + bookId;
        redisUtil.addZSet(
                CacheConstants.READING_HISTORY_DIRTY,
                dirtyValue,
                System.currentTimeMillis()
        );
        return CommonResult.success();
    }
}
