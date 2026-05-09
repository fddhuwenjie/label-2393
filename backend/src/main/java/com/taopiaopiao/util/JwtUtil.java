package com.taopiaopiao.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT工具类
 */
public class JwtUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    
    // 密钥（生产环境应从配置文件读取）
    private static final String SECRET = "taopiaopiao-jwt-secret-key-must-be-at-least-256-bits-long";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    
    // Token有效期：7天
    private static final long EXPIRATION = 7 * 24 * 60 * 60 * 1000L;
    
    /**
     * 生成Token
     */
    public static String generateToken(Long userId, String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION);
        
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("username", username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * 解析Token获取用户ID
     */
    public static Long getUserId(String token) {
        try {
            Claims claims = parseToken(token);
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            logger.warn("解析Token失败: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 解析Token获取用户名
     */
    public static String getUsername(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.get("username", String.class);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 验证Token是否有效
     */
    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("Token已过期");
        } catch (JwtException e) {
            logger.warn("Token无效: {}", e.getMessage());
        }
        return false;
    }
    
    /**
     * 解析Token
     */
    private static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
