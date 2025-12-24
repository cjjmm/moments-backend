package com.moments.controller;

import com.moments.common.Result;
import com.moments.entity.Like;
import com.moments.service.LikeService;
import com.moments.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 点赞控制器
 */
@RestController
@RequestMapping("/api/v1/likes")
@CrossOrigin(origins = "*")
public class LikeController {

  @Autowired
  private LikeService likeService;

  @Autowired
  private JwtUtil jwtUtil;

  /**
   * 点赞
   */
  @PostMapping
  public Result<Map<String, Object>> addLike(@RequestBody Map<String, Long> request,
      HttpServletRequest httpRequest) {
    Long userId = getUserIdFromToken(httpRequest);
    Long postId = request.get("postId");

    if (postId == null) {
      return Result.error(400, "帖子ID不能为空");
    }

    Like like = likeService.addLike(postId, userId);

    Map<String, Object> result = new HashMap<>();
    result.put("likeId", like.getLikeId());
    result.put("postId", like.getPostId());
    result.put("liked", true);

    return Result.success("点赞成功", result);
  }

  /**
   * 取消点赞
   */
  @DeleteMapping("/{postId}")
  public Result<Void> removeLike(@PathVariable Long postId,
      HttpServletRequest httpRequest) {
    Long userId = getUserIdFromToken(httpRequest);
    likeService.removeLike(postId, userId);
    return Result.success("取消点赞成功", null);
  }

  /**
   * 获取点赞状态
   */
  @GetMapping("/{postId}")
  public Result<Map<String, Object>> getLikeStatus(@PathVariable Long postId,
      HttpServletRequest httpRequest) {
    Long userId = getUserIdFromToken(httpRequest);
    boolean isLiked = likeService.isLiked(postId, userId);

    Map<String, Object> result = new HashMap<>();
    result.put("postId", postId);
    result.put("liked", isLiked);

    return Result.success(result);
  }

  /**
   * 从Token中获取用户ID
   */
  private Long getUserIdFromToken(HttpServletRequest httpRequest) {
    String token = httpRequest.getHeader("Authorization");
    if (token != null && token.startsWith("Bearer ")) {
      token = token.substring(7);
    }
    return jwtUtil.getUserIdFromToken(token);
  }
}
