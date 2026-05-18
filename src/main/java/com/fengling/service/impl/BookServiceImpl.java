package com.fengling.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import com.fengling.common.util.RedisUtil;
import com.fengling.entity.BookInfo;
import com.fengling.entity.BookInfoChange;
import com.fengling.entity.BookShelf;
import com.fengling.entity.dto.*;
import com.fengling.mapper.BookInfoChangeMapper;
import com.fengling.mapper.BookMapper;
import com.fengling.mapper.BookShelfMapper;
import com.fengling.mapper.ChapterMapper;
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

        AuthUserInfo authUserInfo = UserContext.getAuthUserInfo();
        if (authUserInfo != null) {
            //设置小说最后阅读到的章节id和最后阅读时间
            bookShelfMapper.update(
                    new BookShelf()
                            .setLastReadChapterId(chapterId)
                            .setLastReadTime(LocalDateTime.now()),
                    new LambdaQueryWrapper<BookShelf>()
                            .eq(BookShelf::getUserId, authUserInfo.getUserId())
                            .eq(BookShelf::getBookId, bookId)
            );
        }

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
        boolean hasChange = bookInfoReqDto != null
                && (bookInfoReqDto.getBookName() != null
                || bookInfoReqDto.getBookIntro() != null);

        if (!hasChange) {
            throw new BusinessException(ResultCodeEnum.FAIL, "变更信息不能为空");
        }
        Long authorId = authorAuthUtil.getCurrentAuthorId();
        BookInfo bookInfo = bookMapper.selectOne(
                new LambdaQueryWrapper<BookInfo>()
                        .select(BookInfo::getId)
                        .eq(BookInfo::getId, bookId)
                        .eq(BookInfo::getAuthorId, authorId)
        );
        if (bookInfo == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "小说信息不存在");
        }
        BookInfoChange isExist = bookInfoChangeMapper.selectOne(
                new LambdaQueryWrapper<BookInfoChange>()
                        .select(BookInfoChange::getId)
                        .eq(BookInfoChange::getAuthorId, authorId)
                        .eq(BookInfoChange::getBookId, bookId)
                        .eq(BookInfoChange::getAuditStatus, CommonConstants.AUDIT_STATUS_AUDIT)
        );

        if (isExist != null) {
            bookInfoChangeMapper.update(
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
                            .eq(BookInfoChange::getId, isExist.getId())
            );
            return CommonResult.success();
        }
        BookInfoChange bookInfoChange = BeanUtil.copyProperties(bookInfoReqDto, BookInfoChange.class);
        bookInfoChange.setBookId(bookId);
        bookInfoChange.setAuthorId(authorId);
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
        BookInfo bookInfo = bookMapper.selectOne(
                new LambdaQueryWrapper<BookInfo>()
                        .select(BookInfo::getId)
                        .eq(BookInfo::getId, bookId)
                        .eq(BookInfo::getAuthorId, authorId)
        );
        if (bookInfo == null) {
            throw new BusinessException(ResultCodeEnum.FAIL, "小说信息不存在");
        }
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
        Page<AuthorBookInfoAuditRespDto> pageBookInfoChangeMapper = bookInfoChangeMapper.listBookInfoAudits(page, authorId);
        return CommonResult.success(PageRespDto.of(pageBookInfoChangeMapper));
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
