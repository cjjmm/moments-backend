package com.moments.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * 错误码
   */
  private Integer code;

  /**
   * 构造方法
   */
  public BusinessException(String message) {
    super(message);
    this.code = 500;
  }

  /**
   * 构造方法，带错误码
   */
  public BusinessException(Integer code, String message) {
    super(message);
    this.code = code;
  }
}
