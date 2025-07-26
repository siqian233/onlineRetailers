package com.lightning.product.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * JWT Token 生成和解析工具类。
 */
@Component
public class JwtTokenUtil {

    // 从 application.yml 中注入 JWT 密钥
    @Value("${token.secret}")
    private String secret;

    // 从 application.yml 中注入 JWT 过期时间（毫秒）
    @Value("${token.expiration}")
    private long expiration;

    /**
     * 生成 JWT Token。
     * @param userId 用户ID
     * @param username 用户名
     * @param roles 用户角色集合
     * @return 生成的 JWT Token
     */
    public String generateToken(Long userId, String username, Set<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userName", username);
        claims.put("roles", roles);

        return createToken(claims, username);
    }

    /**
     * 创建 JWT Token。
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims) // 设置自定义 claims
                .setSubject(subject) // 设置主题（通常是用户名）
                .setIssuedAt(now) // 设置签发时间
                .setExpiration(expiryDate) // 设置过期时间
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 使用 HS256 算法和密钥签名
                .compact();
    }

    /**
     * 获取签名密钥。
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 从 Token 中提取所有 Claims。
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    /**
     * 从 Token 中提取特定 Claim。
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 从 Token 中提取用户名。
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 从 Token 中提取过期时间。
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 检查 Token 是否过期。
     */
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 验证 Token。
     * 在本模块中，通常不在这里再次验证 Token，而是依赖 Gateway。
     * 这个方法主要用于 Gateway 或测试。
     */
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}

