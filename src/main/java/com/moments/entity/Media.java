package com.moments.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 媒体文件实体类
 */
@Data
@TableName("media")
public class Media implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 媒体ID
   */
  @TableId(value = "media_id", type = IdType.AUTO)
  private Long mediaId;

  /**
   * 所属内容ID
   */
  private Long postId;

  /**
   * 媒体文件URL
   */
  private String mediaUrl;

  /**
   * 媒体类型：IMAGE-图片, VIDEO-视频
   */
  private String mediaType;

  /**
   * 文件大小（字节）
   */
  private Long fileSize;

  /**
   * 排序顺序
   */
  private Integer sortOrder;

  /**
   * 创建时间
   */
  private LocalDateTime createTime;
}
