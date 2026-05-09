# JWT 令牌创建流程

JWT，全称是 JSON Web Token，常用于登录认证。一个 JWT 令牌本质上是：

```text
用户信息 + 过期时间 + 密钥签名
```

最终生成的 JWT 字符串一般长这样：

```text
xxxxx.yyyyy.zzzzz
```

它由三部分组成：

```text
Header.Payload.Signature
```

---

## 1. 准备配置

在 `application.yml` 中配置 JWT 的密钥和过期时间：

```yaml
novel:
  jwt:
    secret: myJwtSecretKey2026fenglingyubingfenglingwej1234567890
    ttl: 7200000
```

说明：

- `secret`：JWT 签名密钥，只能保存在后端，不能返回给前端。
- `ttl`：JWT 有效期，单位是毫秒。
- `7200000` 毫秒等于 2 小时。

注意，不建议写成下面这种形式：

```yaml
ttl: 1000 * 60 * 60 * 2
```

因为 Spring Boot 默认不会自动计算这个表达式，而是会把它当成字符串处理。

---

## 2. 读取配置

可以通过 Spring 的 `@Value` 注解读取配置文件中的值：

```java
@Value("${novel.jwt.secret}") String secret
@Value("${novel.jwt.ttl}") long ttl
```

注意导包要使用 Spring 的 `@Value`：

```java
import org.springframework.beans.factory.annotation.Value;
```

不要导成 Lombok 的：

```java
import lombok.Value;
```

---

## 3. 创建 JWT 工具类

可以创建一个 `JWTUtil` 工具类，用于生成 JWT 令牌。

```java
package com.fengling.common.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private final SecretKey secretKey;
    private final long ttl;

    public JWTUtil(
            @Value("${novel.jwt.secret}") String secret,
            @Value("${novel.jwt.ttl}") long ttl
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.ttl = ttl;
    }

    public String createToken(Long userId, Integer userRole) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + ttl);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("userId", userId)
                .claim("userRole", userRole)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }
}
```

说明：

- `@Component`：把 `JWTUtil` 交给 Spring 管理。
- `@Value`：从 `application.yml` 中读取配置。
- `Keys.hmacShaKeyFor(...)`：把字符串密钥转换成 JWT 签名需要的 `SecretKey`。
- `ttl`：用来计算 JWT 的过期时间。
- `compact()`：生成最终的 JWT 字符串。

---

## 4. Header：令牌头部

JWT 的第一部分是 Header，用来描述令牌类型和签名算法。

示例：

```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

在代码中：

```java
.signWith(secretKey, Jwts.SIG.HS256)
```

这句代码会指定签名算法为 `HS256`，JJWT 会自动生成 Header。

---

## 5. Payload：令牌载荷

JWT 的第二部分是 Payload，用来存放用户相关信息和过期时间。

示例：

```json
{
  "sub": "1001",
  "userId": 1001,
  "userRole": 1,
  "iat": 1710000000,
  "exp": 1710007200
}
```

对应代码：

```java
.subject(String.valueOf(userId))
.claim("userId", userId)
.claim("userRole", userRole)
.issuedAt(now)
.expiration(expiration)
```

常见字段说明：

- `sub`：主题，一般存用户 ID。
- `userId`：用户 ID。
- `userRole`：用户角色。
- `iat`：签发时间。
- `exp`：过期时间。

注意：Payload 只是 Base64Url 编码，不是加密，所以不要存放密码、手机号、身份证号等敏感信息。

---

## 6. Signature：令牌签名

JWT 的第三部分是 Signature，用来防止令牌被篡改。

签名逻辑可以简单理解为：

```text
HMACSHA256(
  base64Url(header) + "." + base64Url(payload),
  secretKey
)
```

如果有人修改了 Payload 中的内容，例如把角色从普通用户改成管理员，签名就会校验失败。

所以 JWT 不是靠隐藏数据保证安全，而是靠签名保证数据不能被篡改。

---

## 7. 登录时生成令牌

在登录成功后注入 `JWTUtil`，然后调用 `createToken` 方法。

```java
private final JWTUtil jwtUtil;
```

登录成功后：

```java
String token = jwtUtil.createToken(userInfo.getId(), userInfo.getUserRole());
```

然后把 token 返回给前端。

前端后续请求接口时，一般放在请求头中：

```text
Authorization: Bearer token字符串
```

---

## 8. 完整创建流程

JWT 创建流程如下：

```text
用户登录
  ↓
校验用户名和密码
  ↓
查询用户 ID、角色等信息
  ↓
读取 JWT 配置 secret 和 ttl
  ↓
创建 Payload
  ↓
设置签发时间和过期时间
  ↓
使用 secretKey 和 HS256 算法签名
  ↓
调用 compact() 生成 JWT 字符串
  ↓
返回给前端
```

一句话总结：

```text
JWT 创建 = 设置用户信息 + 设置过期时间 + 使用密钥签名 + 生成字符串
```

