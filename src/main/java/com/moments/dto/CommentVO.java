package com.moments.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评论展示VO
 */
@Data
public class CommentVO {

  /**
   * 评论ID
   */
  private Long commentId;

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
   * 头像
   */
  private String avatar;

  /**
   * 评论内容
   */
  private String content;

  /**
   * 创建时间
   */
  private LocalDateTime createTime;
}
