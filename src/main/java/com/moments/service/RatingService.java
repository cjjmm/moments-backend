package com.moments.service;

import com.moments.dto.RatingCreateRequest;

import java.util.Map;

/**
 * 评分服务接口
 */
public interface RatingService {

  /**
   * 对帖子评分（首次评分或更新评分）
   */
  Map<String, Object> ratePost(Long userId, RatingCreateRequest request);

  /**
   * 获取用户对某帖子的评分
   */
  Map<String, Object> getUserRating(Long userId, Long postId);
}
