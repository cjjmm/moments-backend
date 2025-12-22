package com.moments.exception;

import com.moments.common.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 处理业务异常
   */
  @ExceptionHandler(BusinessException.class)
  public Result<?> handleBusinessException(BusinessException e) {
    return Result.error(e.getCode(), e.getMessage());
  }

  /**
   * 处理认证异常
   */
  @ExceptionHandler(AuthenticationException.class)
  public Result<?> handleAuthenticationException(AuthenticationException e) {
    return Result.error(401, "认证失败：" + e.getMessage());
  }

  /**
   * 处理权限异常
   */
  @ExceptionHandler(AccessDeniedException.class)
  public Result<?> handleAccessDeniedException(AccessDeniedException e) {
    return Result.error(403, "权限不足：" + e.getMessage());
  }

  /**
   * 处理参数校验异常
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Result<?> handleValidationException(MethodArgumentNotValidException e) {
    String message = e.getBindingResult().getFieldError() != null
        ? e.getBindingResult().getFieldError().getDefaultMessage()
        : "参数校验失败";
    return Result.error(400, message);
  }

  /**
   * 处理其他异常
   */
  @ExceptionHandler(Exception.class)
  public Result<?> handleException(Exception e) {
    e.printStackTrace();
    return Result.error("系统异常：" + e.getMessage());
  }
}
