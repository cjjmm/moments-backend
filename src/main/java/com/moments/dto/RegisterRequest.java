package com.moments.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户注册请求DTO
 */
@Data
public class RegisterRequest {

  /**
   * 用户名
   */
  @NotBlank(message = "用户名不能为空")
  private String username;

  /**
   * 密码
   */
  @NotBlank(message = "密码不能为空")
  private String password;

  /**
   * 昵称
   */
  @NotBlank(message = "昵称不能为空")
  private String nickname;

  /**
   * 头像URL（可选）
   */
  private String avatar;
}
