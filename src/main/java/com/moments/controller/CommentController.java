package com.moments.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moments.common.Result;
import com.moments.dto.CommentCreateRequest;
import com.moments.dto.CommentVO;
import com.moments.service.CommentService;
import com.moments.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 评论控制器
 */
@RestController
@RequestMapping("/api/v1/comments")
@CrossOrigin(origins = "*")
public class CommentController {

  @Autowired
  private CommentService commentService;

  @Autowired
  private JwtUtil jwtUtil;

  /**
   * 发表评论
   */
  @PostMapping
  public Result<Map<String, Object>> createComment(@Valid @RequestBody CommentCreateRequest request,
      HttpServletRequest httpRequest) {
    Long userId = getUserIdFromToken(httpRequest);
    Map<String, Object> result = commentService.createComment(userId, request);
    return Result.success("评论成功", result);
  }

  /**
   * 获取帖子的评论列表
   */
  @GetMapping("/{postId}")
  public Result<Map<String, Object>> getCommentsByPostId(
      @PathVariable Long postId,
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "10") Integer size) {

    IPage<CommentVO> commentPage = commentService.getCommentsByPostId(postId, page, size);

    Map<String, Object> result = new HashMap<>();
    result.put("total", commentPage.getTotal());
    result.put("list", commentPage.getRecords());

    return Result.success(result);
  }

  /**
   * 删除评论
   */
  @DeleteMapping("/{commentId}")
  public Result<?> deleteComment(@PathVariable Long commentId, HttpServletRequest httpRequest) {
    Long userId = getUserIdFromToken(httpRequest);
    commentService.deleteComment(userId, commentId);
    return Result.success("删除成功");
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
