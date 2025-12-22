package com.moments.service;

import java.util.List;
import java.util.Map;

/**
 * 统计分析服务接口
 */
public interface StatsService {

  /**
   * 获取用户统计数据
   */
  Map<String, Object> getUserStats();

  /**
   * 获取帖子统计数据
   */
  Map<String, Object> getPostStats();

  /**
   * 获取日活跃趋势
   * 
   * @param days 天数（7或30）
   */
  List<Map<String, Object>> getDailyActiveStats(Integer days);
}
