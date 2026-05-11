# 接口文档
## 结果码
- 200 操作成功
- 500 操作失败
- 401 未登录或登录已失效
- 1001 用户名已存在
- 1002 用户名或密码错误
- 1003 用户不存在

## 注册接口
```text
请求路径：
/api/user/register
请求：
    POST
参数：
    username
    password
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": {
            "id": 2052757804000399362,
            "status": 0,
            "userRole": 1,
            "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMDUzMDI2NDU5ODA4NDA3NTUzIiwidXNlcklkIjoyMDUzMDI2NDU5ODA4NDA3NTUzLCJ1c2VyUm9sZSI6MSwiaWF0IjoxNzc4MzE0NjI2LCJleHAiOjE3NzgzMjE4MjZ9.3v26ZA2RnSjaFwrNye_DDVwKvYCtDxcEvREGXQmr4f4"
        }
    }
    id -> 用户id
    status -> 用户状态 （0-正常，1-禁用）
    userRole -> 用户角色（0-管理员，1-读者，2-作家）
    token -> JWT令牌
```

## 登录接口
```text
请求路径：
/api/user/login
请求：
    POST
参数：
    username
    password
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": {
            "id": 2052757804000399362,
            "status": 0,
            "userRole": 1,
            "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMDUzMDI2NDU5ODA4NDA3NTUzIiwidXNlcklkIjoyMDUzMDI2NDU5ODA4NDA3NTUzLCJ1c2VyUm9sZSI6MSwiaWF0IjoxNzc4MzE0NjI2LCJleHAiOjE3NzgzMjE4MjZ9.3v26ZA2RnSjaFwrNye_DDVwKvYCtDxcEvREGXQmr4f4"
        }
    }
    id -> 用户id
    status -> 用户状态 （0-正常，1-禁用）
    userRole -> 用户角色（0-管理员，1-读者，2-作家）
    token -> JWT令牌
```

## 首页小说分类接口
```text
请求路径：
/api/novel/category/list
请求方式：
    GET
参数：
    无
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": [
            {
                "id": 1,
                "categoryName": "玄幻"
            },
            {
                "id": 2,
                "categoryName": "奇幻"
            },
            ....
        ]
    }
    id -> 小说分类id
    categoryName -> 小说分类名称
```

## 某分类下小说列表查询
```text
请求路径：
/api/novel/{categoryId}/list?pageNum=1&pageSize=5
请求方式：
    GET
参数：
    categoryId -> 小说分类id
    pageNum -> 当前是第几页
    pageSize -> 每页有多少条数据
响应数据：
    {
    "code": 200,
    "message": "操作成功",
    "data": {
        "records": [
            {
                "id": 1,
                "bookName": "碧阳仙门",
                "coverUrl": "https://bookcover.yuewen.com/qdbimg/349573/1048992740/600.webp",
                "authorName": "鹤守月满池",
                "categoryId": 1,
                "updateStatus": 0,
                "bookIntro": "自从天道定鼎，仙释共分万国。 仙门称碧阳，赤释作妙……",
                "wordCount": 37200,
                "latestChapterId": 9,
                "latestChapterName": "第九章 装逼"
            },
            {
                "id": 2,
                "bookName": "修仙界唯一出马仙",
                "coverUrl": "https://bookcover.yuewen.com/qdbimg/349573/1048721558/600.webp",
                "authorName": "尼禄2077",
                "categoryId": 1,
                "updateStatus": 0,
                "bookIntro": "雨夜，高速，半挂卡车。 没有奥丁，也没有尼伯龙根这……",
                "wordCount": 244300,
                "latestChapterId": 39,
                "latestChapterName": "第30章 受够了"
            },
            ...
        ],
        "total": 20,
        "pageNum": 1,
        "pageSize": 5,
        "pages": 4
    }
}
id -> 小说id
bookName -> 小说名称
coverUrl -> 小说封面链接
authorName -> 作者名字
categoryId -> 小说分类id
updateStatus -> 更新状态（0-连载中，1-已完结）
bookIntro -> 小说简介
wordCount -> 小说字数
latestChapterId -> 最新章节id
latestChapterName -> 最新章节名
total -> 一共有多少条数据
pageNum -> 当前是第几页
pageSize -> 当前页有多少条数据
pages -> 一共有几页
```
## 小说详情页查询
```text
请求路径：
/api/novel/{bookId}
请求方式：
    GET
参数：
    bookId -> 小说id
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": {
            "id": 1,
            "bookName": "碧阳仙门",
            "coverUrl": "https://bookcover.yuewen.com/qdbimg/349573/1048992740/600.webp",
            "authorName": "鹤守月满池",
            "categoryName": "玄幻",
            "updateStatus": 0,
            "bookIntro": "自从天道定鼎，仙释共分万国。仙门称碧阳，赤释作妙土。",
            "chapterCount": 9,
            "wordCount": 37200,
            "latestChapterId": 9,
            "latestChapterName": "第九章 装逼",
            "lastChapterTime": "2026-05-11T12:00:00",
            "chapterList": [
                {
                    "id": 1,
                    "chapterName": "第一章 碧阳仙门"
                },
                {
                    "id": 2,
                    "chapterName": "第二章 入门"
                },
                ...
            ]
        }
    }
id -> 小说id
bookName -> 小说名称
coverUrl -> 小说封面链接
authorName -> 作者名字
categoryName -> 小说分类名称
updateStatus -> 更新状态（0-连载中，1-已完结）
bookIntro -> 小说简介
chapterCount -> 小说章节数
wordCount -> 小说字数
latestChapterId -> 最新章节id
latestChapterName -> 最新章节名
lastChapterTime -> 最新章节更新时间
chapterList -> 章节目录列表
chapterList.id -> 章节id
chapterList.chapterName -> 章节名称
```

## 小说正文查询
```text
请求路径：
/api/novel/{bookId}/chapter/{chapterId}
请求方式：
    GET
参数：
    bookId -> 小说id
    chapterId -> 章节id
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": {
            "id": 1,
            "bookId": 1,
            "chapterNum": 1,
            "chapterName": "第一章 碧阳仙门",
            "chapterContent": "自从天道定鼎，仙释共分万国。仙门称碧阳，赤释作妙土。",
            "preChapterId": null,
            "nextChapterId": 2
        }
    }
id -> 章节id
bookId -> 小说id
chapterNum -> 章节序号
chapterName -> 章节名称
chapterContent -> 章节正文
preChapterId -> 上一章id，没有上一章时为null
nextChapterId -> 下一章id，没有下一章时为null
```
