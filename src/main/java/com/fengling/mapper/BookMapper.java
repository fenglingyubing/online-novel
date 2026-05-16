package com.fengling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.entity.BookInfo;
import com.fengling.entity.dto.AuthorNovelsListRespDto;
import com.fengling.entity.dto.BookInfoRespDto;
import com.fengling.entity.dto.BookListRespDto;
import com.fengling.entity.dto.BookRecentListRespDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookMapper extends BaseMapper<BookInfo> {
    /**
     * 查询某分类下的小说列表（分页）
     *
     * @param categoryId 分类id
     * @param page       分页请求对象
     * @return 分页对象
     */
    Page<BookListRespDto> selectCategoryNovelPage(Page<BookListRespDto> page,
                                                  @Param("categoryId") Integer categoryId);

    /**
     * 查询全部小说
     *
     * @param page 分页对象
     * @return 全部小说列表
     */
    Page<BookListRespDto> selectAllNovelPage(Page<BookListRespDto> page);

    /**
     * 查询小说详情
     *
     * @param bookId 小说id
     * @return BookInfoRespDto 小说详情响应实体
     */
    BookInfoRespDto getBookInfoById(Long bookId);

    /**
     * 查询最新上架小说
     *
     * @param limit 首页最新上架小说展示数量
     * @return 最新上架小说列表
     */
    List<BookRecentListRespDto> listRecentBookList(@Param("limit") Integer limit);

    /**
     * 查询作家的所有作品
     *
     * @param page 分页对象
     * @param authorId   作家id
     * @return 作家作品管理页面响应结果
     */
    Page<AuthorNovelsListRespDto> listAuthorNovelsList(
            Page<AuthorNovelsListRespDto> page,
            @Param("authorId") Long authorId
    );
}
