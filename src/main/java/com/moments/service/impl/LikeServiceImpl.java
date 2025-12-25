package com.moments.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moments.entity.Like;
import com.moments.entity.Post;
import com.moments.mapper.LikeMapper;
import com.moments.mapper.PostMapper;
import com.moments.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 点赞服务实现类
 */
@Service
public class LikeServiceImpl implements LikeService {

  @Autowired
  private LikeMapper likeMapper;

  @Autowired
  private PostMapper postMapper;

  @Override
  @Transactional
  public Like addLike(Long postId, Long userId) {
    // 检查是否已点赞
    Like existingLike = getLike(postId, userId);
    if (existingLike != null) {
      return existingLike;
    }

    // 创建点赞记录
    Like like = new Like();
    like.setPostId(postId);
    like.setUserId(userId);
    like.setCreateTime(LocalDateTime.now());
    likeMapper.insert(like);

    // 更新帖子点赞数
    Post post = postMapper.selectById(postId);
    if (post != null) {
      post.setLikeCount(post.getLikeCount() == null ? 1 : post.getLikeCount() + 1);
      postMapper.updateById(post);
    }

    return like;
  }

  @Override
  @Transactional
  public void removeLike(Long postId, Long userId) {
    // 查找点赞记录
    LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(Like::getPostId, postId)
        .eq(Like::getUserId, userId)
        .last("LIMIT 1");

    Like like = likeMapper.selectOne(wrapper);
    if (like != null) {
      likeMapper.deleteById(like.getLikeId());

      // 更新帖子点赞数
      Post post = postMapper.selectById(postId);
      if (post != null && post.getLikeCount() != null && post.getLikeCount() > 0) {
        post.setLikeCount(post.getLikeCount() - 1);
        postMapper.updateById(post);
      }
    }
  }

  @Override
  public boolean isLiked(Long postId, Long userId) {
    return getLike(postId, userId) != null;
  }

  @Override
  public Like getLike(Long postId, Long userId) {
    LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(Like::getPostId, postId)
        .eq(Like::getUserId, userId)
        .last("LIMIT 1");
    return likeMapper.selectOne(wrapper);
  }
}
