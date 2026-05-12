# 登录拦截器实现方案

## 目标

统一处理需要登录的接口，避免每个 Controller 都手动校验 token。

登录拦截器完成后，可以实现：

- 书架接口必须登录后访问
- 后端从 token 中获取当前用户 id
- 前端不再传 `userId`，避免伪造其他用户 id
- 小说详情页可以根据当前用户 id 判断 `isShelf`

## 整体流程

```text
前端请求携带 token
        ↓
拦截器读取 Authorization 请求头
        ↓
校验 JWT 是否合法、是否过期
        ↓
解析 token 中的 userId
        ↓
校验 Redis 中保存的 token 是否一致
        ↓
将 userId 保存到 ThreadLocal
        ↓
Controller / Service 从 UserContext 获取当前用户 id
        ↓
请求结束后清理 ThreadLocal
```

## 前端请求格式

登录成功后，前端保存后端返回的 `token`。

请求需要登录的接口时，在请求头中携带：

```text
Authorization: Bearer token值
```

示例：

```text
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.xxx.xxx
```

## 用户上下文

新增文件：

```text
src/main/java/com/fengling/common/context/UserContext.java
```

```java
package com.fengling.common.context;

public class UserContext {

    private static final ThreadLocal<Long> USER_ID_THREAD_LOCAL = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        USER_ID_THREAD_LOCAL.set(userId);
    }

    public static Long getUserId() {
        return USER_ID_THREAD_LOCAL.get();
    }

    public static void remove() {
        USER_ID_THREAD_LOCAL.remove();
    }
}
```

作用：

```java
Long userId = UserContext.getUserId();
```

后续在 Controller 或 Service 中可以通过这行代码获取当前登录用户 id。

## JWTUtil 增加解析用户 id 方法

在 `JWTUtil` 中新增：

```java
public Long getUserId(String token) {
    return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get("userId", Long.class);
}
```

作用：从 JWT 中解析出登录用户 id。

## RedisUtil 增加读取方法

在 `RedisUtil` 中新增：

```java
public String getRedisCache(String key) {
    return redisTemplate.opsForValue().get(key);
}
```

作用：从 Redis 中读取当前用户保存的 token，用于判断 token 是否仍然有效。

## 登录拦截器

新增文件：

```text
src/main/java/com/fengling/interceptor/AuthInterceptor.java
```

```java
package com.fengling.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengling.common.constant.CacheConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.context.UserContext;
import com.fengling.common.resp.CommonResult;
import com.fengling.common.util.JWTUtil;
import com.fengling.common.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JWTUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");

        if (token == null || token.isBlank()) {
            writeUnauthorized(response);
            return false;
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (!jwtUtil.validateJwtToken(token)) {
            writeUnauthorized(response);
            return false;
        }

        Long userId = jwtUtil.getUserId(token);

        String redisKey = CacheConstants.REDIS_PREFIX + CacheConstants.AUTH_TOKEN + userId;
        String redisToken = redisUtil.getRedisCache(redisKey);

        if (redisToken == null || !redisToken.equals(token)) {
            writeUnauthorized(response);
            return false;
        }

        UserContext.setUserId(userId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        UserContext.remove();
    }

    private void writeUnauthorized(HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                objectMapper.writeValueAsString(CommonResult.fail(ResultCodeEnum.UNAUTHORIZED))
        );
    }
}
```

注意：

- 如果项目使用 Spring Boot 3，使用 `jakarta.servlet`。
- 如果项目使用 Spring Boot 2，需要把 `jakarta.servlet` 改成 `javax.servlet`。

## 注册拦截器

新增文件：

```text
src/main/java/com/fengling/config/WebMvcConfig.java
```

```java
package com.fengling.config;

import com.fengling.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/shelf/**")
                .excludePathPatterns(
                        "/api/user/register",
                        "/api/user/login"
                );
    }
}
```

当前建议先只拦截书架接口：

```text
/api/shelf/**
```

因为书架相关接口都需要登录。

## 书架接口调整

登录拦截器完成后，书架接口不建议继续从路径中传 `userId`。

原来的接口：

```text
GET  /api/shelf/{userId}/list?pageNum=1&pageSize=10
POST /api/shelf/{userId}/{bookId}
```

建议改成：

```text
GET  /api/shelf/list?pageNum=1&pageSize=10
POST /api/shelf/{bookId}
```

Controller 示例：

```java
@GetMapping(ApiPathConstants.LIST)
public CommonResult<PageRespDto<BookShelfRespDto>> listShelfNovels(PageReqDto pageReqDto) {
    Long userId = UserContext.getUserId();
    return bookShelfService.listShelfNovels(userId, pageReqDto);
}

@PostMapping("/{bookId}")
public CommonResult<Void> saveBookToBookShelf(@PathVariable("bookId") Long bookId) {
    Long userId = UserContext.getUserId();
    return bookShelfService.saveBookToBookShelf(userId, bookId);
}
```

这样可以避免前端伪造其他用户的 `userId`。

## 小说详情页 isShelf

`BookInfoRespDto` 中的字段：

```java
private Boolean isShelf;
```

含义是：当前登录用户是否已将该小说加入书架。

判断需要：

```text
userId + bookId
```

登录拦截器完成后，可以这样判断：

```java
Long userId = UserContext.getUserId();

Boolean isShelf = bookShelfMapper.exists(
        new LambdaQueryWrapper<BookShelf>()
                .eq(BookShelf::getUserId, userId)
                .eq(BookShelf::getBookId, bookId)
);

bookInfoRespDto.setIsShelf(isShelf);
```

对应 SQL：

```sql
SELECT COUNT(*)
FROM book_shelf
WHERE user_id = ?
  AND book_id = ?;
```

这个查询可以使用书架表中的联合唯一索引：

```sql
UNIQUE KEY uk_user_book (user_id, book_id)
```

## 后续扩展：可选登录解析

小说详情页通常允许未登录用户访问。

如果 `/api/novel/**` 不想强制登录，但又希望登录用户访问小说详情时能返回 `isShelf`，可以后续实现“可选登录解析”：

- 没有 token：放行，`isShelf` 返回 `false` 或 `null`
- 有 token 且合法：解析用户 id，设置到 `UserContext`
- 有 token 但非法：可以返回 `401`，也可以按业务决定忽略 token

第一阶段建议先完成：

```text
/api/shelf/** 强制登录拦截
```

等书架接口跑通后，再处理小说详情页的 `isShelf`。
