package com.moments.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 点赞实体类
 */
@Data
@TableName("post_like")
public class Like implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 点赞ID
   */
  @TableId(value = "like_id", type = IdType.AUTO)
  private Long likeId;

  /**
   * 帖子ID
   */
  private Long postId;

  /**
   * 用户ID
   */
  private Long userId;

  /**
   * 创建时间
   */
  private LocalDateTime createTime;
}
