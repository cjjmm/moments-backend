package com.moments.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 敏感词实体类
 */
@Data
@TableName("sensitive_word")
public class SensitiveWord implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 敏感词ID
   */
  @TableId(value = "word_id", type = IdType.AUTO)
  private Long wordId;

  /**
   * 敏感词
   */
  private String word;

  /**
   * 创建时间
   */
  private LocalDateTime createTime;
}
