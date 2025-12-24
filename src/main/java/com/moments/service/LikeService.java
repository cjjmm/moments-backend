package com.moments.service;

import com.moments.entity.Like;

/**
 * 点赞服务接口
 */
public interface LikeService {

  /**
   * 点赞
   * 
   * @param postId 帖子ID
   * @param userId 用户ID
   * @return 点赞记录
   */
  Like addLike(Long postId, Long userId);

  /**
   * 取消点赞
   * 
   * @param postId 帖子ID
   * @param userId 用户ID
   */
  void removeLike(Long postId, Long userId);

  /**
   * 检查是否已点赞
   * 
   * @param postId 帖子ID
   * @param userId 用户ID
   * @return 是否已点赞
   */
  boolean isLiked(Long postId, Long userId);

  /**
   * 获取用户对某帖子的点赞记录
   * 
   * @param postId 帖子ID
   * @param userId 用户ID
   * @return 点赞记录
   */
  Like getLike(Long postId, Long userId);
}
