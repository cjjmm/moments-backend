package com.moments.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moments.common.Result;
import com.moments.dto.PostCreateRequest;
import com.moments.dto.PostUpdateRequest;
import com.moments.dto.PostVO;
import com.moments.service.PostService;
import com.moments.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 帖子控制器
 */
@RestController
@RequestMapping("/api/v1/posts")
@CrossOrigin(origins = "*")
public class PostController {

  @Autowired
  private PostService postService;

  @Autowired
  private JwtUtil jwtUtil;

  /**
   * 发布帖子
   */
  @PostMapping
  public Result<Map<String, Object>> createPost(@Valid @RequestBody PostCreateRequest request,
      HttpServletRequest httpRequest) {
    Long userId = getUserIdFromToken(httpRequest);
    Map<String, Object> result = postService.createPost(userId, request);
    return Result.success("发布成功", result);
  }

  /**
   * 获取帖子列表（首页）
   */
  @GetMapping
  public Result<Map<String, Object>> getPostList(
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "10") Integer size,
      @RequestParam(required = false) String tag,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate) {

    IPage<PostVO> postPage = postService.getPostList(page, size, tag, startDate, endDate);

    Map<String, Object> result = new HashMap<>();
    result.put("total", postPage.getTotal());
    result.put("list", postPage.getRecords());

    return Result.success(result);
  }

  /**
   * 获取用户自己的帖子
   */
  @GetMapping("/my")
  public Result<Map<String, Object>> getMyPosts(
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "10") Integer size,
      HttpServletRequest httpRequest) {

    Long userId = getUserIdFromToken(httpRequest);
    IPage<PostVO> postPage = postService.getMyPosts(userId, page, size);

    Map<String, Object> result = new HashMap<>();
    result.put("total", postPage.getTotal());
    result.put("list", postPage.getRecords());

    return Result.success(result);
  }

  /**
   * 获取帖子详情
   */
  @GetMapping("/{postId}")
  public Result<PostVO> getPostDetail(@PathVariable Long postId) {
    PostVO postVO = postService.getPostDetail(postId);
    return Result.success(postVO);
  }

  /**
   * 修改帖子
   */
  @PutMapping("/{postId}")
  public Result<?> updatePost(@PathVariable Long postId,
      @RequestBody PostUpdateRequest request,
      HttpServletRequest httpRequest) {
    Long userId = getUserIdFromToken(httpRequest);
    postService.updatePost(userId, postId, request);
    return Result.success("修改成功");
  }

  /**
   * 删除帖子
   */
  @DeleteMapping("/{postId}")
  public Result<?> deletePost(@PathVariable Long postId, HttpServletRequest httpRequest) {
    Long userId = getUserIdFromToken(httpRequest);
    postService.deletePost(userId, postId);
    return Result.success("删除成功");
  }

  /**
   * 搜索帖子
   */
  @GetMapping("/search")
  public Result<Map<String, Object>> searchPosts(
      @RequestParam String keyword,
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "10") Integer size) {

    IPage<PostVO> postPage = postService.searchPosts(keyword, page, size);

    Map<String, Object> result = new HashMap<>();
    result.put("total", postPage.getTotal());
    result.put("list", postPage.getRecords());

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
