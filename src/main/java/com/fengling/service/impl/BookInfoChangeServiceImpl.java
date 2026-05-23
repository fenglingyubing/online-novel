package com.fengling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.common.constant.CommonConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.exception.BusinessException;
import com.fengling.common.resp.CommonResult;
import com.fengling.common.util.AdminAuthUtil;
import com.fengling.common.util.PageAuthUtil;
import com.fengling.entity.BookInfo;
import com.fengling.entity.BookInfoChange;
import com.fengling.entity.dto.*;
import com.fengling.mapper.BookInfoChangeMapper;
import com.fengling.mapper.BookMapper;
import com.fengling.service.BookInfoChangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookInfoChangeServiceImpl implements BookInfoChangeService {

    private final AdminAuthUtil adminAuthUtil;
    private final BookInfoChangeMapper bookInfoChangeMapper;
    private final PageAuthUtil pageAuthUtil;
    private final BookMapper bookMapper;

    @Override
    public CommonResult<PageRespDto<AdminAuditListRespDto>> listAdminAuditList(PageReqDto pageReqDto, Integer auditStatus) {
        argsAuth(pageReqDto, auditStatus);
        Page<AdminAuditListRespDto> page = new Page<>(
                pageReqDto.getPageNum(),
                pageReqDto.getPageSize()
        );
        Page<AdminAuditListRespDto> pageAuditList = bookInfoChangeMapper.listAdminAuditList(page, auditStatus);
        return CommonResult.success(PageRespDto.of(pageAuditList));
    }

    @Override
    public CommonResult<PageRespDto<AdminAuditCreateListRespDto>> listAdminAuditCreateList(PageReqDto pageReqDto, Integer auditStatus) {
        argsAuth(pageReqDto, auditStatus);
        Page<AdminAuditCreateListRespDto> page = new Page<>(
                pageReqDto.getPageNum(),
                pageReqDto.getPageSize()
        );
        Page<AdminAuditCreateListRespDto> pageAuditCreateList = bookInfoChangeMapper.listAdminAuditCreateList(
                page,
                auditStatus
        );
        return CommonResult.success(PageRespDto.of(pageAuditCreateList));
    }

    @Override
    public CommonResult<PageRespDto<AdminAuditChaptersListRespDto>> listAdminAuditChaptersList(PageReqDto pageReqDto, Integer auditStatus) {
        argsAuth(pageReqDto, auditStatus);
        Page<AdminAuditChaptersListRespDto> page = new Page<>(
                pageReqDto.getPageNum(),
                pageReqDto.getPageSize()
        );
        Page<AdminAuditChaptersListRespDto> pageAuditChapters = bookInfoChangeMapper.listAdminAuditChaptersList(
                page,
                auditStatus
        );
        return CommonResult.success(PageRespDto.of(pageAuditChapters));
    }

    @Override
    @Transactional
    public CommonResult<Void> updateAdminAuditStatus(
            Long auditId,
            AdminAuditInfoReqDto auditInfoReqDto
    ) {
        if (auditInfoReqDto == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        Integer auditStatus = auditInfoReqDto.getAuditStatus();
        if (
                auditStatus == null ||
                        (!CommonConstants.INFO_CHANGE_PASS.equals(auditStatus) &&
                                !CommonConstants.INFO_CHANGE_REJECTED.equals(auditStatus))
        ) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }
        AdminInfoDto adminInfoDto = adminAuthUtil.adminAuth();
        int update = bookInfoChangeMapper.update(
                new LambdaUpdateWrapper<BookInfoChange>()
                        .set(BookInfoChange::getAuditAdminId, adminInfoDto.getId())
                        .set(BookInfoChange::getAuditStatus, auditStatus)
                        .set(BookInfoChange::getAuditTime, LocalDateTime.now())
                        .set(BookInfoChange::getAuditRemark, auditInfoReqDto.getAuditRemark())
                        .eq(BookInfoChange::getId, auditId)
                        .eq(BookInfoChange::getAuditType, CommonConstants.AUDIT_TYPE_INFORMATION_CHANGE)
                        .eq(BookInfoChange::getAuditStatus, CommonConstants.INFO_CHANGE_AUDIT)
                        .eq(BookInfoChange::getApplyStatus, CommonConstants.APPLY_STATUS_NOT_APPLY)
        );

        if (update != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "审核失败");
        }

        if (CommonConstants.INFO_CHANGE_REJECTED.equals(auditStatus)) {
            return CommonResult.success();
        }

        BookInfoChange bookInfoChange = bookInfoChangeMapper.selectOne(
                new LambdaQueryWrapper<BookInfoChange>()
                        .select(
                                BookInfoChange::getBookId,
                                BookInfoChange::getAuthorId,
                                BookInfoChange::getBookName,
                                BookInfoChange::getBookIntro,
                                BookInfoChange::getCoverUrl,
                                BookInfoChange::getPublishStatus
                        )
                        .eq(BookInfoChange::getId, auditId)
        );

        int i = bookMapper.update(
                new LambdaUpdateWrapper<BookInfo>()
                        .set(
                                bookInfoChange.getBookName() != null,
                                BookInfo::getBookName,
                                bookInfoChange.getBookName()
                        )
                        .set(
                                bookInfoChange.getBookIntro() != null,
                                BookInfo::getBookIntro,
                                bookInfoChange.getBookIntro()
                        )
                        .set(
                                bookInfoChange.getCoverUrl() != null,
                                BookInfo::getCoverUrl,
                                bookInfoChange.getCoverUrl()
                        )
                        .set(
                                bookInfoChange.getPublishStatus() != null,
                                BookInfo::getPublishStatus,
                                bookInfoChange.getPublishStatus()
                        )
                        .eq(BookInfo::getId, bookInfoChange.getBookId())
                        .eq(BookInfo::getAuthorId, bookInfoChange.getAuthorId())
        );

        if (i != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "审核失败");
        }

        int j = bookInfoChangeMapper.update(
                new LambdaUpdateWrapper<BookInfoChange>()
                        .set(BookInfoChange::getApplyStatus, CommonConstants.APPLY_STATUS_APPLY)
                        .eq(BookInfoChange::getId, auditId)
                        .eq(BookInfoChange::getAuditStatus, CommonConstants.INFO_CHANGE_PASS)
                        .eq(BookInfoChange::getApplyStatus, CommonConstants.APPLY_STATUS_NOT_APPLY)
        );

        if (j != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "审核失败");
        }
        return CommonResult.success();
    }

    @Override
    @Transactional
    public CommonResult<Void> updateAdminAuditCreateStatus(Long auditId, AdminAuditInfoReqDto auditInfoReqDto) {
        if (auditInfoReqDto == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        Integer auditStatus = auditInfoReqDto.getAuditStatus();
        if (auditStatus == null ||
                (!CommonConstants.NEW_BOOK_CHANGE_PASS.equals(auditStatus) &&
                        !CommonConstants.NEW_BOOK_CHANGE_REJECTED.equals(auditStatus))
        ) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        AdminInfoDto adminInfoDto = adminAuthUtil.adminAuth();
        LocalDateTime now = LocalDateTime.now();
        int update = bookInfoChangeMapper.update(
                new LambdaUpdateWrapper<BookInfoChange>()
                        .set(BookInfoChange::getAuditAdminId, adminInfoDto.getId())
                        .set(BookInfoChange::getAuditStatus, auditStatus)
                        .set(BookInfoChange::getAuditTime, now)
                        .set(BookInfoChange::getAuditRemark, auditInfoReqDto.getAuditRemark())
                        .eq(BookInfoChange::getId, auditId)
                        .eq(BookInfoChange::getAuditType, CommonConstants.AUDIT_TYPE_CREATE_WORK)
                        .eq(BookInfoChange::getAuditStatus, CommonConstants.NEW_BOOK_CHANGE_AUDIT)
                        .eq(BookInfoChange::getApplyStatus, CommonConstants.APPLY_STATUS_NOT_APPLY)
        );

        if (update != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "审核失败");
        }

        if (CommonConstants.NEW_BOOK_CHANGE_REJECTED.equals(auditStatus)) {
            return CommonResult.success();
        }

        BookInfoChange bookInfoChange = bookInfoChangeMapper.selectOne(
                new LambdaQueryWrapper<BookInfoChange>()
                        .select(
                                BookInfoChange::getAuthorId,
                                BookInfoChange::getBookName,
                                BookInfoChange::getBookIntro,
                                BookInfoChange::getCoverUrl,
                                BookInfoChange::getCategoryId
                        )
                        .eq(BookInfoChange::getId, auditId)
                        .eq(BookInfoChange::getAuditStatus, CommonConstants.NEW_BOOK_CHANGE_PASS)
                        .eq(BookInfoChange::getApplyStatus, CommonConstants.APPLY_STATUS_NOT_APPLY)

        );

        BookInfo bookInfo = new BookInfo();
        bookInfo.setBookName(bookInfoChange.getBookName());
        bookInfo.setCoverUrl(bookInfoChange.getCoverUrl());
        bookInfo.setAuthorId(bookInfoChange.getAuthorId());
        bookInfo.setCategoryId(bookInfoChange.getCategoryId());
        bookInfo.setPublishStatus(CommonConstants.PUBLISH_STATUS_SHELVES);
        bookInfo.setBookIntro(bookInfoChange.getBookIntro());
        bookInfo.setCreateTime(now);
        bookInfo.setUpdateTime(now);
        int insert = bookMapper.insert(bookInfo);

        if (insert != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "审核失败");
        }

        int j = bookInfoChangeMapper.update(
                new LambdaUpdateWrapper<BookInfoChange>()
                        .set(BookInfoChange::getBookId, bookInfo.getId())
                        .set(BookInfoChange::getApplyStatus, CommonConstants.APPLY_STATUS_APPLY)
                        .eq(BookInfoChange::getId, auditId)
                        .eq(BookInfoChange::getAuditStatus, CommonConstants.NEW_BOOK_CHANGE_PASS)
                        .eq(BookInfoChange::getApplyStatus, CommonConstants.APPLY_STATUS_NOT_APPLY)
        );

        if (j != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "审核失败");
        }
        return CommonResult.success();
    }

    @Override
    public CommonResult<AdminAuditInfoRespDto> getAuditInfo(Long auditId, Long authorId, Long bookId, Integer auditStatus) {
        if (
                auditStatus == null ||
                        auditStatus < CommonConstants.INFO_CHANGE_AUDIT ||
                        auditStatus > CommonConstants.INFO_CHANGE_REJECTED
        ) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        adminAuthUtil.adminAuth();
        AdminAuditInfoRespDto auditInfoRespDto = bookInfoChangeMapper.getAdminAuditInfo(
                auditId,
                authorId,
                bookId,
                auditStatus
        );

        if (auditInfoRespDto == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "审核信息不存在");
        }
        return CommonResult.success(auditInfoRespDto);
    }

    @Override
    public CommonResult<AdminAuditCreateRespDto> getAuditCreateInfo(Long auditId, Integer auditStatus) {
        if (
                auditStatus == null ||
                        auditStatus < CommonConstants.NEW_BOOK_CHANGE_AUDIT ||
                        auditStatus > CommonConstants.NEW_BOOK_CHANGE_REJECTED
        ){
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        adminAuthUtil.adminAuth();
        AdminAuditCreateRespDto auditCreateInfo =bookInfoChangeMapper.getAuditCreateInfo(auditId, auditStatus);

        if (auditCreateInfo == null){
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "审核信息不存在");
        }

        return CommonResult.success(auditCreateInfo);
    }

    /**
     * 审核列表参数校验
     *
     * @param pageReqDto  分页请求参数
     * @param auditStatus 审核状态
     */
    private void argsAuth(PageReqDto pageReqDto, Integer auditStatus) {
        if (
                auditStatus == null ||
                        auditStatus < CommonConstants.CHAPTER_AUDIT_STATUS_AUDIT ||
                        auditStatus > CommonConstants.CHAPTER_AUDIT_STATUS_REJECTED
        ) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }
        adminAuthUtil.adminAuth();
        pageAuthUtil.pageAuth(pageReqDto);
    }
}
