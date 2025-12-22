package com.moments.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 内容/帖子实体类
 */
@Data
@TableName("post")
public class Post implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 内容ID
   */
  @TableId(value = "post_id", type = IdType.AUTO)
  private Long postId;

  /**
   * 发布用户ID
   */
  private Long userId;

  /**
   * 文本内容
   */
  private String content;

  /**
   * 媒体类型：TEXT-纯文本, IMAGE-图片, VIDEO-视频
   */
  private String mediaType;

  /**
   * 点赞数
   */
  private Integer likeCount;

  /**
   * 评论数
   */
  private Integer commentCount;

  /**
   * 浏览数
   */
  private Integer viewCount;

  /**
   * 平均评分
   */
  private BigDecimal avgRating;

  /**
   * 评分人数
   */
  private Integer ratingCount;

  /**
   * 状态：0-已删除，1-正常
   */
  private Integer status;

  /**
   * 创建时间
   */
  private LocalDateTime createTime;

  /**
   * 更新时间
   */
  private LocalDateTime updateTime;
}
