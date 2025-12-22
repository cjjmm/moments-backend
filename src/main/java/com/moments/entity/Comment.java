package com.moments.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 评论实体类
 */
@Data
@TableName("comment")
public class Comment implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 评论ID
   */
  @TableId(value = "comment_id", type = IdType.AUTO)
  private Long commentId;

  /**
   * 内容ID
   */
  private Long postId;

  /**
   * 评论用户ID
   */
  private Long userId;

  /**
   * 评论内容
   */
  private String content;

  /**
   * 状态：0-已删除，1-正常
   */
  private Integer status;

  /**
   * 创建时间
   */
  private LocalDateTime createTime;
}
