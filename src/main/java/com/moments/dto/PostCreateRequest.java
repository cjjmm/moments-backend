package com.moments.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 创建帖子请求DTO
 */
@Data
public class PostCreateRequest {

  /**
   * 文本内容
   */
  private String content;

  /**
   * 媒体URL数组
   */
  private List<String> mediaUrls;

  /**
   * 媒体类型：TEXT-纯文本, IMAGE-图片, VIDEO-视频
   */
  @NotBlank(message = "媒体类型不能为空")
  private String mediaType;

  /**
   * 标签数组
   */
  private List<String> tags;
}
