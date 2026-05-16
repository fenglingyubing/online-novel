package com.fengling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.exception.BusinessException;
import com.fengling.common.resp.CommonResult;
import com.fengling.common.util.AuthorAuthUtil;
import com.fengling.entity.AuthorInfo;
import com.fengling.entity.dto.ChapterEditInfoRespDto;
import com.fengling.entity.dto.UserInfoDto;
import com.fengling.mapper.AuthorMapper;
import com.fengling.mapper.ChapterMapper;
import com.fengling.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private final ChapterMapper chapterMapper;
    private final AuthorAuthUtil authorAuthUtil;
    private final AuthorMapper authorMapper;

    @Override
    public CommonResult<ChapterEditInfoRespDto> getChapterInfo(Long bookId, Long chapterId) {
        UserInfoDto userInfoDto = authorAuthUtil.authorAuth();
        // 获取当前作家id
        AuthorInfo authorInfo = authorMapper.selectOne(
                new LambdaQueryWrapper<AuthorInfo>()
                        .select(AuthorInfo::getId)
                        .eq(AuthorInfo::getUserId, userInfoDto.getId())
        );
        if (authorInfo == null) {
            throw new BusinessException(ResultCodeEnum.FORBIDDEN);
        }
        ChapterEditInfoRespDto chapterInfo = chapterMapper.getChapterInfo(bookId, chapterId, authorInfo.getId());
        if (chapterInfo == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "章节信息未找到");
        }
        return CommonResult.success(chapterInfo);
    }
}
