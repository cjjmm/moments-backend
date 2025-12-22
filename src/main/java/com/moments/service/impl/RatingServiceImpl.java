package com.moments.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moments.dto.RatingCreateRequest;
import com.moments.entity.Post;
import com.moments.entity.Rating;
import com.moments.exception.BusinessException;
import com.moments.mapper.PostMapper;
import com.moments.mapper.RatingMapper;
import com.moments.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评分服务实现类
 */
@Service
public class RatingServiceImpl implements RatingService {

  @Autowired
  private RatingMapper ratingMapper;

  @Autowired
  private PostMapper postMapper;

  @Override
  @Transactional
  public Map<String, Object> ratePost(Long userId, RatingCreateRequest request) {
    // 检查帖子是否存在
    Post post = postMapper.selectById(request.getPostId());
    if (post == null || post.getStatus() == 0) {
      throw new BusinessException(404, "帖子不存在");
    }

    // 查找是否已评分
    LambdaQueryWrapper<Rating> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(Rating::getPostId, request.getPostId())
        .eq(Rating::getUserId, userId);
    Rating existingRating = ratingMapper.selectOne(wrapper);

    Rating rating;
    if (existingRating != null) {
      // 更新评分
      existingRating.setScore(request.getScore());
      ratingMapper.updateById(existingRating);
      rating = existingRating;
    } else {
      // 创建新评分
      rating = new Rating();
      rating.setPostId(request.getPostId());
      rating.setUserId(userId);
      rating.setScore(request.getScore());
      ratingMapper.insert(rating);

      // 更新帖子评分人数
      post.setRatingCount(post.getRatingCount() + 1);
    }

    // 更新帖子平均评分
    updatePostAvgRating(post);

    // 返回结果
    Map<String, Object> result = new HashMap<>();
    result.put("ratingId", rating.getRatingId());
    result.put("postId", rating.getPostId());
    result.put("score", rating.getScore());

    return result;
  }

  @Override
  public Map<String, Object> getUserRating(Long userId, Long postId) {
    // 检查帖子是否存在
    Post post = postMapper.selectById(postId);
    if (post == null || post.getStatus() == 0) {
      throw new BusinessException(404, "帖子不存在");
    }

    // 查找用户评分
    LambdaQueryWrapper<Rating> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(Rating::getPostId, postId)
        .eq(Rating::getUserId, userId);
    Rating rating = ratingMapper.selectOne(wrapper);

    Map<String, Object> result = new HashMap<>();
    if (rating != null) {
      result.put("ratingId", rating.getRatingId());
      result.put("postId", rating.getPostId());
      result.put("score", rating.getScore());
    } else {
      result.put("ratingId", null);
      result.put("postId", postId);
      result.put("score", null);
    }

    return result;
  }

  /**
   * 更新帖子平均评分
   */
  private void updatePostAvgRating(Post post) {
    // 查询该帖子所有评分
    LambdaQueryWrapper<Rating> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(Rating::getPostId, post.getPostId());
    List<Rating> ratings = ratingMapper.selectList(wrapper);

    if (ratings.isEmpty()) {
      post.setAvgRating(BigDecimal.ZERO);
    } else {
      // 计算平均分
      int totalScore = ratings.stream()
          .mapToInt(Rating::getScore)
          .sum();
      BigDecimal avg = BigDecimal.valueOf(totalScore)
          .divide(BigDecimal.valueOf(ratings.size()), 2, RoundingMode.HALF_UP);
      post.setAvgRating(avg);
      post.setRatingCount(ratings.size());
    }

    postMapper.updateById(post);
  }
}
