package com.moments.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果封装类
 * 
 * @param <T> 数据类型
 */
@Data
public class Result<T> implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 状态码
   */
  private Integer code;

  /**
   * 返回消息
   */
  private String message;

  /**
   * 返回数据
   */
  private T data;

  /**
   * 私有构造方法
   */
  private Result() {
  }

  /**
   * 成功返回
   */
  public static <T> Result<T> success() {
    Result<T> result = new Result<>();
    result.setCode(200);
    result.setMessage("success");
    return result;
  }

  /**
   * 成功返回，带消息
   */
  public static <T> Result<T> success(String message) {
    Result<T> result = new Result<>();
    result.setCode(200);
    result.setMessage(message);
    return result;
  }

  /**
   * 成功返回，带数据
   */
  public static <T> Result<T> success(T data) {
    Result<T> result = new Result<>();
    result.setCode(200);
    result.setMessage("success");
    result.setData(data);
    return result;
  }

  /**
   * 成功返回，带消息和数据
   */
  public static <T> Result<T> success(String message, T data) {
    Result<T> result = new Result<>();
    result.setCode(200);
    result.setMessage(message);
    result.setData(data);
    return result;
  }

  /**
   * 失败返回
   */
  public static <T> Result<T> error(String message) {
    Result<T> result = new Result<>();
    result.setCode(500);
    result.setMessage(message);
    return result;
  }

  /**
   * 失败返回，带状态码
   */
  public static <T> Result<T> error(Integer code, String message) {
    Result<T> result = new Result<>();
    result.setCode(code);
    result.setMessage(message);
    return result;
  }
}
