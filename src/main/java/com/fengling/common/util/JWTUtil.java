package com.fengling.common.util;

import com.fengling.common.context.AuthUserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private final SecretKey secretKey;
    @Getter
    private final long ttl;

    public JWTUtil(
            @Value("${novel.jwt.secret}") String secret,
            @Value("${novel.jwt.ttl}") long ttl
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.ttl = ttl;
    }

    /**
     * 创建jwt令牌
     *
     * @param userId   用户id
     * @param userRole 用户角色
     * @return jwt令牌
     */
    public String createJwtToken(Long userId, Integer userRole) {
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

    /**
     * 验证jwt
     *
     * @param token jwt
     * @return 是否过期/是否被篡改
     */
    public boolean validateJwtToken(String token) {
        try {
            if (token == null || token.isBlank()) {
                return false;
            }
            Jwts.parser().verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


    /**
     * 解析JWT令牌
     *
     * @param token JWT令牌
     * @return 认证对象
     */
    public AuthUserInfo parseJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return new AuthUserInfo(
                claims.get("userId", Long.class),
                claims.get("userRole", Integer.class)
        );
    }
}
