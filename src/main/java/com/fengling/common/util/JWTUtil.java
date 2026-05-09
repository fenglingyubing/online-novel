package com.fengling.common.util;

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

    public String createJwtToken(Long userId, Integer userRole){
        Date now = new Date();
        Date expiration = new Date(now.getTime() + ttl);
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("userId",userId)
                .claim("userRole", userRole)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }
}
