package com.moments.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moments.common.Result;
import com.moments.dto.UserVO;
import com.moments.service.PostService;
import com.moments.service.StatsService;
import com.moments.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员控制器
 */
@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "*")
public class AdminController {

  @Autowired
  private UserService userService;

  @Autowired
  private PostService postService;

  @Autowired
  private StatsService statsService;

  /**
   * 获取所有用户列表（分页）
   */
  @GetMapping("/users")
  public Result<Map<String, Object>> getAllUsers(
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "10") Integer size) {

    IPage<UserVO> userPage = userService.getAllUsers(page, size);

    Map<String, Object> result = new HashMap<>();
    result.put("total", userPage.getTotal());
    result.put("list", userPage.getRecords());

    return Result.success(result);
  }

  /**
   * 删除用户
   */
  @DeleteMapping("/users/{userId}")
  public Result<?> deleteUser(@PathVariable Long userId) {
    userService.deleteUser(userId);
    return Result.success("删除成功");
  }

  /**
   * 管理员删除帖子
   */
  @DeleteMapping("/posts/{postId}")
  public Result<?> deletePost(@PathVariable Long postId) {
    postService.adminDeletePost(postId);
    return Result.success("删除成功");
  }

  // ==================== 统计分析接口 ====================

  /**
   * 用户活跃度统计
   */
  @GetMapping("/stats/users")
  public Result<Map<String, Object>> getUserStats() {
    Map<String, Object> stats = statsService.getUserStats();
    return Result.success(stats);
  }

  /**
   * 内容统计
   */
  @GetMapping("/stats/posts")
  public Result<Map<String, Object>> getPostStats() {
    Map<String, Object> stats = statsService.getPostStats();
    return Result.success(stats);
  }

  /**
   * 日活跃趋势
   * 
   * @param days 天数（7或30），默认7
   */
  @GetMapping("/stats/daily-active")
  public Result<List<Map<String, Object>>> getDailyActiveStats(
      @RequestParam(defaultValue = "7") Integer days) {
    List<Map<String, Object>> stats = statsService.getDailyActiveStats(days);
    return Result.success(stats);
  }
}
