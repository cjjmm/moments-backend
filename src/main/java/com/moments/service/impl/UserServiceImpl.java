package com.moments.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moments.dto.LoginRequest;
import com.moments.dto.RegisterRequest;
import com.moments.dto.UserVO;
import com.moments.entity.User;
import com.moments.exception.BusinessException;
import com.moments.mapper.UserMapper;
import com.moments.service.UserService;
import com.moments.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private JwtUtil jwtUtil;

  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Override
  public Map<String, Object> register(RegisterRequest request) {
    // 检查用户名是否已存在
    LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(User::getUsername, request.getUsername());
    User existUser = userMapper.selectOne(wrapper);

    if (existUser != null) {
      throw new BusinessException(400, "用户名已存在");
    }

    // 创建新用户
    User user = new User();
    user.setUsername(request.getUsername());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setNickname(request.getNickname());
    user.setAvatar(request.getAvatar());
    user.setRole("USER");
    user.setStatus(1);

    userMapper.insert(user);

    // 生成Token
    String token = jwtUtil.generateToken(user.getUserId(), user.getUsername(), user.getRole());

    // 返回结果
    Map<String, Object> result = new HashMap<>();
    result.put("userId", user.getUserId());
    result.put("username", user.getUsername());
    result.put("nickname", user.getNickname());
    result.put("token", token);

    return result;
  }

  @Override
  public Map<String, Object> login(LoginRequest request) {
    // 查询用户
    LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(User::getUsername, request.getUsername());
    User user = userMapper.selectOne(wrapper);

    if (user == null) {
      throw new BusinessException(401, "用户名或密码错误");
    }

    // 验证密码
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new BusinessException(401, "用户名或密码错误");
    }

    // 检查用户状态
    if (user.getStatus() == 0) {
      throw new BusinessException(403, "账号已被禁用");
    }

    // 生成Token
    String token = jwtUtil.generateToken(user.getUserId(), user.getUsername(), user.getRole());

    // 返回结果
    Map<String, Object> result = new HashMap<>();
    result.put("userId", user.getUserId());
    result.put("username", user.getUsername());
    result.put("nickname", user.getNickname());
    result.put("avatar", user.getAvatar());
    result.put("token", token);

    return result;
  }

  @Override
  public User getUserById(Long userId) {
    User user = userMapper.selectById(userId);
    if (user == null) {
      throw new BusinessException(404, "用户不存在");
    }
    // 清除密码信息
    user.setPassword(null);
    return user;
  }

  @Override
  public void updatePassword(Long userId, String oldPassword, String newPassword) {
    User user = userMapper.selectById(userId);
    if (user == null) {
      throw new BusinessException(404, "用户不存在");
    }

    // 验证旧密码
    if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
      throw new BusinessException(400, "原密码错误");
    }

    // 更新密码
    user.setPassword(passwordEncoder.encode(newPassword));
    userMapper.updateById(user);
  }

  @Override
  public IPage<UserVO> getAllUsers(Integer page, Integer size) {
    Page<UserVO> pageParam = new Page<>(page, size);
    return userMapper.selectUserListWithPostCount(pageParam);
  }

  @Override
  public void deleteUser(Long userId) {
    User user = userMapper.selectById(userId);
    if (user == null) {
      throw new BusinessException(404, "用户不存在");
    }

    // 不允许删除管理员
    if ("ADMIN".equals(user.getRole())) {
      throw new BusinessException(403, "不能删除管理员账号");
    }

    // 逻辑删除：将状态设为0（禁用）
    user.setStatus(0);
    userMapper.updateById(user);
  }
}
