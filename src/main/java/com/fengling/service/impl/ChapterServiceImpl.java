package com.fengling.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fengling.common.constant.CommonConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.exception.BusinessException;
import com.fengling.common.resp.CommonResult;
import com.fengling.common.util.AuthorAuthUtil;
import com.fengling.entity.BookInfo;
import com.fengling.entity.ChapterAudit;
import com.fengling.entity.ChapterInfo;
import com.fengling.entity.dto.*;
import com.fengling.mapper.BookMapper;
import com.fengling.mapper.ChapterAuditMapper;
import com.fengling.mapper.ChapterMapper;
import com.fengling.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private final ChapterMapper chapterMapper;
    private final AuthorAuthUtil authorAuthUtil;
    private final BookMapper bookMapper;
    private final ChapterAuditMapper chapterAuditMapper;

    @Override
    public CommonResult<ChapterEditInfoRespDto> getChapterInfo(Long bookId, Long chapterId) {
        Long authorId = authorAuthUtil.getCurrentAuthorId();
        ChapterEditInfoRespDto chapterInfo = chapterMapper.getChapterInfo(bookId, chapterId, authorId);
        if (chapterInfo == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "章节信息未找到");
        }
        return CommonResult.success(chapterInfo);
    }

    @Override
    @Transactional
    public CommonResult<Void> updateChapterInfo(Long bookId, Long chapterId, ChapterUpdateReqDto chapterUpdateReqDto) {
        Long authorId = authorAuthUtil.getCurrentAuthorId();

        if (chapterUpdateReqDto == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }
        Integer chapterStatus = chapterUpdateReqDto.getChapterStatus();
        if (
                chapterStatus != null &&
                        !CommonConstants.CHAPTER_STATUS_DRAFTS.equals(chapterStatus) &&
                        !CommonConstants.CHAPTER_STATUS_AUDIT.equals(chapterStatus)
        ) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        int i = chapterMapper.updateChapterInfo(
                bookId,
                chapterId,
                authorId,
                chapterUpdateReqDto
        );
        if (i != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "更新失败");
        }
        if (!CommonConstants.CHAPTER_STATUS_AUDIT.equals(chapterStatus)) {
            return CommonResult.success();
        }

        ChapterAudit chapterAudit = chapterAuditMapper.selectOne(
                new LambdaQueryWrapper<ChapterAudit>()
                        .select(ChapterAudit::getId)
                        .eq(ChapterAudit::getChapterId, chapterId)
                        .eq(ChapterAudit::getBookId, bookId)
                        .eq(ChapterAudit::getAuthorId, authorId)
                        .eq(ChapterAudit::getAuditStatus, CommonConstants.CHAPTER_AUDIT_STATUS_AUDIT)
        );
        if (chapterAudit != null) {
            int update = chapterAuditMapper.update(
                    null,
                    new LambdaUpdateWrapper<ChapterAudit>()
                            .set(ChapterAudit::getUpdateTime, LocalDateTime.now())
                            .eq(ChapterAudit::getId, chapterAudit.getId())
            );

            if (update != 1) {
                throw new BusinessException(ResultCodeEnum.FAIL, "更新失败");
            }
            return CommonResult.success();
        }

        chapterAudit = new ChapterAudit();
        chapterAudit.setChapterId(chapterId);
        chapterAudit.setBookId(bookId);
        chapterAudit.setAuthorId(authorId);
        int insert = chapterAuditMapper.insert(chapterAudit);
        if (insert != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "更新失败");
        }
        return CommonResult.success();
    }

    @Override
    @Transactional
    public CommonResult<ChapterSaveRespDto> saveChapterInfo(Long bookId, ChapterSaveReqDto chapterSaveReqDto) {
        Long authorId = authorAuthUtil.getCurrentAuthorId();
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

        if (!CommonConstants.CHAPTER_STATUS_AUDIT.equals(chapterStatus)) {
            return CommonResult.success(new ChapterSaveRespDto(chapterInfo.getId()));
        }

        Long chapterId = chapterInfo.getId();
        ChapterAudit chapterAudit = new ChapterAudit();
        chapterAudit.setChapterId(chapterId);
        chapterAudit.setBookId(bookId);
        chapterAudit.setAuthorId(authorId);
        int insert = chapterAuditMapper.insert(chapterAudit);

        if (insert != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "更新失败");
        }

        return CommonResult.success(new ChapterSaveRespDto(chapterId));
    }

    @Override
    @Transactional
    public CommonResult<Void> deleteChapters(Long bookId, List<Long> chapterIdList) {
        if (chapterIdList == null || chapterIdList.isEmpty()) {
            throw new BusinessException(ResultCodeEnum.FAIL, "章节id为空");
        }
        if (chapterIdList.stream().anyMatch(Objects::isNull)) {
            throw new BusinessException(ResultCodeEnum.FAIL, "章节id为空");
        }
        Long currentAuthorId = authorAuthUtil.getCurrentAuthorId();
        long chapterCount = chapterIdList.stream().distinct().count();
        int i = chapterMapper.deleteChapters(bookId, currentAuthorId, chapterIdList);
        if (i != chapterCount) {
            throw new BusinessException(ResultCodeEnum.FAIL, "删除失败");
        }
        return CommonResult.success();
    }

    @Override
    public CommonResult<Void> updateChapterStatus(Long bookId, Long chapterId) {
        Long authorId = authorAuthUtil.getCurrentAuthorId();
        int update = chapterMapper.updateChapterStatus(bookId, chapterId, authorId);
        if (update != 1) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "审核章节不存在");
        }
        return CommonResult.success();
    }
}
