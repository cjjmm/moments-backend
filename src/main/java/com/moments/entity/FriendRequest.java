package com.moments.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 好友申请实体类
 */
@Data
@TableName("friend_request")
public class FriendRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 申请ID
   */
  @TableId(value = "request_id", type = IdType.AUTO)
  private Long requestId;

  /**
   * 申请人ID
   */
  private Long fromUserId;

  /**
   * 被申请人ID
   */
  private Long toUserId;

  /**
   * 验证消息
   */
  private String message;

  /**
   * 状态：0-待处理，1-已同意，2-已拒绝
   */
  private Integer status;

  /**
   * 申请时间
   */
  private LocalDateTime createTime;

  /**
   * 更新时间
   */
  private LocalDateTime updateTime;
}
