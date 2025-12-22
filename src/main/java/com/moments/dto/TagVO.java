package com.moments.dto;

import lombok.Data;

/**
 * 标签展示VO
 */
@Data
public class TagVO {

  /**
   * 标签名称
   */
  private String tagName;

  /**
   * 使用次数
   */
  private Integer useCount;
}
