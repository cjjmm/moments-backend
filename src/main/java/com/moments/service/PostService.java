package com.moments.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moments.dto.PostCreateRequest;
import com.moments.dto.PostUpdateRequest;
import com.moments.dto.PostVO;

import java.util.Map;

/**
 * 帖子服务接口
 */
public interface PostService {

  /**
   * 发布帖子
   */
  Map<String, Object> createPost(Long userId, PostCreateRequest request);

  /**
   * 获取帖子列表（分页，支持筛选）
   */
  IPage<PostVO> getPostList(Integer page, Integer size, String tag, String startDate, String endDate);

  /**
   * 获取用户自己的帖子
   */
  IPage<PostVO> getMyPosts(Long userId, Integer page, Integer size);

  /**
   * 获取帖子详情
   */
  PostVO getPostDetail(Long postId);

  /**
   * 修改帖子
   */
  void updatePost(Long userId, Long postId, PostUpdateRequest request);

  /**
   * 删除帖子（用户删除自己的）
   */
  void deletePost(Long userId, Long postId);

  /**
   * 管理员删除帖子
   */
  void adminDeletePost(Long postId);

  /**
   * 搜索帖子
   */
  IPage<PostVO> searchPosts(String keyword, Integer page, Integer size);
}
