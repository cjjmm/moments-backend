package com.moments.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 创建评分请求DTO
 */
@Data
public class RatingCreateRequest {

  /**
   * 帖子ID
   */
  @NotNull(message = "帖子ID不能为空")
  private Long postId;

  /**
   * 评分：1-5分
   */
  @NotNull(message = "评分不能为空")
  @Min(value = 1, message = "评分最低为1分")
  @Max(value = 5, message = "评分最高为5分")
  private Integer score;
}
