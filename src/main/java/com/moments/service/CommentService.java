package com.moments.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moments.dto.CommentCreateRequest;
import com.moments.dto.CommentVO;

import java.util.Map;

/**
 * 评论服务接口
 */
public interface CommentService {

  /**
   * 发表评论
   */
  Map<String, Object> createComment(Long userId, CommentCreateRequest request);

  /**
   * 获取帖子的评论列表
   */
  IPage<CommentVO> getCommentsByPostId(Long postId, Integer page, Integer size);

  /**
   * 删除评论
   */
  void deleteComment(Long userId, Long commentId);
}
