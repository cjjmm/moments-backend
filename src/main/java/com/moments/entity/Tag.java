package com.moments.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 标签实体类
 */
@Data
@TableName("tag")
public class Tag implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 标签ID
   */
  @TableId(value = "tag_id", type = IdType.AUTO)
  private Long tagId;

  /**
   * 标签名称
   */
  private String tagName;

  /**
   * 使用次数
   */
  private Integer useCount;

  /**
   * 创建时间
   */
  private LocalDateTime createTime;
}
