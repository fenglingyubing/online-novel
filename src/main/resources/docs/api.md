# 接口文档
## 结果码
- 200 操作成功
- 500 操作失败
- 401 未登录或登录已失效
- 403 无权限访问
- 404 资源未找到
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
    /api/author/home
    /api/author/novels
    /api/author/{bookId}
    /api/author/{bookId}/uploadcover
    /api/author/create
    /api/author/bookinfo/audit
    /api/author/bookinfo/audit/{auditId}
    /api/author/drafts
    /api/author/audit/list
    /api/author/edit/booklist
    /api/author/{bookId}/chapters
    /api/author/{bookId}/chapters/{chapterId}
    /api/author/{bookId}/chapters/{chapterId}/cancel
    /api/admin/list
    /api/admin/list/create
    /api/admin/list/chapters

当前强制登录的功能：
    查询书架小说列表
    添加小说到书架
    退出登录
    查询我的页面用户信息
    修改我的页面用户信息
    上传用户头像
    查询作家首页信息
    查询作家作品管理页面列表
    查询作家某本小说详情
    提交小说信息变更审核
    更新作家小说状态
    提交小说封面变更审核
    提交作家新建作品审核
    查询小说变更信息审核列表
    查询小说变更信息审核详情
    删除小说变更信息待审核记录
    查询作家草稿箱列表
    查询作家审核章节列表
    查询作家编辑页小说列表
    新增作家章节信息
    查询作家某本小说的某个章节信息
    撤回审核中的章节
    管理员查询小说变更审核列表
    管理员查询新书审核列表
    管理员查询章节审核列表

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
            "userSex": null,
            "nickName": "reader_123456",
            "userPhoto": null,
            "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMDUzMDI2NDU5ODA4NDA3NTUzIiwidXNlcklkIjoyMDUzMDI2NDU5ODA4NDA3NTUzLCJ1c2VyUm9sZSI6MSwiaWF0IjoxNzc4MzE0NjI2LCJleHAiOjE3NzgzMjE4MjZ9.3v26ZA2RnSjaFwrNye_DDVwKvYCtDxcEvREGXQmr4f4"
        }
    }
    id -> 用户id
    userStatus -> 用户状态（0-正常，1-禁用）
    userRole -> 用户角色（0-管理员，1-读者，2-作家）
    userBalance -> 用户书币余额
    userSex -> 用户性别（0-男，1-女，未设置时为null）
    nickName -> 用户昵称
    userPhoto -> 用户头像，未设置时为null
    token -> JWT令牌
说明：注册成功后会返回首页展示所需的用户公共信息，前端可直接使用nickName、userPhoto、userBalance渲染登录态
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
            "userSex": null,
            "nickName": "reader_123456",
            "userPhoto": null,
            "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMDUzMDI2NDU5ODA4NDA3NTUzIiwidXNlcklkIjoyMDUzMDI2NDU5ODA4NDA3NTUzLCJ1c2VyUm9sZSI6MiwiaWF0IjoxNzc4MzE0NjI2LCJleHAiOjE3NzgzMjE4MjZ9.xxx"
        }
    }
    id -> 用户id
    userStatus -> 用户状态（0-正常，1-禁用）
    userRole -> 用户角色（0-管理员，1-读者，2-作家）
    userBalance -> 用户书币余额
    userSex -> 用户性别（0-男，1-女，未设置时为null）
    nickName -> 用户昵称
    userPhoto -> 用户头像，未设置时为null
    token -> JWT令牌
说明：
    1. 作家注册成功后会同时创建用户信息和作者信息
    2. 注册成功后会返回JWT令牌，前端可用于后续登录认证
    3. 用户名不能重复
    4. 作者笔名不能重复
```

## 作家首页信息查询
```text
请求路径：
/api/author/home
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
            "id": 1,
            "authorName": "风铃",
            "userPhoto": "https://xxx.com/avatar.png",
            "wordCount": 37200,
            "recentNovelRespDto": {
                "id": 1,
                "bookName": "碧阳仙门",
                "coverUrl": "https://bookcover.yuewen.com/qdbimg/349573/1048992740/600.webp",
                "wordCount": 37200,
                "latestChapterName": "第九章 装逼",
                "lastChapterTime": "2026-05-11T12:00:00"
            }
        }
    }
id -> 作家id
authorName -> 作者笔名
userPhoto -> 用户头像，未设置时为null
wordCount -> 作者累计字数
recentNovelRespDto -> 最近更新小说，暂无作品时为null
recentNovelRespDto.id -> 小说id
recentNovelRespDto.bookName -> 小说名称
recentNovelRespDto.coverUrl -> 小说封面链接
recentNovelRespDto.wordCount -> 小说字数
recentNovelRespDto.latestChapterName -> 最新章节名称
recentNovelRespDto.lastChapterTime -> 最新章节更新时间
异常响应：
    未登录或登录失效：
    {
        "code": 401,
        "message": "未登录或登录已失效",
        "data": null
    }
    当前用户不是作家：
    {
        "code": 403,
        "message": "无权限访问",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 只有作家角色用户可以访问
    3. 用户id和用户角色由后端从token中解析，前端不需要传userId或userRole
    4. 最近更新小说按最新章节更新时间倒序取第一本
```

## 作家作品管理页面列表查询
```text
请求路径：
/api/author/novels?pageNum=1&pageSize=5
请求方式：
    GET
请求头：
    Authorization: Bearer token值
参数：
    pageNum -> 当前是第几页，默认1
    pageSize -> 每页有多少条数据，默认5
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
                    "updateStatus": 0,
                    "wordCount": 37200,
                    "latestChapterName": "第九章 装逼",
                    "lastChapterTime": "2026-05-11T12:00:00"
                },
                {
                    "id": 2,
                    "bookName": "修仙界唯一出马仙",
                    "coverUrl": "https://bookcover.yuewen.com/qdbimg/349573/1048721558/600.webp",
                    "updateStatus": 1,
                    "wordCount": 244300,
                    "latestChapterName": "第30章 受够了",
                    "lastChapterTime": "2026-05-12T12:00:00"
                }
            ],
            "total": 2,
            "pageNum": 1,
            "pageSize": 5,
            "pages": 1
        }
    }
id -> 小说id
bookName -> 小说名称
coverUrl -> 小说封面链接
updateStatus -> 更新状态（0-连载中，1-已完结）
wordCount -> 小说字数
latestChapterName -> 最新章节名称
lastChapterTime -> 最新章节更新时间
total -> 一共有多少条数据
pageNum -> 当前是第几页
pageSize -> 当前页有多少条数据
pages -> 一共有几页
异常响应：
    未登录或登录失效：
    {
        "code": 401,
        "message": "未登录或登录已失效",
        "data": null
    }
    当前用户不是作家：
    {
        "code": 403,
        "message": "当前用户不是作家",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 只有作家角色用户可以访问
    3. 用户id和用户角色由后端从token中解析，前端不需要传userId或userRole
    4. 只查询当前登录作家自己的作品
    5. 作品按修改时间倒序排列
```

## 作家某本小说详情查询
```text
请求路径：
/api/author/{bookId}?pageNum=1&pageSize=10
请求方式：
    GET
请求头：
    Authorization: Bearer token值
参数：
    bookId -> 小说id
    pageNum -> 章节目录当前是第几页，默认1
    pageSize -> 章节目录每页有多少条数据，默认10
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": {
            "id": 1,
            "bookName": "碧阳仙门",
            "coverUrl": "https://bookcover.yuewen.com/qdbimg/349573/1048992740/600.webp",
            "categoryName": "玄幻",
            "publishStatus": 1,
            "updateStatus": 0,
            "bookIntro": "自从天道定鼎，仙释共分万国。仙门称碧阳，赤释作妙土。",
            "bookChapterList": {
                "records": [
                    {
                        "id": 1,
                        "chapterName": "第一章 碧阳仙门",
                        "chapterNum": 1,
                        "wordCount": 4200,
                        "chapterStatus": 1
                    },
                    {
                        "id": 2,
                        "chapterName": "第二章 入门",
                        "chapterNum": 2,
                        "wordCount": 3800,
                        "chapterStatus": 0
                    }
                ],
                "total": 2,
                "pageNum": 1,
                "pageSize": 5,
                "pages": 1
            }
        }
    }
id -> 小说id
bookName -> 小说名称
coverUrl -> 小说封面链接
categoryName -> 小说分类名称
publishStatus -> 发布状态（0-下架，1-上架）
updateStatus -> 更新状态（0-连载中，1-已完结）
bookIntro -> 小说简介
bookChapterList -> 小说章节目录分页数据
bookChapterList.records.id -> 章节id
bookChapterList.records.chapterName -> 章节名称
bookChapterList.records.chapterNum -> 章节序号
bookChapterList.records.wordCount -> 章节字数
bookChapterList.records.chapterStatus -> 章节状态（0-草稿，1-已发布，2-下架，3-审核中）
bookChapterList.total -> 一共有多少条章节数据
bookChapterList.pageNum -> 当前是第几页
bookChapterList.pageSize -> 当前页有多少条章节数据
bookChapterList.pages -> 一共有几页
异常响应：
    未登录或登录失效：
    {
        "code": 401,
        "message": "未登录或登录已失效",
        "data": null
    }
    当前用户不是作家：
    {
        "code": 403,
        "message": "无权限访问",
        "data": null
    }
    小说不存在或不属于当前作家：
    {
        "code": 404,
        "message": "小说信息不存在",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 只有作家角色用户可以访问
    3. 用户id和用户角色由后端从token中解析，前端不需要传userId或userRole
    4. 只允许查询当前登录作家自己的小说详情
    5. bookId和当前作家id必须同时匹配才会返回小说信息
    6. 章节目录按章节序号升序排列
    7. 如果该小说暂无章节，bookChapterList.records为空数组，total为0
```

## 小说信息变更审核提交
```text
请求路径：
/api/author/{bookId}
请求方式：
    POST
请求头：
    Authorization: Bearer token值
参数：
    bookId -> 小说id
请求体：
    {
        "bookName": "碧阳仙门新版",
        "bookIntro": "自从天道定鼎，仙释共分万国。仙门称碧阳，赤释作妙土。",
        "publishStatus": 1
    }
bookName -> 变更后的小说名称，不传则不修改
bookIntro -> 变更后的小说简介，不传则不修改
publishStatus -> 发布状态，仅用于提交上架审核；当前小说为下架状态且传1时表示申请上架，不传则不修改
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": null
    }
异常响应：
    未登录或登录失效：
    {
        "code": 401,
        "message": "未登录或登录已失效",
        "data": null
    }
    当前用户不是作家：
    {
        "code": 403,
        "message": "无权限访问",
        "data": null
    }
    小说不存在或不属于当前作家：
    {
        "code": 404,
        "message": "小说信息不存在",
        "data": null
    }
    参数无效：
    {
        "code": 501,
        "message": "参数无效",
        "data": null
    }
    提交失败：
    {
        "code": 500,
        "message": "申请失败",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 只有作家角色用户可以访问
    3. 用户id和用户角色由后端从token中解析，前端不需要传userId或userRole
    4. 只允许提交当前登录作家自己小说的信息变更审核
    5. bookId和当前作家id必须同时匹配才允许提交
    6. 前端不需要在请求体中传bookId和authorId，后端分别从路径参数和token中获取
    7. 如果该小说已有待审核的信息变更申请，本次提交会合并到原申请中
    8. 请求体中字段为null或未传时表示不修改该字段，不会覆盖已有待审核内容
    9. 已通过或已驳回的历史申请不会被修改，会生成新的待审核申请
    10. publishStatus只支持下架到上架审核：当前小说publishStatus为0且请求体传1时，会提交上架审核
    11. 直接下架不走审核，请调用 PUT /api/author/{bookId} 并传publishStatus为0
    12. 如果只传publishStatus为0，或当前小说已经上架仍传publishStatus为1，会返回参数无效
    13. 小说封面变更请调用 /api/author/{bookId}/uploadcover 接口
```

## 作家小说状态更新
```text
请求路径：
/api/author/{bookId}?pageNum=1&pageSize=5
请求方式：
    PUT
请求头：
    Authorization: Bearer token值
参数：
    bookId -> 小说id
    pageNum -> 当前作品列表页码，默认1，用于删除对应分页的作品列表缓存
    pageSize -> 当前作品列表每页数量，默认5，用于删除对应分页的作品列表缓存
请求体：
    {
        "publishStatus": 0,
        "updateStatus": 0
    }
publishStatus -> 发布状态，仅允许传0表示直接下架；上架需要提交审核，不传则不修改
updateStatus -> 更新状态（0-连载中，1-已完结），不传则不修改
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": null
    }
异常响应：
    未登录或登录失效：
    {
        "code": 401,
        "message": "未登录或登录已失效",
        "data": null
    }
    当前用户不是作家：
    {
        "code": 403,
        "message": "无权限访问",
        "data": null
    }
    参数无效：
    {
        "code": 501,
        "message": "参数无效",
        "data": null
    }
    更新失败：
    {
        "code": 500,
        "message": "更新失败",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 只有作家角色用户可以访问
    3. 用户id和用户角色由后端从token中解析，前端不需要传userId或userRole
    4. 只允许更新当前登录作家自己的小说状态
    5. publishStatus和updateStatus可以只传一个，但不能都不传
    6. updateStatus传入时只能为0或1
    7. publishStatus传入时只能为0，用于直接下架；publishStatus为1的上架操作需要调用 POST /api/author/{bookId} 提交审核
    8. 更新失败可能表示小说不存在或不属于当前作家
    9. pageNum和pageSize用于定位并删除作家作品管理列表缓存key，不参与小说状态更新
```

## 小说封面变更审核提交
```text
请求路径：
/api/author/{bookId}/uploadcover
请求方式：
    POST
请求头：
    Authorization: Bearer token值
参数：
    bookId -> 小说id
    file -> 本地封面图片文件
    coverUrl -> 封面图片链接
参数说明：
    1. file和coverUrl必须二选一
    2. file用于上传本地封面图片，使用multipart/form-data提交
    3. coverUrl用于直接提交封面图片链接，使用Query参数或表单参数提交
本地文件上传示例：
    Content-Type: multipart/form-data
    file: 选择本地图片文件
图片链接提交示例：
    /api/author/1/uploadcover?coverUrl=https://xxx.com/book-cover.png
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": null
    }
异常响应：
    未登录或登录失效：
    {
        "code": 401,
        "message": "未登录或登录已失效",
        "data": null
    }
    当前用户不是作家：
    {
        "code": 403,
        "message": "无权限访问",
        "data": null
    }
    未选择或同时选择两种上传方式：
    {
        "code": 500,
        "message": "请选择一种图片上传方式",
        "data": null
    }
    小说不存在或不属于当前作家：
    {
        "code": 500,
        "message": "小说信息不存在",
        "data": null
    }
    提交失败：
    {
        "code": 500,
        "message": "申请失败",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 只有作家角色用户可以访问
    3. 用户id和用户角色由后端从token中解析，前端不需要传userId或userRole
    4. 只允许提交当前登录作家自己小说的封面变更审核
    5. 如果该小说已有待审核的信息变更申请，本次提交会合并到原申请中
    6. 已通过或已驳回的历史申请不会被修改，会生成新的待审核申请
    7. 本地文件上传成功但审核记录保存失败时，后端会删除本次新上传的OSS文件
    8. 覆盖已有待审核封面后，后端会尝试删除旧的待审核封面；删除失败不影响本次提交
```

## 作家新建作品审核提交
```text
请求路径：
/api/author/create
请求方式：
    POST
请求头：
    Authorization: Bearer token值
Content-Type：
    multipart/form-data
参数：
    bookName -> 小说名称
    bookIntro -> 小说简介
    categoryId -> 小说分类id
    file -> 本地封面图片文件
    coverUrl -> 封面图片链接
参数说明：
    1. bookName、bookIntro、categoryId必传，且bookName和bookIntro不能为空字符串
    2. file和coverUrl必须二选一
    3. file用于上传本地封面图片
    4. coverUrl用于直接提交封面图片链接
    5. 请求使用multipart/form-data提交，bookName、bookIntro、categoryId、coverUrl均作为表单字段提交
本地文件上传示例：
    Content-Type: multipart/form-data
    bookName: 碧阳仙门
    bookIntro: 自从天道定鼎，仙释共分万国。仙门称碧阳，赤释作妙土。
    categoryId: 1
    file: 选择本地图片文件
图片链接提交示例：
    Content-Type: multipart/form-data
    bookName: 碧阳仙门
    bookIntro: 自从天道定鼎，仙释共分万国。仙门称碧阳，赤释作妙土。
    categoryId: 1
    coverUrl: https://xxx.com/book-cover.png
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": null
    }
异常响应：
    未登录或登录失效：
    {
        "code": 401,
        "message": "未登录或登录已失效",
        "data": null
    }
    当前用户不是作家：
    {
        "code": 403,
        "message": "无权限访问",
        "data": null
    }
    参数无效：
    {
        "code": 501,
        "message": "参数无效",
        "data": null
    }
    分类不存在：
    {
        "code": 404,
        "message": "分类id不存在",
        "data": null
    }
    未选择或同时选择两种封面提交方式：
    {
        "code": 501,
        "message": "请选择一种图片上传方式",
        "data": null
    }
    重复提交：
    {
        "code": 500,
        "message": "不能重复创建",
        "data": null
    }
    创建失败：
    {
        "code": 500,
        "message": "创建失败",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 只有作家角色用户可以访问
    3. 用户id和用户角色由后端从token中解析，前端不需要传userId或userRole
    4. 新建作品不会直接写入正式小说表，会先写入小说信息变更审核表
    5. 审核类型为新建作品审核，审核通过后再创建正式作品
    6. 同一作者同名作品已有待审核的新建申请时，不能重复提交
    7. 本地文件上传成功但审核记录保存失败时，后端会尝试删除本次新上传的封面
```

## 小说变更信息审核列表查询
```text
请求路径：
/api/author/bookinfo/audit?pageNum=1&pageSize=10
请求方式：
    GET
请求头：
    Authorization: Bearer token值
参数：
    pageNum -> 当前是第几页，默认1
    pageSize -> 每页有多少条数据，默认5
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": {
            "records": [
                {
                    "id": 1,
                    "bookId": 1,
                    "bookName": "碧阳仙门",
                    "bookNameChange": "碧阳仙门新版",
                    "bookIntro": "自从天道定鼎，仙释共分万国。仙门称碧阳，赤释作妙土。",
                    "coverUrl": "https://xxx.com/book-cover.png",
                    "publishStatus": 1,
                    "auditStatus": 0,
                    "auditType": 2,
                    "adminName": null,
                    "submitTime": "2026-05-11T12:00:00",
                    "auditTime": null,
                    "auditRemark": null
                },
                {
                    "id": 2,
                    "bookId": 2,
                    "bookName": "修仙界唯一出马仙",
                    "bookNameChange": null,
                    "bookIntro": null,
                    "coverUrl": "https://xxx.com/book-cover-2.png",
                    "publishStatus": null,
                    "auditStatus": 1,
                    "auditType": 2,
                    "adminName": "管理员",
                    "submitTime": "2026-05-10T12:00:00",
                    "auditTime": "2026-05-10T13:00:00",
                    "auditRemark": "通过"
                }
            ],
            "total": 2,
            "pageNum": 1,
            "pageSize": 5,
            "pages": 1
        }
    }
id -> 审核记录id
bookId -> 小说id
bookName -> 当前小说名称
bookNameChange -> 变更后的小说名称，未变更时为null
bookIntro -> 变更后的小说简介，未变更时为null
coverUrl -> 变更后的小说封面链接，未变更时为null
publishStatus -> 变更后的发布状态，当前仅上架审核时为1，未变更时为null
auditStatus -> 审核状态（0-待审核，1-已通过，2-已驳回）
auditType -> 申请类型（1-作品创建，2-信息变更和作品上架）
adminName -> 审核人昵称，未审核时为null
submitTime -> 提交时间，当前取审核记录更新时间
auditTime -> 审核时间，未审核时为null
auditRemark -> 审核备注，无备注时为null
total -> 一共有多少条数据
pageNum -> 当前是第几页
pageSize -> 当前页有多少条数据
pages -> 一共有几页
异常响应：
    未登录或登录失效：
    {
        "code": 401,
        "message": "未登录或登录已失效",
        "data": null
    }
    当前用户不是作家：
    {
        "code": 403,
        "message": "无权限访问",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 只有作家角色用户可以访问
    3. 用户id和用户角色由后端从token中解析，前端不需要传userId或userRole
    4. 只查询当前登录作家自己的小说变更审核记录
    5. 如果当前作家暂无小说变更审核记录，records为空数组，total为0
```

## 小说变更信息审核详情查询
```text
请求路径：
/api/author/bookinfo/audit/{auditId}
请求方式：
    GET
请求头：
    Authorization: Bearer token值
参数：
    auditId -> 审核记录id
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": {
            "id": 1,
            "bookName": "碧阳仙门",
            "bookNameChange": "碧阳仙门新版",
            "bookIntro": "自从天道定鼎，仙释共分万国。仙门称碧阳，赤释作妙土。",
            "coverUrl": "https://xxx.com/book-cover.png",
            "publishStatus": 1,
            "auditStatus": 0,
            "auditType": 2,
            "auditRemark": null,
            "auditTime": null,
            "auditName": null
        }
    }
id -> 审核记录id
bookName -> 当前小说名称，新建作品审核时为null
bookNameChange -> 变更后的小说名称，未变更时为null
bookIntro -> 变更后的小说简介，未变更时为null
coverUrl -> 变更后的小说封面链接，未变更时为null
publishStatus -> 变更后的发布状态，当前仅上架审核时为1，未变更时为null
auditStatus -> 审核状态（0-待审核，1-已通过，2-已驳回）
auditType -> 申请类型（1-作品创建，2-信息变更和作品上架）
auditRemark -> 审核备注，无备注时为null
auditTime -> 审核时间，未审核时为null
auditName -> 审核人昵称，未审核时为null
异常响应：
    未登录或登录失效：
    {
        "code": 401,
        "message": "未登录或登录已失效",
        "data": null
    }
    当前用户不是作家：
    {
        "code": 403,
        "message": "无权限访问",
        "data": null
    }
    审核信息不存在：
    {
        "code": 404,
        "message": "审核信息不存在",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 只有作家角色用户可以访问
    3. 用户id和用户角色由后端从token中解析，前端不需要传userId或userRole
    4. 只允许查询当前登录作家自己的小说变更审核详情
    5. auditId和当前作家id必须同时匹配才会返回审核详情
```

## 小说变更信息审核删除
```text
请求路径：
/api/author/bookinfo/audit/{auditId}
请求方式：
    DELETE
请求头：
    Authorization: Bearer token值
参数：
    auditId -> 审核记录id
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": null
    }
异常响应：
    未登录或登录失效：
    {
        "code": 401,
        "message": "未登录或登录已失效",
        "data": null
    }
    当前用户不是作家：
    {
        "code": 403,
        "message": "无权限访问",
        "data": null
    }
    删除失败：
    {
        "code": 500,
        "message": "删除失败",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 只有作家角色用户可以访问
    3. 用户id和用户角色由后端从token中解析，前端不需要传userId或userRole
    4. 只允许删除当前登录作家自己的小说变更审核记录
    5. 只允许删除待审核状态记录，auditStatus必须为0
    6. 已通过或已驳回的历史审核记录不能删除
```

## 管理员小说变更审核列表查询
```text
请求路径：
/api/admin/list?pageNum=1&pageSize=5&auditStatus=0
请求方式：
    GET
请求头：
    Authorization: Bearer token值
参数：
    pageNum -> 当前是第几页，默认1
    pageSize -> 每页有多少条数据，默认5
    auditStatus -> 审核状态（0-待审核，1-已通过，2-已驳回），必传
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": {
            "records": [
                {
                    "id": 1,
                    "bookId": 1,
                    "authorId": 1,
                    "authorName": "风铃",
                    "bookName": "碧阳仙门",
                    "coverUrl": "https://xxx.com/book-cover.png",
                    "subTime": "2026-05-11T12:00:00",
                    "auditType": 2
                },
                {
                    "id": 2,
                    "bookId": 2,
                    "authorId": 2,
                    "authorName": "尼禄2077",
                    "bookName": "修仙界唯一出马仙",
                    "coverUrl": "https://xxx.com/book-cover-2.png",
                    "subTime": "2026-05-12T12:00:00",
                    "auditType": 2
                }
            ],
            "total": 2,
            "pageNum": 1,
            "pageSize": 5,
            "pages": 1
        }
    }
id -> 审核记录id
bookId -> 小说id
authorId -> 作家id
authorName -> 作家笔名
bookName -> 小说名称
coverUrl -> 小说封面链接
subTime -> 提交时间
auditType -> 审核类型（2-信息变更和作品上架）
total -> 一共有多少条数据
pageNum -> 当前是第几页
pageSize -> 当前页有多少条数据
pages -> 一共有几页
异常响应：
    未登录或登录失效：
    {
        "code": 401,
        "message": "未登录或登录已失效",
        "data": null
    }
    当前用户不是管理员：
    {
        "code": 403,
        "message": "无权限访问",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 只有管理员角色用户可以访问
    3. 用户id和用户角色由后端从token中解析，前端不需要传userId或userRole
    4. 根据auditStatus查询小说信息变更和上架申请
    5. auditStatus为0时表示待审核，1表示已通过，2表示已驳回
    6. 列表按提交时间升序排列，先提交的申请排在前面
    7. 如果暂无对应状态的审核记录，records为空数组，total为0
```

## 管理员新书审核列表查询
```text
请求路径：
/api/admin/list/create?pageNum=1&pageSize=5&auditStatus=0
请求方式：
    GET
请求头：
    Authorization: Bearer token值
参数：
    pageNum -> 当前是第几页，默认1
    pageSize -> 每页有多少条数据，默认5
    auditStatus -> 审核状态（0-待审核，1-已通过，2-已驳回），必传
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": {
            "records": [
                {
                    "id": 1,
                    "bookId": null,
                    "bookName": "碧阳仙门",
                    "coverUrl": "https://xxx.com/book-cover.png",
                    "authorId": 1,
                    "authorName": "风铃",
                    "subTime": "2026-05-11T12:00:00"
                },
                {
                    "id": 2,
                    "bookId": null,
                    "bookName": "修仙界唯一出马仙",
                    "coverUrl": "https://xxx.com/book-cover-2.png",
                    "authorId": 2,
                    "authorName": "尼禄2077",
                    "subTime": "2026-05-12T12:00:00"
                }
            ],
            "total": 2,
            "pageNum": 1,
            "pageSize": 5,
            "pages": 1
        }
    }
id -> 审核记录id
bookId -> 小说id，新建作品未审核通过前通常为null
bookName -> 新建作品名称
coverUrl -> 新建作品封面链接
authorId -> 作家id
authorName -> 作家笔名
subTime -> 提交时间
total -> 一共有多少条数据
pageNum -> 当前是第几页
pageSize -> 当前页有多少条数据
pages -> 一共有几页
异常响应：
    未登录或登录失效：
    {
        "code": 401,
        "message": "未登录或登录已失效",
        "data": null
    }
    当前用户不是管理员：
    {
        "code": 403,
        "message": "无权限访问",
        "data": null
    }
    参数无效：
    {
        "code": 501,
        "message": "参数无效",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 只有管理员角色用户可以访问
    3. 用户id和用户角色由后端从token中解析，前端不需要传userId或userRole
    4. 根据auditStatus查询新建作品审核申请
    5. auditStatus为0时表示待审核，1表示已通过，2表示已驳回
    6. 列表按提交时间升序排列，先提交的申请排在前面
    7. 如果暂无对应状态的新书审核记录，records为空数组，total为0
```

## 管理员章节审核列表查询
```text
请求路径：
/api/admin/list/chapters?pageNum=1&pageSize=5&auditStatus=0
请求方式：
    GET
请求头：
    Authorization: Bearer token值
参数：
    pageNum -> 当前是第几页，默认1
    pageSize -> 每页有多少条数据，默认5
    auditStatus -> 审核状态（0-待审核，1-已通过，2-已驳回），必传
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": {
            "records": [
                {
                    "id": 1,
                    "chapterId": 1,
                    "bookName": "碧阳仙门",
                    "authorName": "风铃",
                    "chapterName": "第一章 碧阳仙门",
                    "wordCount": 4200,
                    "subTime": "2026-05-11T12:00:00"
                },
                {
                    "id": 2,
                    "chapterId": 2,
                    "bookName": "修仙界唯一出马仙",
                    "authorName": "尼禄2077",
                    "chapterName": "第二章 入门",
                    "wordCount": 3800,
                    "subTime": "2026-05-12T12:00:00"
                }
            ],
            "total": 2,
            "pageNum": 1,
            "pageSize": 5,
            "pages": 1
        }
    }
id -> 章节审核记录id
chapterId -> 章节id
bookName -> 小说名称
authorName -> 作家笔名
chapterName -> 章节名称
wordCount -> 章节字数
subTime -> 提交时间，当前取章节审核记录更新时间
total -> 一共有多少条数据
pageNum -> 当前是第几页
pageSize -> 当前页有多少条数据
pages -> 一共有几页
异常响应：
    未登录或登录失效：
    {
        "code": 401,
        "message": "未登录或登录已失效",
        "data": null
    }
    当前用户不是管理员：
    {
        "code": 403,
        "message": "无权限访问",
        "data": null
    }
    参数无效：
    {
        "code": 501,
        "message": "参数无效",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 只有管理员角色用户可以访问
    3. 用户id和用户角色由后端从token中解析，前端不需要传userId或userRole
    4. 根据auditStatus查询章节审核申请
    5. auditStatus为0时表示待审核，1表示已通过，2表示已驳回
    6. 列表按提交时间倒序排列，最近提交或更新的申请排在前面
    7. 如果暂无对应状态的章节审核记录，records为空数组，total为0
```

## 作家草稿箱列表查询
```text
请求路径：
/api/author/drafts?pageNum=1&pageSize=5
请求方式：
    GET
请求头：
    Authorization: Bearer token值
参数：
    pageNum -> 当前是第几页，默认1
    pageSize -> 每页有多少条数据，默认6
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": {
            "records": [
                {
                    "id": 1,
                    "bookId": 1,
                    "chapterName": "第一章 碧阳仙门",
                    "bookName": "碧阳仙门",
                    "chapterContent": "自从天道定鼎，仙释共分万国。仙门称碧阳，赤释作妙土……",
                    "wordCount": 4200,
                    "updateTime": "2026-05-11T12:00:00"
                },
                {
                    "id": 2,
                    "bookId": 1,
                    "chapterName": "第二章 入门",
                    "bookName": "碧阳仙门",
                    "chapterContent": "山门之前，少年抬头望向云雾深处……",
                    "wordCount": 3800,
                    "updateTime": "2026-05-10T12:00:00"
                }
            ],
            "total": 2,
            "pageNum": 1,
            "pageSize": 5,
            "pages": 1
        }
    }
id -> 草稿章节id
bookId -> 小说id
chapterName -> 章节名称
bookName -> 小说名称
chapterContent -> 章节正文摘要，超过100字时会截断
wordCount -> 章节字数
updateTime -> 草稿更新时间
total -> 一共有多少条数据
pageNum -> 当前是第几页
pageSize -> 当前页有多少条数据
pages -> 一共有几页
异常响应：
    未登录或登录失效：
    {
        "code": 401,
        "message": "未登录或登录已失效",
        "data": null
    }
    当前用户不是作家：
    {
        "code": 403,
        "message": "无权限访问",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 只有作家角色用户可以访问
    3. 用户id和用户角色由后端从token中解析，前端不需要传userId或userRole
    4. 只查询当前登录作家自己作品下的草稿章节
    5. 只返回草稿状态章节，按更新时间倒序排列
    6. 如果当前作家暂无作品或暂无草稿，records为空数组，total为0
```

## 作家审核章节列表查询
```text
请求路径：
/api/author/audit/list?pageNum=1&pageSize=5
请求方式：
    GET
请求头：
    Authorization: Bearer token值
参数：
    pageNum -> 当前是第几页，默认1
    pageSize -> 每页有多少条数据，默认5
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": {
            "records": [
                {
                    "id": 1,
                    "bookId": 1,
                    "chapterName": "第一章 碧阳仙门",
                    "bookName": "碧阳仙门",
                    "wordCount": 4200,
                    "applicationTime": "2026-05-11T12:00:00"
                },
                {
                    "id": 2,
                    "bookId": 1,
                    "chapterName": "第二章 入门",
                    "bookName": "碧阳仙门",
                    "wordCount": 3800,
                    "applicationTime": "2026-05-10T12:00:00"
                }
            ],
            "total": 2,
            "pageNum": 1,
            "pageSize": 5,
            "pages": 1
        }
    }
id -> 审核中章节id
bookId -> 小说id
chapterName -> 章节名称
bookName -> 小说名称
wordCount -> 章节字数
applicationTime -> 提交审核时间，当前取章节更新时间
total -> 一共有多少条数据
pageNum -> 当前是第几页
pageSize -> 当前页有多少条数据
pages -> 一共有几页
异常响应：
    未登录或登录失效：
    {
        "code": 401,
        "message": "未登录或登录已失效",
        "data": null
    }
    当前用户不是作家：
    {
        "code": 403,
        "message": "无权限访问",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 只有作家角色用户可以访问
    3. 用户id和用户角色由后端从token中解析，前端不需要传userId或userRole
    4. 只查询当前登录作家自己作品下的审核中章节
    5. 只返回审核中状态章节，按提交审核时间倒序排列
    6. 如果当前作家暂无审核中章节，records为空数组，total为0
```

## 作家审核章节撤回
```text
请求路径：
/api/author/{bookId}/chapters/{chapterId}/cancel
请求方式：
    PUT
请求头：
    Authorization: Bearer token值
参数：
    bookId -> 小说id
    chapterId -> 章节id
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": null
    }
异常响应：
    未登录或登录失效：
    {
        "code": 401,
        "message": "未登录或登录已失效",
        "data": null
    }
    当前用户不是作家：
    {
        "code": 403,
        "message": "无权限访问",
        "data": null
    }
    审核章节不存在：
    {
        "code": 404,
        "message": "审核章节不存在",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 只有作家角色用户可以访问
    3. 用户id和用户角色由后端从token中解析，前端不需要传userId或userRole
    4. 只允许撤回当前登录作家自己作品下的审核中章节
    5. bookId、chapterId、当前作家id和章节审核中状态必须同时匹配才会撤回成功
    6. 撤回成功后章节状态会变为草稿（0）
```

## 编辑页小说列表获取
```text
请求路径：
/api/author/edit/booklist
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
        "data": [
            {
                "id": 1,
                "bookName": "碧阳仙门"
            },
            {
                "id": 2,
                "bookName": "修仙界唯一出马仙"
            }
        ]
    }
id -> 小说id
bookName -> 小说名称
异常响应：
    未登录或登录失效：
    {
        "code": 401,
        "message": "未登录或登录已失效",
        "data": null
    }
    当前用户不是作家：
    {
        "code": 403,
        "message": "无权限访问",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 只有作家角色用户可以访问
    3. 用户id和用户角色由后端从token中解析，前端不需要传userId或userRole
    4. 只查询当前登录作家自己的小说
    5. 如果当前作家暂无作品，data为空数组
```

## 作家章节编辑信息查询
```text
请求路径：
/api/author/{bookId}/chapters/{chapterId}
请求方式：
    GET
请求头：
    Authorization: Bearer token值
参数：
    bookId -> 小说id
    chapterId -> 章节id
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": {
            "id": 1,
            "bookName": "碧阳仙门",
            "chapterName": "第一章 碧阳仙门",
            "chapterContent": "自从天道定鼎，仙释共分万国。仙门称碧阳，赤释作妙土。",
            "wordCount": 4200
        }
    }
id -> 章节id
bookName -> 小说名称
chapterName -> 章节名称
chapterContent -> 章节正文
wordCount -> 章节字数
异常响应：
    未登录或登录失效：
    {
        "code": 401,
        "message": "未登录或登录已失效",
        "data": null
    }
    当前用户不是作家：
    {
        "code": 403,
        "message": "无权限访问",
        "data": null
    }
    章节不存在或不属于当前作家：
    {
        "code": 404,
        "message": "章节信息未找到",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 只有作家角色用户可以访问
    3. 用户id和用户角色由后端从token中解析，前端不需要传userId或userRole
    4. 只查询当前登录作家自己作品下的章节
    5. bookId、chapterId和当前作家id必须同时匹配才会返回章节信息
```

## 作家章节信息更新
```text
请求路径：
/api/author/{bookId}/chapters/{chapterId}
请求方式：
    PUT
请求头：
    Authorization: Bearer token值
参数：
    bookId -> 小说id
    chapterId -> 章节id
请求体：
    {
        "chapterName": "第一章 碧阳仙门",
        "chapterContent": "自从天道定鼎，仙释共分万国。仙门称碧阳，赤释作妙土。",
        "chapterStatus": 3
    }
chapterName -> 章节名称，不传则不修改
chapterContent -> 章节正文，不传则不修改；传入后后端会重新计算章节字数
chapterStatus -> 章节状态（0-草稿，1-已发布，2-下架，3-审核中），不传则不修改
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": null
    }
异常响应：
    未登录或登录失效：
    {
        "code": 401,
        "message": "未登录或登录已失效",
        "data": null
    }
    当前用户不是作家：
    {
        "code": 403,
        "message": "无权限访问",
        "data": null
    }
    请求参数为空：
    {
        "code": 500,
        "message": "请求参数不能为空",
        "data": null
    }
    章节状态不合法：
    {
        "code": 500,
        "message": "章节状态不合法",
        "data": null
    }
    更新失败：
    {
        "code": 500,
        "message": "更新失败",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 只有作家角色用户可以访问
    3. 用户id和用户角色由后端从token中解析，前端不需要传userId或userRole
    4. 只允许更新当前登录作家自己作品下的章节
    5. bookId、chapterId和当前作家id必须同时匹配才会更新成功
    6. 前端不需要传wordCount，章节字数由后端根据chapterContent计算
    7. 如果请求体为空对象，则只更新章节更新时间
```

## 作家章节信息新增
```text
请求路径：
/api/author/{bookId}/chapters
请求方式：
    POST
请求头：
    Authorization: Bearer token值
参数：
    bookId -> 小说id
请求体：
    {
        "chapterName": "第一章 碧阳仙门",
        "chapterContent": "自从天道定鼎，仙释共分万国。仙门称碧阳，赤释作妙土。",
        "chapterStatus": 0
    }
chapterName -> 章节名称
chapterContent -> 章节正文
chapterStatus -> 章节状态（0-草稿，3-审核中），不传则默认草稿
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": {
            "id": 1
        }
    }
id -> 新增后的章节id
异常响应：
    未登录或登录失效：
    {
        "code": 401,
        "message": "未登录或登录已失效",
        "data": null
    }
    当前用户不是作家：
    {
        "code": 403,
        "message": "无权限访问",
        "data": null
    }
    请求参数为空：
    {
        "code": 500,
        "message": "章节信息为空",
        "data": null
    }
    章节状态不合法：
    {
        "code": 500,
        "message": "章节状态不对",
        "data": null
    }
    小说不存在或不属于当前作家：
    {
        "code": 404,
        "message": "未找到该小说",
        "data": null
    }
    新增失败：
    {
        "code": 500,
        "message": "新增章节失败",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 只有作家角色用户可以访问
    3. 用户id和用户角色由后端从token中解析，前端不需要传userId或userRole
    4. 只允许给当前登录作家自己的小说新增章节
    5. 新增章节的章节序号由后端按当前小说最大章节序号自动加1
    6. 新增章节只允许保存为草稿（0）或提交审核（3），不传chapterStatus时默认为草稿（0）
    7. 前端不需要传wordCount，章节字数由后端根据chapterContent计算
    8. 草稿和审核中章节不会设置publishTime
    9. 新增成功后会返回新增章节id
```

## 作家章节删除
```text
请求路径：
/api/author/{bookId}/chapters
请求方式：
    DELETE
请求头：
    Authorization: Bearer token值
参数：
    bookId -> 小说id
请求体：
    [
        1,
        2,
        3
    ]
请求体说明：
    数组中的值为需要删除的章节id
响应数据：
    {
        "code": 200,
        "message": "操作成功",
        "data": null
    }
异常响应：
    未登录或登录失效：
    {
        "code": 401,
        "message": "未登录或登录已失效",
        "data": null
    }
    当前用户不是作家：
    {
        "code": 403,
        "message": "无权限访问",
        "data": null
    }
    章节id为空：
    {
        "code": 500,
        "message": "章节id为空",
        "data": null
    }
    删除失败：
    {
        "code": 500,
        "message": "删除失败",
        "data": null
    }
说明：
    1. 该接口需要登录后调用
    2. 只有作家角色用户可以访问
    3. 用户id和用户角色由后端从token中解析，前端不需要传userId或userRole
    4. 只允许删除当前登录作家自己作品下的章节
    5. bookId、当前作家id和请求体中的章节id必须同时匹配才会删除成功
    6. 请求体不能为空数组，且数组中不能包含null
    7. 单个章节删除时，请求体数组只传一个章节id，例如：[1]
    8. 批量删除时，如果存在章节不存在、不属于该小说或不属于当前作家，则删除失败
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
            "userSex": 0,
            "nickName": "reader_123456",
            "userPhoto": "https://xxx.com/avatar.png",
            "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMDUzMDI2NDU5ODA4NDA3NTUzIiwidXNlcklkIjoyMDUzMDI2NDU5ODA4NDA3NTUzLCJ1c2VyUm9sZSI6MSwiaWF0IjoxNzc4MzE0NjI2LCJleHAiOjE3NzgzMjE4MjZ9.3v26ZA2RnSjaFwrNye_DDVwKvYCtDxcEvREGXQmr4f4"
        }
    }
    id -> 用户id
    userStatus -> 用户状态（0-正常，1-禁用）
    userRole -> 用户角色（0-管理员，1-读者，2-作家）
    userBalance -> 用户书币余额
    userSex -> 用户性别（0-男，1-女，未设置时为null）
    nickName -> 用户昵称
    userPhoto -> 用户头像，未设置时为null
    token -> JWT令牌
说明：登录成功后会返回首页展示所需的用户公共信息，前端可直接使用nickName、userPhoto、userBalance渲染登录态
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

## 最新上架小说查询
```text
请求路径：
/api/novel/recent
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
                "bookName": "碧阳仙门",
                "coverUrl": "https://bookcover.yuewen.com/qdbimg/349573/1048992740/600.webp",
                "authorName": "鹤守月满池",
                "bookIntro": "自从天道定鼎，仙释共分万国。仙门称碧阳，赤释作妙……",
                "categoryName": "玄幻"
            },
            {
                "id": 2,
                "bookName": "修仙界唯一出马仙",
                "coverUrl": "https://bookcover.yuewen.com/qdbimg/349573/1048721558/600.webp",
                "authorName": "尼禄2077",
                "bookIntro": "雨夜，高速，半挂卡车。没有奥丁，也没有尼伯龙根这……",
                "categoryName": "仙侠"
            },
            ...
        ]
    }
id -> 小说id
bookName -> 小说名称
coverUrl -> 小说封面链接
authorName -> 作者名字
bookIntro -> 小说简介（截断后）
categoryName -> 小说分类名称
说明：该接口默认返回已发布小说中最新上架的前4本
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
            "pageSize": 5,
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
