package com.moments.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 配置类
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  /**
   * 密码加密器
   */
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * 安全过滤器链配置
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        // 禁用 CSRF（因为使用JWT，不需要CSRF保护）
        .csrf().disable()
        // 禁用 Session
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        // 配置请求授权
        .authorizeRequests()
        // 允许注册和登录接口无需认证
        .antMatchers("/api/v1/users/register", "/api/v1/users/login").permitAll()
        // 允许文件访问
        .antMatchers("/uploads/**").permitAll()
        // 其他请求暂时全部放行（后续可根据需要添加JWT过滤器）
        .anyRequest().permitAll()
        .and()
        // 禁用 frame options（用于H2控制台等）
        .headers().frameOptions().disable();

    return http.build();
  }
}
