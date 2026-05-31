package com.fengling.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.common.constant.CacheConstants;
import com.fengling.common.constant.CommonConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.context.AuthUserInfo;
import com.fengling.common.context.UserContext;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.exception.BusinessException;
import com.fengling.common.resp.CommonResult;
import com.fengling.common.util.AuthorAuthUtil;
import com.fengling.common.util.OSSUtil;
import com.fengling.common.util.PageAuthUtil;
import com.fengling.common.util.RedisUtil;
import com.fengling.entity.BookCategory;
import com.fengling.entity.BookInfo;
import com.fengling.entity.BookInfoChange;
import com.fengling.entity.BookShelf;
import com.fengling.entity.dto.*;
import com.fengling.mapper.*;
import com.fengling.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;
    private final ChapterMapper chapterMapper;
    private final BookShelfMapper bookShelfMapper;
    private final RedisUtil redisUtil;
    private final AuthorAuthUtil authorAuthUtil;
    private final BookInfoChangeMapper bookInfoChangeMapper;
    private final OSSUtil ossUtil;
    private final BookCategoryMapper bookCategoryMapper;

    @Override
    public CommonResult<PageRespDto<BookListRespDto>> listCategoryNovel(Integer categoryId, PageReqDto pageReqDto) {
        //分页对象
        Page<BookListRespDto> page = new Page<>(pageReqDto.getPageNum(), pageReqDto.getPageSize());
        Page<BookListRespDto> bookPage;
        if (categoryId == 0) {
            // 查询所有小说
            bookPage = bookMapper.selectAllNovelPage(page);
        } else {
            bookPage = bookMapper.selectCategoryNovelPage(page, categoryId);
        }
        // 压缩简介
        bookPage.getRecords().forEach(
                book -> book.setBookIntro(shortBookIntro(book.getBookIntro()))
        );
        return CommonResult.success(PageRespDto.of(bookPage));
    }

    @Override
    public CommonResult<BookInfoRespDto> getBookInfoById(Long bookId) {
        BookInfoRespDto bookInfoRespDto = bookMapper.getBookInfoById(bookId);
        if (bookInfoRespDto == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND);
        }
        //获取是否在书架中
        AuthUserInfo authUserInfo = UserContext.getAuthUserInfo();
        if (authUserInfo != null) {
            BookShelf one = bookShelfMapper.selectOne(
                    new LambdaQueryWrapper<BookShelf>()
                            .eq(BookShelf::getUserId, authUserInfo.getUserId())
                            .eq(BookShelf::getBookId, bookId)
            );
            if (one != null) {
                bookInfoRespDto.setIsShelf(true);
            }
        }
        List<ChapterListRespDto> chapterList = chapterMapper.getChapterListByBookId(bookId);
        bookInfoRespDto.setChapterList(chapterList);
        return CommonResult.success(bookInfoRespDto);
    }

    @Override
    public CommonResult<ChapterContentRespDto> getBookContentById(Long bookId, Long chapterId) {
        ChapterContentRespDto chapterContentRespDto = chapterMapper.getBookContentById(bookId, chapterId);
        if (chapterContentRespDto == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "小说章节不存在");
        }
        chapterContentRespDto.setPreChapterId(
                chapterMapper.getPreChapterId(bookId, chapterContentRespDto.getChapterNum())
        );
        chapterContentRespDto.setNextChapterId(
                chapterMapper.getNextChapterId(bookId, chapterContentRespDto.getChapterNum())
        );

        UserContext.getAuthUserInfo();
        return CommonResult.success(chapterContentRespDto);
    }

    @Override
    public CommonResult<List<BookRecentListRespDto>> listRecentBookList() {
        // 从redis读取
        String jsonStr = redisUtil.getValueForKey(CacheConstants.NOVEL_RECENT);
        if (jsonStr != null && !jsonStr.isBlank()) {
            return CommonResult.success(JSONUtil.toList(jsonStr, BookRecentListRespDto.class));
        }
        List<BookRecentListRespDto> recentBookList = bookMapper.listRecentBookList(
                CommonConstants.NOVEL_RECENT_LIMIT
        );
        recentBookList.forEach(
                book -> book.setBookIntro(
                        shortBookIntro(book.getBookIntro()))
        );
        // 将列表转换为字符串
        String valueStr = JSONUtil.toJsonStr(recentBookList);
        redisUtil.addRedisCache(
                CacheConstants.NOVEL_RECENT,
                valueStr,
                CacheConstants.NOVEL_RECENT_TTL
        );
        return CommonResult.success(recentBookList);
    }

    @Override
    public CommonResult<AuthorBookInfoRespDto> getAuthorBookInfo(Long bookId, PageReqDto pageReqDto) {
        Long authorId = authorAuthUtil.getCurrentAuthorId();
        AuthorBookInfoRespDto bookInfo = bookMapper.getAuthorBookInfo(bookId, authorId);
        if (bookInfo == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "小说信息不存在");
        }
        Page<BookChapterListRespDto> page = new Page<>(
                pageReqDto.getPageNum(), pageReqDto.getPageSize()
        );
        Page<BookChapterListRespDto> chapterPage = chapterMapper.listChapters(page, bookId, authorId);
        bookInfo.setBookChapterList(PageRespDto.of(chapterPage));
        return CommonResult.success(bookInfo);
    }

    @Override
    public CommonResult<Void> saveChangeBookInfo(Long bookId, AuthorBookInfoReqDto bookInfoReqDto) {
        if (bookInfoReqDto == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }
        Long authorId = authorAuthUtil.getCurrentAuthorId();

        BookInfo bookInfo = bookMapper.selectOne(
                new LambdaQueryWrapper<BookInfo>()
                        .select(BookInfo::getPublishStatus)
                        .eq(BookInfo::getId, bookId)
                        .eq(BookInfo::getAuthorId, authorId)
        );

        Integer publishStatus = bookInfoReqDto.getPublishStatus();
        Integer auditPublishStatus = null;

        if (bookInfo == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "小说信息不存在");
        }

        if (publishStatus != null) {
            if (publishStatus < 0 || publishStatus > 1) {
                throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
            }

            if (
                    CommonConstants.PUBLISH_STATUS_UNDERCARRIAGE.equals(bookInfo.getPublishStatus()) &&
                            CommonConstants.PUBLISH_STATUS_SHELVES.equals(publishStatus)
            ) {
                auditPublishStatus = CommonConstants.PUBLISH_STATUS_SHELVES;
            }
        }

        boolean hasChange = bookInfoReqDto.getBookName() != null
                || bookInfoReqDto.getBookIntro() != null
                || auditPublishStatus != null;


        if (!hasChange) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        BookInfoChange isExist = bookInfoChangeMapper.selectOne(
                new LambdaQueryWrapper<BookInfoChange>()
                        .select(BookInfoChange::getId)
                        .eq(BookInfoChange::getAuthorId, authorId)
                        .eq(BookInfoChange::getBookId, bookId)
                        .eq(BookInfoChange::getAuditStatus, CommonConstants.AUDIT_STATUS_AUDIT)
        );

        if (isExist != null) {
            int update = bookInfoChangeMapper.update(
                    new LambdaUpdateWrapper<BookInfoChange>()
                            .set(
                                    bookInfoReqDto.getBookName() != null,
                                    BookInfoChange::getBookName,
                                    bookInfoReqDto.getBookName()
                            )
                            .set(
                                    bookInfoReqDto.getBookIntro() != null,
                                    BookInfoChange::getBookIntro,
                                    bookInfoReqDto.getBookIntro()
                            )
                            .set(auditPublishStatus != null,
                                    BookInfoChange::getPublishStatus,
                                    auditPublishStatus)

                            .eq(BookInfoChange::getId, isExist.getId())
            );
            if (update != 1) {
                throw new BusinessException(ResultCodeEnum.FAIL, "申请失败");
            }
            return CommonResult.success();
        }

        BookInfoChange bookInfoChange = new BookInfoChange();
        bookInfoChange.setBookId(bookId);
        bookInfoChange.setAuthorId(authorId);
        bookInfoChange.setBookName(bookInfoReqDto.getBookName());
        bookInfoChange.setBookIntro(bookInfoReqDto.getBookIntro());
        bookInfoChange.setPublishStatus(auditPublishStatus);
        bookInfoChange.setAuditType(CommonConstants.AUDIT_TYPE_INFORMATION_CHANGE);

        int i = bookInfoChangeMapper.insert(bookInfoChange);
        if (i != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "申请失败");
        }
        return CommonResult.success();
    }

    @Override
    public CommonResult<Void> saveBookCoverUrl(Long bookId, MultipartFile file, String coverUrl) {
        boolean hasFile = file != null && !file.isEmpty();
        boolean hasCoverUrl = coverUrl != null && !coverUrl.isBlank();
        if (hasFile == hasCoverUrl) {
            throw new BusinessException(ResultCodeEnum.FAIL, "请选择一种图片上传方式");
        }
        Long authorId = authorAuthUtil.getCurrentAuthorId();
        validateBookOwnership(authorId, bookId);
        String novelCover;
        if (hasFile) {
            novelCover = ossUtil.upload(file, "novelCover");
        } else {
            novelCover = coverUrl;
        }
        String oldCoverUrl;
        try {
            oldCoverUrl = saveBookCoverChange(bookId, authorId, novelCover);
        } catch (RuntimeException e) {
            if (hasFile) {
                ossUtil.delete(novelCover);
            }
            throw e;
        }
        deleteOldCover(oldCoverUrl);
        return CommonResult.success();
    }

    @Override
    public CommonResult<PageRespDto<AuthorBookInfoAuditRespDto>> listBookInfoAudits(PageReqDto pageReqDto) {
        Long authorId = authorAuthUtil.getCurrentAuthorId();
        Page<AuthorBookInfoAuditRespDto> page = new Page<>(
                pageReqDto.getPageNum(),
                pageReqDto.getPageSize()
        );
        Page<AuthorBookInfoAuditRespDto> pageBookInfoChange = bookInfoChangeMapper.listBookInfoAudits(page, authorId);
        pageBookInfoChange.getRecords().forEach(
                bookInfo ->
                        bookInfo.setBookIntro(shortBookIntro(bookInfo.getBookIntro()))
        );
        return CommonResult.success(PageRespDto.of(pageBookInfoChange));
    }

    @Override
    public CommonResult<Void> updateAuthorBookInfo(
            Long bookId,
            AuthorBookInfoNotAuditReqDto bookInfoNotAuditReqDto
    ) {
        if (bookInfoNotAuditReqDto == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }
        Integer updateStatus = bookInfoNotAuditReqDto.getUpdateStatus();
        Integer publishStatus = bookInfoNotAuditReqDto.getPublishStatus();
        if (updateStatus == null && publishStatus == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }
        if (
                (updateStatus != null && (updateStatus < CommonConstants.UPDATE_STATUS_SERIALIZED ||
                        updateStatus > CommonConstants.UPDATE_STATUS_CLOSED)) ||
                        (publishStatus != null && !CommonConstants.PUBLISH_STATUS_UNDERCARRIAGE.equals(publishStatus))
        ) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }
        Long authorId = authorAuthUtil.getCurrentAuthorId();
        int i = bookMapper.update(
                new LambdaUpdateWrapper<BookInfo>()
                        .set(updateStatus != null, BookInfo::getUpdateStatus, updateStatus)
                        .set(publishStatus != null, BookInfo::getPublishStatus, publishStatus)
                        .eq(BookInfo::getId, bookId)
                        .eq(BookInfo::getAuthorId, authorId)
        );
        if (i != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "更新失败");
        }
        return CommonResult.success();
    }

    @Override
    public CommonResult<Void> saveCreateBookInfo(AuthorCreateBookReqDto createBookReqDto, MultipartFile file, String coverUrl) {
        if (createBookReqDto == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        Integer categoryId = createBookReqDto.getCategoryId();
        String bookName = createBookReqDto.getBookName();
        String bookIntro = createBookReqDto.getBookIntro();
        if (
                bookName == null ||
                        bookName.trim().isBlank() ||
                        bookIntro == null ||
                        bookIntro.isBlank() ||
                        categoryId == null
        ) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        BookCategory bookCategory = bookCategoryMapper.selectOne(
                new LambdaQueryWrapper<BookCategory>()
                        .select(BookCategory::getId)
                        .eq(BookCategory::getId, categoryId)
        );

        if (bookCategory == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "分类id不存在");
        }

        Long authorId = authorAuthUtil.getCurrentAuthorId();
        BookInfoChange isExist = bookInfoChangeMapper.selectOne(
                new LambdaQueryWrapper<BookInfoChange>()
                        .select(BookInfoChange::getId)
                        .eq(BookInfoChange::getAuthorId, authorId)
                        .eq(BookInfoChange::getBookName, bookName)
                        .eq(BookInfoChange::getAuditType, CommonConstants.AUDIT_TYPE_CREATE_WORK)
                        .eq(BookInfoChange::getAuditStatus, CommonConstants.AUDIT_STATUS_AUDIT)
        );

        if (isExist != null) {
            throw new BusinessException(ResultCodeEnum.FAIL, "不能重复创建");
        }

        boolean hasFile = file != null && !file.isEmpty();
        boolean hasCoverUrl = coverUrl != null && !coverUrl.isBlank();
        if (hasFile == hasCoverUrl) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID, "请选择一种图片上传方式");
        }

        // 上传图片
        String novelCover;
        if (hasFile) {
            novelCover = ossUtil.upload(file, "novelCover");
        } else {
            novelCover = coverUrl;
        }

        try {
            BookInfoChange bookInfoChange = new BookInfoChange();
            bookInfoChange.setBookName(bookName.trim());
            bookInfoChange.setBookIntro(bookIntro);
            bookInfoChange.setCategoryId(categoryId);
            bookInfoChange.setAuthorId(authorId);
            bookInfoChange.setAuditType(CommonConstants.AUDIT_TYPE_CREATE_WORK);
            bookInfoChange.setCoverUrl(novelCover);
            int insert = bookInfoChangeMapper.insert(bookInfoChange);

            if (insert != 1) {
                throw new BusinessException(ResultCodeEnum.FAIL, "创建失败");
            }

            return CommonResult.success();
        } catch (RuntimeException e) {
            try {
                ossUtil.delete(novelCover);
            } catch (RuntimeException deleteException) {
                log.warn("删除新建作品封面失败，coverUrl={}", novelCover, deleteException);
            }
            throw e;
        }
    }

    @Override
    public CommonResult<Void> deleteAuditInfo(Long auditId) {
        Long authorId = authorAuthUtil.getCurrentAuthorId();
        int i = bookInfoChangeMapper.delete(
                new LambdaQueryWrapper<BookInfoChange>()
                        .eq(BookInfoChange::getId, auditId)
                        .eq(BookInfoChange::getAuthorId, authorId)
                        .eq(BookInfoChange::getAuditStatus, CommonConstants.AUDIT_STATUS_AUDIT)
        );
        if (i != 1) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "审核信息不存在");
        }
        return CommonResult.success();
    }

    @Override
    public CommonResult<AuthorAuditInfoRespDto> getAuditInfo(Long auditId) {
        Long authorId = authorAuthUtil.getCurrentAuthorId();
        AuthorAuditInfoRespDto authorAuditInfoRespDto = bookInfoChangeMapper.getAuditInfo(auditId, authorId);
        if (authorAuditInfoRespDto == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "审核信息不存在");
        }
        return CommonResult.success(authorAuditInfoRespDto);
    }

    @Override
    public CommonResult<RecommendBookInfoRespDto> getRecommendBookInfo() {
        LocalDateTime now = LocalDateTime.now();
        RecommendBookInfoRespDto respDto = bookMapper.getRecommendBookInfo(now);
        if (respDto == null) {
            return CommonResult.success(null);
        }
        respDto.setBookIntro(shortBookIntro(respDto.getBookIntro()));
        return CommonResult.success(respDto);
    }

    /**
     * 验证小说是否属于当前作者
     *
     * @param authorId 作家id
     * @param bookId   小说id
     */
    private void validateBookOwnership(Long authorId, Long bookId) {
        if (authorId == null || bookId == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        BookInfo bookInfo = bookMapper.selectOne(
                new LambdaQueryWrapper<BookInfo>()
                        .select(BookInfo::getId)
                        .eq(BookInfo::getId, bookId)
                        .eq(BookInfo::getAuthorId, authorId)
        );
        if (bookInfo == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "小说信息不存在");
        }
    }

    /**
     * 保存变更小说封面
     *
     * @param bookId     小说id
     * @param authorId   作家id
     * @param novelCover 小说封面链接
     * @return 小说变更旧封面
     */
    private String saveBookCoverChange(Long bookId, Long authorId, String novelCover) {
        BookInfoChange isExist = bookInfoChangeMapper.selectOne(
                new LambdaQueryWrapper<BookInfoChange>()
                        .select(
                                BookInfoChange::getId,
                                BookInfoChange::getCoverUrl
                        )
                        .eq(BookInfoChange::getBookId, bookId)
                        .eq(BookInfoChange::getAuthorId, authorId)
                        .eq(BookInfoChange::getAuditStatus, CommonConstants.AUDIT_STATUS_AUDIT)
        );
        BookInfoChange bookInfoChange = new BookInfoChange();
        bookInfoChange.setCoverUrl(novelCover);
        if (isExist != null) {
            bookInfoChange.setId(isExist.getId());
            int i = bookInfoChangeMapper.updateById(bookInfoChange);
            if (i != 1) {
                throw new BusinessException(ResultCodeEnum.FAIL, "申请失败");
            }
            return isExist.getCoverUrl();
        }
        bookInfoChange.setBookId(bookId);
        bookInfoChange.setAuthorId(authorId);
        int insert = bookInfoChangeMapper.insert(bookInfoChange);
        if (insert != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "申请失败");
        }
        return null;
    }

    /**
     * 删除小说旧封面
     *
     * @param oldCoverUrl 旧封面链接
     */
    private void deleteOldCover(String oldCoverUrl) {
        if (oldCoverUrl != null) {
            try {
                ossUtil.delete(oldCoverUrl);
            } catch (RuntimeException e) {
                log.warn("删除旧小说封面失败，oldCoverUrl={}", oldCoverUrl, e);
            }
        }
    }

    /**
     * 压缩小说简介
     *
     * @param bookIntro 小说简介
     * @return 压缩后的小说简介
     */
    private String shortBookIntro(String bookIntro) {
        if (bookIntro == null || bookIntro.isBlank() || bookIntro.length() <= 50) {
            return bookIntro;
        }
        return bookIntro.substring(0, 50) + "……";
    }
}
