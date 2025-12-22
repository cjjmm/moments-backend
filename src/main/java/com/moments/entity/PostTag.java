package com.moments.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 帖子-标签关联实体类
 */
@Data
@TableName("post_tag")
public class PostTag implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 自增ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 内容ID
   */
  private Long postId;

  /**
   * 标签ID
   */
  private Long tagId;

  /**
   * 创建时间
   */
  private LocalDateTime createTime;
}
