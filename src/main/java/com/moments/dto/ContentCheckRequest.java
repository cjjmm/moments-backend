package com.moments.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 内容检测请求DTO
 */
@Data
public class ContentCheckRequest {

  /**
   * 待检测的文本内容
   */
  @NotBlank(message = "内容不能为空")
  private String content;
}
