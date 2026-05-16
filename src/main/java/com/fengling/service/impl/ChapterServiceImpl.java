package com.fengling.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengling.common.constant.CommonConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.exception.BusinessException;
import com.fengling.common.resp.CommonResult;
import com.fengling.common.util.AuthorAuthUtil;
import com.fengling.entity.AuthorInfo;
import com.fengling.entity.BookInfo;
import com.fengling.entity.ChapterInfo;
import com.fengling.entity.dto.*;
import com.fengling.mapper.AuthorMapper;
import com.fengling.mapper.BookMapper;
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
    private final BookMapper bookMapper;

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

    @Override
    public CommonResult<Void> updateChapterInfo(Long bookId, Long chapterId, ChapterUpdateReqDto chapterUpdateReqDto) {
        UserInfoDto userInfoDto = authorAuthUtil.authorAuth();
        AuthorInfo authorInfo = authorMapper.selectOne(
                new LambdaQueryWrapper<AuthorInfo>()
                        .select(AuthorInfo::getId)
                        .eq(AuthorInfo::getUserId, userInfoDto.getId())
        );
        if (authorInfo == null) {
            throw new BusinessException(ResultCodeEnum.FORBIDDEN);
        }

        if (chapterUpdateReqDto == null) {
            throw new BusinessException(ResultCodeEnum.FAIL, "请求参数不能为空");
        }
        Integer chapterStatus = chapterUpdateReqDto.getChapterStatus();
        if (chapterStatus != null && (chapterStatus < 0 || chapterStatus > 3)) {
            throw new BusinessException(ResultCodeEnum.FAIL, "章节状态不合法");
        }
        int i = chapterMapper.updateChapterInfo(
                bookId,
                chapterId,
                authorInfo.getId(),
                chapterUpdateReqDto
        );
        if (i != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "更新失败");
        }
        return CommonResult.success();
    }

    @Override
    public CommonResult<ChapterSaveRespDto> saveChapterInfo(Long bookId, ChapterSaveReqDto chapterSaveReqDto) {
        UserInfoDto userInfoDto = authorAuthUtil.authorAuth();
        AuthorInfo authorInfo = authorMapper.selectOne(
                new LambdaQueryWrapper<AuthorInfo>()
                        .select(AuthorInfo::getId)
                        .eq(AuthorInfo::getUserId, userInfoDto.getId())
        );
        if (authorInfo == null) {
            throw new BusinessException(ResultCodeEnum.FORBIDDEN);
        }
        if (chapterSaveReqDto == null) {
            throw new BusinessException(ResultCodeEnum.FAIL, "章节信息为空");
        }
        Integer chapterStatus = chapterSaveReqDto.getChapterStatus();
        if (chapterStatus == null) {
            chapterStatus = CommonConstants.CHAPTER_STATUS_DRAFTS;
        } else if (
                !CommonConstants.CHAPTER_STATUS_DRAFTS.equals(chapterStatus) &&
                !CommonConstants.CHAPTER_STATUS_AUDIT.equals(chapterStatus)
        ) {
            throw new BusinessException(ResultCodeEnum.FAIL, "章节状态不对");
        }
        Long authorId = authorInfo.getId();
        BookInfo bookInfo = bookMapper.selectOne(
                new LambdaQueryWrapper<BookInfo>()
                        .select(BookInfo::getId)
                        .eq(BookInfo::getId, bookId)
                        .eq(BookInfo::getAuthorId, authorId)
        );
        if (bookInfo == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "未找到该小说");
        }
        ChapterInfo chapterInfo = BeanUtil.copyProperties(chapterSaveReqDto, ChapterInfo.class);
        ChapterInfo lastChapterInfo = chapterMapper.selectOne(
                new LambdaQueryWrapper<ChapterInfo>()
                        .select(ChapterInfo::getChapterNum)
                        .eq(ChapterInfo::getBookId, bookId)
                        .orderByDesc(ChapterInfo::getChapterNum)
                        .last("limit 1")
        );
        int chapterNum = 0;
        if (
                lastChapterInfo != null &&
                        lastChapterInfo.getChapterNum() != null
        ) {
            chapterNum = lastChapterInfo.getChapterNum();
        }
        chapterInfo.setBookId(bookId);
        chapterInfo.setChapterStatus(chapterStatus);
        chapterInfo.setChapterNum(chapterNum + 1);
        int i = chapterMapper.saveChapterInfo(bookId, authorId, chapterInfo);
        if (i != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "新增章节失败");
        }
        return CommonResult.success(new ChapterSaveRespDto(chapterInfo.getId()));
    }
}
