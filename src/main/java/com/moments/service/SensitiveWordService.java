package com.moments.service;

import java.util.Map;

/**
 * 敏感词服务接口
 */
public interface SensitiveWordService {

  /**
   * 检测内容是否包含敏感词
   * 
   * @param content 待检测内容
   * @return 检测结果（hasSensitiveWord, sensitiveWords, filteredContent）
   */
  Map<String, Object> checkContent(String content);

  /**
   * 过滤敏感词（将敏感词替换为*）
   * 
   * @param content 原始内容
   * @return 过滤后的内容
   */
  String filterContent(String content);
}
