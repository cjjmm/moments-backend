package com.moments.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@TableName("user")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 用户ID
   */
  @TableId(value = "user_id", type = IdType.AUTO)
  private Long userId;

  /**
   * 用户名
   */
  private String username;

  /**
   * 密码（加密存储）
   */
  private String password;

  /**
   * 昵称
   */
  private String nickname;

  /**
   * 头像URL
   */
  private String avatar;

  /**
   * 角色：USER-普通用户, ADMIN-管理员
   */
  private String role;

  /**
   * 状态：0-禁用，1-正常
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
