package com.moments.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moments.dto.LoginRequest;
import com.moments.dto.RegisterRequest;
import com.moments.dto.UserVO;
import com.moments.entity.User;

import java.util.Map;

/**
 * 用户服务接口
 */
public interface UserService {

  /**
   * 用户注册
   */
  Map<String, Object> register(RegisterRequest request);

  /**
   * 用户登录
   */
  Map<String, Object> login(LoginRequest request);

  /**
   * 根据用户ID获取用户信息
   */
  User getUserById(Long userId);

  /**
   * 修改密码
   */
  void updatePassword(Long userId, String oldPassword, String newPassword);

  /**
   * 分页获取所有用户（管理员）
   */
  IPage<UserVO> getAllUsers(Integer page, Integer size);

  /**
   * 删除用户（管理员）
   */
  void deleteUser(Long userId);
}
