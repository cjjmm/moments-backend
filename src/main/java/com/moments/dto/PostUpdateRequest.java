package com.moments.dto;

import lombok.Data;

import java.util.List;

/**
 * 修改帖子请求DTO
 */
@Data
public class PostUpdateRequest {

  /**
   * 文本内容
   */
  private String content;

  /**
   * 标签数组
   */
  private List<String> tags;
}
