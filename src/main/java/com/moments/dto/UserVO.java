package com.moments.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户列表展示VO
 */
@Data
public class UserVO {

  /**
   * 用户ID
   */
  private Long userId;

  /**
   * 用户名
   */
  private String username;

  /**
   * 昵称
   */
  private String nickname;

  /**
   * 头像URL
   */
  private String avatar;

  /**
   * 创建时间
   */
  private LocalDateTime createTime;

  /**
   * 发帖数量
   */
  private Integer postCount;
}
