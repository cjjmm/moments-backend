package com.moments.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 帖子展示VO
 */
@Data
public class PostVO {

  /**
   * 内容ID
   */
  private Long postId;

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
   * 文本内容
   */
  private String content;

  /**
   * 媒体URL列表
   */
  private List<String> mediaUrls;

  /**
   * 媒体类型
   */
  private String mediaType;

  /**
   * 标签列表
   */
  private List<String> tags;

  /**
   * 点赞数
   */
  private Integer likeCount;

  /**
   * 评论数
   */
  private Integer commentCount;

  /**
   * 平均评分
   */
  private BigDecimal avgRating;

  /**
   * 创建时间
   */
  private LocalDateTime createTime;
}
