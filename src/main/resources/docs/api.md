# 接口文档
## 结果码
- 200 操作成功
- 500 操作失败
- 401 未登录或登录已失效
- 1001 用户名已存在

## 注册接口
```text
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
            "status": 0
        }
    }
    id -> 用户id
    status -> 用户状态 （0-正常，1-禁用）
```
