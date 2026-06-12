package com.neusoft.environment.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 生成 JWT Token
     */
    public String generateToken(Long userId, String loginCode, String realName, Integer role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("loginCode", loginCode);
        claims.put("realName", realName);
        claims.put("role", role);
        return createToken(claims);
    }

    private String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 解析 Token 获取 Claims
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证 Token 是否有效
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从 Token 中获取用户ID
     */
    public Long getUserId(String token) {
        return parseToken(token).get("userId", Long.class);
    }

    /**
     * 从 Token 中获取角色
     */
    public Integer getRole(String token) {
        return parseToken(token).get("role", Integer.class);
    }

    /**
     * 从 Token 中获取登录编码
     */
    public String getLoginCode(String token) {
        return parseToken(token).get("loginCode", String.class);
    }
}
