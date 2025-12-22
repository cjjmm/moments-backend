package com.moments.controller;

import com.moments.common.Result;
import com.moments.dto.RatingCreateRequest;
import com.moments.service.RatingService;
import com.moments.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * 评分控制器
 */
@RestController
@RequestMapping("/api/v1/ratings")
@CrossOrigin(origins = "*")
public class RatingController {

  @Autowired
  private RatingService ratingService;

  @Autowired
  private JwtUtil jwtUtil;

  /**
   * 对帖子评分
   */
  @PostMapping
  public Result<Map<String, Object>> ratePost(@Valid @RequestBody RatingCreateRequest request,
      HttpServletRequest httpRequest) {
    Long userId = getUserIdFromToken(httpRequest);
    Map<String, Object> result = ratingService.ratePost(userId, request);
    return Result.success("评分成功", result);
  }

  /**
   * 获取用户对某帖子的评分
   */
  @GetMapping("/{postId}")
  public Result<Map<String, Object>> getUserRating(@PathVariable Long postId,
      HttpServletRequest httpRequest) {
    Long userId = getUserIdFromToken(httpRequest);
    Map<String, Object> result = ratingService.getUserRating(userId, postId);
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
