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