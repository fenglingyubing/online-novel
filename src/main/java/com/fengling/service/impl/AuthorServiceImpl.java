package com.fengling.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.common.constant.CacheConstants;
import com.fengling.common.constant.CommonConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.exception.BusinessException;
import com.fengling.common.resp.CommonResult;
import com.fengling.common.util.*;
import com.fengling.entity.AuthorInfo;
import com.fengling.entity.BookInfo;
import com.fengling.entity.UserInfo;
import com.fengling.entity.dto.*;
import com.fengling.mapper.AuthorMapper;
import com.fengling.mapper.BookMapper;
import com.fengling.mapper.ChapterMapper;
import com.fengling.mapper.UserMapper;
import com.fengling.service.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorMapper authorMapper;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RegisterUtil registerUtil;
    private final JWTUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final BookMapper bookMapper;
    private final AuthorAuthUtil authorAuthUtil;
    private final ChapterMapper chapterMapper;
    private final PageAuthUtil pageAuthUtil;

    @Transactional
    @Override
    public CommonResult<UserAuthRespDto> authorRegister(AuthorReqDto authorReqDto) {
        if (authorReqDto == null) {
            throw new BusinessException(ResultCodeEnum.FAIL);
        }
        //判断用户名是否存在
        String username = authorReqDto.getUsername();
        String password = authorReqDto.getPassword();
        String authorName = authorReqDto.getAuthorName();
        if (username == null || username.isBlank()) {
            throw new BusinessException(ResultCodeEnum.FAIL, "用户名为空");
        }
        if (password == null || password.isBlank()) {
            throw new BusinessException(ResultCodeEnum.FAIL, "密码为空");
        }
        if (authorName == null || authorName.isBlank()) {
            throw new BusinessException(ResultCodeEnum.FAIL, "作者名为空");
        }

        UserInfo one = registerUtil.getUserInfoByUserName(username);
        if (one != null) {
            throw new BusinessException(ResultCodeEnum.USERNAME_EXIST);
        }

        // 判断作者名是否存在
        AuthorInfo authorInfo = authorMapper.selectOne(
                new LambdaQueryWrapper<AuthorInfo>()
                        .eq(AuthorInfo::getAuthorName, authorName)
        );
        if (authorInfo != null) {
            throw new BusinessException(ResultCodeEnum.FAIL, "作者名已存在");
        }

        UserInfo registerUser = new UserInfo();
        registerUser.setUsername(username);
        //加密密码
        registerUser.setPassword(passwordEncoder.encode(password));
        registerUser.setUserRole(CommonConstants.USER_ROLE_AUTHOR);
        registerUser.setNickName(registerUtil.generateNickname());
        registerUser.setUserStatus(CommonConstants.USER_STATUS_NORMAL);
        registerUser.setUserBalance(CommonConstants.USER_DEFAULT_BALANCE);
        int insert = userMapper.insert(registerUser);
        if (insert != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "注册失败");
        }
        Long userId = registerUser.getId();
        int author = authorMapper.insert(new AuthorInfo(
                authorName,
                userId
        ));
        if (author != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "注册失败");
        }
        // 生成JWT令牌
        String jwtToken = jwtUtil.createJwtToken(userId, registerUser.getUserRole());
        // 将JWT令牌放到Redis
        String key = CacheConstants.AUTH_TOKEN + userId;
        redisUtil.addRedisCache(key, jwtToken, jwtUtil.getTtl());
        UserAuthRespDto userAuthRespDto = BeanUtil.copyProperties(
                registerUser,
                UserAuthRespDto.class
        );
        userAuthRespDto.setToken(jwtToken);
        return CommonResult.success(userAuthRespDto);
    }

    @Override
    public CommonResult<AuthorHomeRespDto> getAuthorHomeInfo() {
        UserInfoDto userInfoDto = authorAuthUtil.authorAuth();
        AuthorHomeRespDto authorHomeRespDto = authorMapper.getAuthorHomeInfo(userInfoDto.getId());
        if (authorHomeRespDto == null) {
            throw new BusinessException(ResultCodeEnum.FAIL, "没有这个作者信息");
        }
        BookInfo bookInfo = bookMapper.selectOne(
                new LambdaQueryWrapper<BookInfo>()
                        .select(
                                BookInfo::getId,
                                BookInfo::getBookName,
                                BookInfo::getCoverUrl,
                                BookInfo::getWordCount,
                                BookInfo::getLatestChapterName,
                                BookInfo::getLastChapterTime
                        )
                        .eq(BookInfo::getAuthorId, authorHomeRespDto.getId())
                        .orderByDesc(BookInfo::getLastChapterTime)
                        .last("limit 1")
        );
        if (bookInfo != null) {
            AuthorRecentNovelRespDto recentNovelRespDto = BeanUtil.copyProperties(
                    bookInfo,
                    AuthorRecentNovelRespDto.class
            );
            authorHomeRespDto.setRecentNovelRespDto(recentNovelRespDto);
        }
        return CommonResult.success(authorHomeRespDto);
    }

    @Override
    public CommonResult<PageRespDto<AuthorNovelsListRespDto>> listAuthorNovelsList(PageReqDto pageReqDto) {
        pageAuthUtil.pageAuth(pageReqDto);
        UserInfoDto userInfoDto = authorAuthUtil.authorAuth();
        Long pageNum = pageReqDto.getPageNum();
        Long pageSize = pageReqDto.getPageSize();
        String key = CacheConstants.WORKS + userInfoDto.getId() + ":" + pageNum + ":" + pageSize;
        // 从redis获取
        String jsonStr = redisUtil.getValueForKey(key);
        if (jsonStr != null && !jsonStr.isBlank()) {
            PageRespDto<AuthorNovelsListRespDto> respDto = JSONUtil.toBean(
                    jsonStr,
                    new TypeReference<>() {
                    },
                    false
            );
            return CommonResult.success(respDto);
        }
        AuthorInfo authorInfo = authorMapper.selectOne(
                new LambdaQueryWrapper<AuthorInfo>()
                        .select(AuthorInfo::getId)
                        .eq(AuthorInfo::getUserId, userInfoDto.getId())
        );
        if (authorInfo == null) {
            throw new BusinessException(ResultCodeEnum.FORBIDDEN, "当前用户不是作家");
        }
        Page<AuthorNovelsListRespDto> page = new Page<>(
                pageNum,
                pageSize
        );
        Page<AuthorNovelsListRespDto> pageAuthorNovels = bookMapper.listAuthorNovelsList(
                page,
                authorInfo.getId()
        );
        // 缓存到redis
        PageRespDto<AuthorNovelsListRespDto> pageRespDto = PageRespDto.of(pageAuthorNovels);
        String valueStr = JSONUtil.toJsonStr(pageRespDto);
        redisUtil.addRedisCache(
                key,
                valueStr,
                CacheConstants.WORKS_TTL
        );
        return CommonResult.success(pageRespDto);
    }

    @Override
    public CommonResult<PageRespDto<AuthorDraftsRespDto>> listAuthorDrafts(PageReqDto page) {
        UserInfoDto userInfoDto = authorAuthUtil.authorAuth();
        // 查询作家下的全部小说id
        AuthorInfo authorInfo = authorMapper.selectOne(
                new LambdaQueryWrapper<AuthorInfo>()
                        .select(AuthorInfo::getId)
                        .eq(AuthorInfo::getUserId, userInfoDto.getId())
        );
        if (authorInfo == null) {
            throw new BusinessException(ResultCodeEnum.FORBIDDEN);
        }
        pageAuthUtil.pageAuth(page);
        Long pageNum = page.getPageNum();
        Long pageSize = page.getPageSize();
        List<BookInfo> bookInfos = bookMapper.selectList(
                new LambdaQueryWrapper<BookInfo>()
                        .select(BookInfo::getId)
                        .eq(BookInfo::getAuthorId, authorInfo.getId())
        );
        if (bookInfos.isEmpty()) {
            Page<AuthorDraftsRespDto> emptyPage = new Page<>(pageNum, pageSize);
            return CommonResult.success(PageRespDto.of(emptyPage));
        }
        List<Long> bookIdList = bookInfos.stream().map(BookInfo::getId).toList();

        // 根据小说id查询
        Page<AuthorDraftsRespDto> draftsPage = new Page<>(pageNum, pageSize);
        Page<AuthorDraftsRespDto> chaptersPage = chapterMapper.listAuthorDrafts(draftsPage, bookIdList);
        chaptersPage.getRecords().forEach(
                chapter -> chapter.setChapterContent(
                        shortContent(chapter.getChapterContent())
                )
        );
        return CommonResult.success(PageRespDto.of(chaptersPage));
    }

    @Override
    public CommonResult<List<AuthorEditBookListResp>> listAuthorEditBook() {
        UserInfoDto userInfoDto = authorAuthUtil.authorAuth();
        AuthorInfo authorInfo = authorMapper.selectOne(
                new LambdaQueryWrapper<AuthorInfo>()
                        .select(AuthorInfo::getId)
                        .eq(AuthorInfo::getUserId, userInfoDto.getId())
        );
        if (authorInfo == null) {
            throw new BusinessException(ResultCodeEnum.FORBIDDEN);
        }
        List<BookInfo> bookInfos = bookMapper.selectList(
                new LambdaQueryWrapper<BookInfo>()
                        .select(BookInfo::getId, BookInfo::getBookName)
                        .eq(BookInfo::getAuthorId, authorInfo.getId())
                        .orderByDesc(BookInfo::getUpdateTime)
        );
        List<AuthorEditBookListResp> bookList = bookInfos.stream()
                .map(
                        bookInfo -> new AuthorEditBookListResp(
                                bookInfo.getId(),
                                bookInfo.getBookName()
                        )
                ).toList();
        return CommonResult.success(bookList);
    }

    /**
     * 压缩小说正文
     *
     * @param content 小说正文
     * @return 压缩后的小说正文
     */
    private String shortContent(String content) {
        if (content == null || content.isBlank() || content.length() <= 100) {
            return content;
        }
        return content.substring(0, 99) + "……";
    }
}
