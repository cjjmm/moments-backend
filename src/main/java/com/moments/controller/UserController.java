package com.moments.controller;

import com.moments.common.Result;
import com.moments.dto.LoginRequest;
import com.moments.dto.RegisterRequest;
import com.moments.entity.User;
import com.moments.service.UserService;
import com.moments.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private JwtUtil jwtUtil;

  /**
   * 用户注册
   */
  @PostMapping("/register")
  public Result<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
    Map<String, Object> result = userService.register(request);
    return Result.success("注册成功", result);
  }

  /**
   * 用户登录
   */
  @PostMapping("/login")
  public Result<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
    Map<String, Object> result = userService.login(request);
    return Result.success("登录成功", result);
  }

  /**
   * 获取用户信息
   */
  @GetMapping("/{userId}")
  public Result<User> getUserInfo(@PathVariable Long userId) {
    User user = userService.getUserById(userId);
    return Result.success(user);
  }

  /**
   * 修改密码
   */
  @PutMapping("/password")
  public Result<?> updatePassword(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
    // 从Token中获取用户ID
    String token = httpRequest.getHeader("Authorization");
    if (token != null && token.startsWith("Bearer ")) {
      token = token.substring(7);
    }
    Long userId = jwtUtil.getUserIdFromToken(token);

    String oldPassword = request.get("oldPassword");
    String newPassword = request.get("newPassword");

    userService.updatePassword(userId, oldPassword, newPassword);
    return Result.success("密码修改成功");
  }

  /**
   * 更新用户资料（头像、邮箱）
   */
  @PutMapping("/profile")
  public Result<?> updateProfile(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
    // 从Token中获取用户ID
    String token = httpRequest.getHeader("Authorization");
    if (token != null && token.startsWith("Bearer ")) {
      token = token.substring(7);
    }
    Long userId = jwtUtil.getUserIdFromToken(token);

    String avatar = request.get("avatar");
    String email = request.get("email");

    userService.updateProfile(userId, avatar, email);
    return Result.success("资料更新成功");
  }
}
