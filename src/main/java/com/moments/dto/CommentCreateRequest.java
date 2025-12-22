package com.moments.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 创建评论请求DTO
 */
@Data
public class CommentCreateRequest {

  /**
   * 帖子ID
   */
  @NotNull(message = "帖子ID不能为空")
  private Long postId;

  /**
   * 评论内容
   */
  @NotBlank(message = "评论内容不能为空")
  private String content;
}
