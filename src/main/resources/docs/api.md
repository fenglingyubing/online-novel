# 接口文档
## 结果码
- 200 操作成功
- 500 操作失败
- 401 未登录或登录已失效
- 1001 用户名已存在
- 1002 用户名或密码错误
- 1003 用户不存在

## 登录认证说明
```text
强制登录接口必须在请求头中携带JWT令牌：
    Authorization: Bearer token值

当前强制登录拦截的接口：
    /api/shelf/**
    /api/user/logout
    /api/user/mine
    /api/user/updateinfo
    /api/user/uploadphoto

当前强制登录的功能：
    查询书架小说列表
    添加小说到书架
    退出登录
    查询我的页面用户信息
    修改我的页面用户信息
    上传用户头像

当前可选登录解析的接口：
    /api/novel/{bookId}/chapter/{chapterId}

当前可选登录的功能：
    小说正文查询

认证失败响应：
    {
        "code": 401,
        "message": "未登录或登录已失效",
        "data": null
    }

说明：
    1. token由注册或登录接口返回
    2. Authorization必须以"Bearer "开头
    3. Bearer后面需要跟一个空格，再拼接token值
    4. 用户id由后端从token中解析，前端不需要传userId
    5. 强制登录接口未携带token或token无效会返回401
    6. 可选登录接口未携带token仍可访问；携带有效token时，后端会识别当前用户
```

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
            "userStatus": 0,
            "userRole": 1,
            "userBalance": 0,
            "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMDUzMDI2NDU5ODA4NDA3NTUzIiwidXNlcklkIjoyMDUzMDI2NDU5ODA4NDA3NTUzLCJ1c2VyUm9sZSI6MSwiaWF0IjoxNzc4MzE0NjI2LCJleHAiOjE3NzgzMjE4MjZ9.3v26ZA2RnSjaFwrNye_DDVwKvYCtDxcEvREGXQmr4f4"
        }
    }
    id -> 用户id
    userStatus -> 用户状态（0-正常，1-禁用）
    userRole -> 用户角色（0-管理员，1-读者，2-作家）
    userBalance -> 用户书币余额
    token -> JWT令牌
```

## 作家注册接口
```text
请求路径：
/api/author/register
请求方式：
    POST
参数：
    username -> 用户名
    password -> 密码
    authorName -> 作者笔名
请求体示例：
    {
        "username": "author001",
        "password": "123456",
        "authorName": "风铃"
    }
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": {
            "id": 2052757804000399362,
            "userStatus": 0,
            "userRole": 2,
            "userBalance": 0,
            "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMDUzMDI2NDU5ODA4NDA3NTUzIiwidXNlcklkIjoyMDUzMDI2NDU5ODA4NDA3NTUzLCJ1c2VyUm9sZSI6MiwiaWF0IjoxNzc4MzE0NjI2LCJleHAiOjE3NzgzMjE4MjZ9.xxx"
        }
    }
    id -> 用户id
    userStatus -> 用户状态（0-正常，1-禁用）
    userRole -> 用户角色（0-管理员，1-读者，2-作家）
    userBalance -> 用户书币余额
    token -> JWT令牌
说明：
    1. 作家注册成功后会同时创建用户信息和作者信息
    2. 注册成功后会返回JWT令牌，前端可用于后续登录认证
    3. 用户名不能重复
    4. 作者笔名不能重复
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
            "userStatus": 0,
            "userRole": 1,
            "userBalance": 0,
            "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMDUzMDI2NDU5ODA4NDA3NTUzIiwidXNlcklkIjoyMDUzMDI2NDU5ODA4NDA3NTUzLCJ1c2VyUm9sZSI6MSwiaWF0IjoxNzc4MzE0NjI2LCJleHAiOjE3NzgzMjE4MjZ9.3v26ZA2RnSjaFwrNye_DDVwKvYCtDxcEvREGXQmr4f4"
        }
    }
    id -> 用户id
    userStatus -> 用户状态（0-正常，1-禁用）
    userRole -> 用户角色（0-管理员，1-读者，2-作家）
    userBalance -> 用户书币余额
    token -> JWT令牌
```

## 退出登录接口
```text
请求路径：
/api/user/logout
请求方式：
    POST
请求头：
    Authorization: Bearer token值
参数：
    无
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 后端会清除Redis中当前token对应的缓存
    3. 退出成功后，当前token将失效
```

## 我的页面用户信息查询
```text
请求路径：
/api/user/mine
请求方式：
    GET
请求头：
    Authorization: Bearer token值
参数：
    无
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": {
            "userId": 2052767332125331457,
            "userSex": 0,
            "nickName": "reader_123456",
            "userRole": 1,
            "userPhoto": "https://xxx.com/avatar.png",
            "userBalance": 0,
            "userStatus": 0
        }
    }
userId -> 用户id
userSex -> 用户性别（0-男，1-女）
nickName -> 用户昵称
userRole -> 用户角色（0-管理员，1-读者，2-作家）
userPhoto -> 用户头像
userBalance -> 用户书币余额
userStatus -> 用户状态（0-正常，1-禁用）
说明：
    1. 该接口需要登录后调用
    2. 用户id由后端从token中解析，前端不需要传userId
```

## 我的页面用户信息修改
```text
请求路径：
/api/user/updateinfo
请求方式：
    PUT
请求头：
    Authorization: Bearer token值
参数：
    userSex -> 用户性别
    nickName -> 用户昵称
    userPhoto -> 用户头像
请求体示例：
    {
        "userSex": 0,
        "nickName": "reader_654321",
        "userPhoto": "https://xxx.com/avatar.png"
    }
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 用户id由后端从token中解析，前端不需要传userId
    3. 只会修改请求体中不为null的字段
```

## 用户头像上传
```text
请求路径：
/api/user/uploadphoto
请求方式：
    POST
请求头：
    Authorization: Bearer token值
参数：
    file -> 本地图片文件
    imageUrl -> 图片链接
参数说明：
    1. file和imageUrl必须二选一
    2. file用于上传本地图片，使用multipart/form-data提交
    3. imageUrl用于直接保存图片链接，使用Query参数提交
    4. imageUrl必须以http://或https://开头
    5. 如果更新成功，后端会删除当前用户原来的OSS头像文件
本地文件上传示例：
    Content-Type: multipart/form-data
    file: 选择本地图片文件
图片链接上传示例：
    /api/user/uploadphoto?imageUrl=https://xxx.com/avatar.png
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": {
            "userPhoto": "https://xxx.com/avatar.png"
        }
    }
userPhoto -> 更新后的用户头像链接
说明：
    1. 该接口需要登录后调用
    2. 用户id由后端从token中解析，前端不需要传userId
    3. file和imageUrl不能同时传，也不能同时为空
    4. 文件上传仅支持jpg、jpeg、png、webp格式图片
    5. 如果数据库更新失败，本次新上传到OSS的图片会被删除
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
            "isShelf": true,
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
isShelf -> 是否已加入当前登录用户书架（true-已加入，false-未加入）
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
请求头：
    Authorization: Bearer token值（可选，登录用户携带后会更新书架阅读进度）
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
说明：未登录用户可直接访问正文；登录用户携带有效token访问时，会自动更新书架中的lastReadChapterId和lastReadTime
```

## 书架小说列表查询
```text
请求路径：
/api/shelf/list?pageNum=1&pageSize=10
请求方式：
    GET
请求头：
    Authorization: Bearer token值
参数：
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
                    "userId": 2052767332125331457,
                    "bookId": 1,
                    "bookName": "碧阳仙门",
                    "coverUrl": "https://bookcover.yuewen.com/qdbimg/349573/1048992740/600.webp",
                    "lastChapterNum": 1,
                    "chapterCount": 9,
                    "lastReadChapterId": 1,
                    "lastReadTime": "2026-05-11T12:00:00"
                },
                {
                    "id": 2,
                    "userId": 2052767332125331457,
                    "bookId": 2,
                    "bookName": "修仙界唯一出马仙",
                    "coverUrl": "https://bookcover.yuewen.com/qdbimg/349573/1048721558/600.webp",
                    "lastChapterNum": null,
                    "chapterCount": 39,
                    "lastReadChapterId": null,
                    "lastReadTime": null
                }
            ],
            "total": 2,
            "pageNum": 1,
            "pageSize": 10,
            "pages": 1
        }
    }
id -> 书架id
userId -> 用户id，由登录令牌解析得到
bookId -> 小说id
bookName -> 小说名称
coverUrl -> 小说封面链接
lastChapterNum -> 上次阅读到第几章，未阅读时为null
chapterCount -> 小说总章节数
lastReadChapterId -> 上次阅读章节id，未阅读时为null
lastReadTime -> 上次阅读时间，未阅读时为null
total -> 一共有多少条数据
pageNum -> 当前是第几页
pageSize -> 当前页有多少条数据
pages -> 一共有几页
```

## 添加小说到书架
```text
请求路径：
/api/shelf/{bookId}
请求方式：
    POST
请求头：
    Authorization: Bearer token值
参数：
    bookId -> 小说id
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": null
    }
```

## 删除书架小说
```text
请求路径：
/api/shelf
请求方式：
    DELETE
请求头：
    Authorization: Bearer token值
参数：
    bookIdList -> 小说id列表
请求体示例：
    {
        "bookIdList": [1, 2, 3]
    }
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 用户id由后端从token中解析，前端不需要传userId
    3. 只会删除当前登录用户书架中bookIdList对应的小说记录
    4. 如果列表中的小说不在当前用户书架中，不会产生影响
```
