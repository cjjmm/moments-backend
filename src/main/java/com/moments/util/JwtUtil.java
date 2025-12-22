package com.moments.util;

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
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private Long expiration;

  /**
   * 生成 JWT Token
   * 
   * @param userId   用户ID
   * @param username 用户名
   * @return JWT Token
   */
  public String generateToken(Long userId, String username) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", userId);
    claims.put("username", username);
    return createToken(claims);
  }

  /**
   * 生成 JWT Token（带角色）
   */
  public String generateToken(Long userId, String username, String role) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", userId);
    claims.put("username", username);
    claims.put("role", role);
    return createToken(claims);
  }

  /**
   * 创建 Token
   */
  private String createToken(Map<String, Object> claims) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expiration);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }

  /**
   * 从 Token 中获取用户ID
   */
  public Long getUserIdFromToken(String token) {
    Claims claims = parseToken(token);
    return Long.valueOf(claims.get("userId").toString());
  }

  /**
   * 从 Token 中获取用户名
   */
  public String getUsernameFromToken(String token) {
    Claims claims = parseToken(token);
    return claims.get("username").toString();
  }

  /**
   * 从 Token 中获取角色
   */
  public String getRoleFromToken(String token) {
    Claims claims = parseToken(token);
    return claims.get("role") != null ? claims.get("role").toString() : "USER";
  }

  /**
   * 验证 Token 是否有效
   */
  public boolean validateToken(String token) {
    try {
      Claims claims = parseToken(token);
      return !claims.getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 解析 Token
   */
  private Claims parseToken(String token) {
    return Jwts.parser()
        .setSigningKey(secret)
        .parseClaimsJws(token)
        .getBody();
  }
}
