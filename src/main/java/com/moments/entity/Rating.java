package com.moments.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 评分实体类
 */
@Data
@TableName("rating")
public class Rating implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 评分ID
   */
  @TableId(value = "rating_id", type = IdType.AUTO)
  private Long ratingId;

  /**
   * 内容ID
   */
  private Long postId;

  /**
   * 评分用户ID
   */
  private Long userId;

  /**
   * 评分：1-5分
   */
  private Integer score;

  /**
   * 创建时间
   */
  private LocalDateTime createTime;

  /**
   * 更新时间
   */
  private LocalDateTime updateTime;
}
