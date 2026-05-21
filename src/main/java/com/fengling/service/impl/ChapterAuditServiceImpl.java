package com.fengling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fengling.common.constant.CommonConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.exception.BusinessException;
import com.fengling.common.resp.CommonResult;
import com.fengling.common.util.AdminAuthUtil;
import com.fengling.entity.ChapterAudit;
import com.fengling.entity.ChapterInfo;
import com.fengling.entity.dto.AdminAuditInfoReqDto;
import com.fengling.entity.dto.AdminInfoDto;
import com.fengling.mapper.ChapterAuditMapper;
import com.fengling.mapper.ChapterMapper;
import com.fengling.service.ChapterAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChapterAuditServiceImpl implements ChapterAuditService {

    private final AdminAuthUtil adminAuthUtil;
    private final ChapterAuditMapper chapterAuditMapper;
    private final ChapterMapper chapterMapper;

    @Override
    @Transactional
    public CommonResult<Void> updateAdminAuditChaptersStatus(Long auditId, AdminAuditInfoReqDto auditInfoReqDto) {
        if (auditInfoReqDto == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        Integer auditStatus = auditInfoReqDto.getAuditStatus();
        if (
                auditStatus == null ||
                        (!CommonConstants.CHAPTER_AUDIT_STATUS_PASS.equals(auditStatus) &&
                                !CommonConstants.CHAPTER_AUDIT_STATUS_REJECTED.equals(auditStatus))
        ) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        AdminInfoDto adminInfoDto = adminAuthUtil.adminAuth();

        LocalDateTime now = LocalDateTime.now();
        int update = chapterAuditMapper.update(
                new LambdaUpdateWrapper<ChapterAudit>()
                        .set(ChapterAudit::getAuditAdminId, adminInfoDto.getId())
                        .set(ChapterAudit::getAuditRemark, auditInfoReqDto.getAuditRemark())
                        .set(ChapterAudit::getAuditTime, now)
                        .set(ChapterAudit::getAuditStatus, auditStatus)
                        .eq(ChapterAudit::getId, auditId)
                        .eq(ChapterAudit::getAuditStatus, CommonConstants.CHAPTER_AUDIT_STATUS_AUDIT)
                        .eq(ChapterAudit::getApplyStatus, CommonConstants.APPLY_STATUS_NOT_APPLY)
        );

        if (update != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "审核失败");
        }

        ChapterAudit chapterAudit = chapterAuditMapper.selectOne(
                new LambdaQueryWrapper<ChapterAudit>()
                        .select(
                                ChapterAudit::getChapterId,
                                ChapterAudit::getBookId
                        )
                        .eq(ChapterAudit::getId, auditId)
                        .eq(ChapterAudit::getAuditStatus, auditStatus)
                        .eq(ChapterAudit::getApplyStatus, CommonConstants.APPLY_STATUS_NOT_APPLY)
        );

        if (CommonConstants.CHAPTER_AUDIT_STATUS_REJECTED.equals(auditStatus)) {
            // 驳回
            int j = chapterMapper.update(
                    new LambdaUpdateWrapper<ChapterInfo>()
                            .set(ChapterInfo::getChapterStatus, CommonConstants.CHAPTER_STATUS_DRAFTS)
                            .set(ChapterInfo::getUpdateTime, now)
                            .eq(ChapterInfo::getId, chapterAudit.getChapterId())
                            .eq(ChapterInfo::getBookId, chapterAudit.getBookId())
                            .eq(ChapterInfo::getChapterStatus, CommonConstants.CHAPTER_STATUS_AUDIT)
            );
            if (j != 1) {
                throw new BusinessException(ResultCodeEnum.FAIL, "审核失败");
            }
        } else {
            // 通过
            int j = chapterMapper.update(
                    new LambdaUpdateWrapper<ChapterInfo>()
                            .set(ChapterInfo::getChapterStatus, CommonConstants.CHAPTER_STATUS_RELEASE)
                            .set(ChapterInfo::getPublishTime, now)
                            .set(ChapterInfo::getUpdateTime, now)
                            .eq(ChapterInfo::getId, chapterAudit.getChapterId())
                            .eq(ChapterInfo::getBookId, chapterAudit.getBookId())
                            .eq(ChapterInfo::getChapterStatus, CommonConstants.CHAPTER_STATUS_AUDIT)
            );

            if (j != 1) {
                throw new BusinessException(ResultCodeEnum.FAIL, "审核失败");
            }

        }

        int i = chapterAuditMapper.update(
                new LambdaUpdateWrapper<ChapterAudit>()
                        .set(ChapterAudit::getApplyStatus, CommonConstants.APPLY_STATUS_APPLY)
                        .eq(ChapterAudit::getId, auditId)
                        .eq(ChapterAudit::getApplyStatus, CommonConstants.APPLY_STATUS_NOT_APPLY)
                        .eq(ChapterAudit::getAuditStatus, auditStatus)
        );

        if (i != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "审核失败");
        }
        return CommonResult.success();
    }
}
