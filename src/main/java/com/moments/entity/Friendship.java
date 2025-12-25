package com.moments.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 好友关系实体类
 */
@Data
@TableName("friendship")
public class Friendship implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 关系ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 用户ID
   */
  private Long userId;

  /**
   * 好友ID
   */
  private Long friendId;

  /**
   * 成为好友时间
   */
  private LocalDateTime createTime;
}
