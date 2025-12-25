package com.moments.controller;

import com.moments.common.Result;
import com.moments.service.FriendService;
import com.moments.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 好友控制器
 */
@RestController
@RequestMapping("/api/v1/friends")
@CrossOrigin(origins = "*")
public class FriendController {

  @Autowired
  private FriendService friendService;

  @Autowired
  private JwtUtil jwtUtil;

  /**
   * 获取当前用户ID
   */
  private Long getCurrentUserId(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (token != null && token.startsWith("Bearer ")) {
      token = token.substring(7);
    }
    return jwtUtil.getUserIdFromToken(token);
  }

  /**
   * 搜索用户
   */
  @GetMapping("/search")
  public Result<List<Map<String, Object>>> searchUsers(
      @RequestParam String keyword,
      HttpServletRequest request) {
    Long userId = getCurrentUserId(request);
    List<Map<String, Object>> users = friendService.searchUsers(keyword, userId);
    return Result.success(users);
  }

  /**
   * 发送好友申请
   */
  @PostMapping("/request")
  public Result<?> sendFriendRequest(
      @RequestBody Map<String, Object> body,
      HttpServletRequest request) {
    Long userId = getCurrentUserId(request);
    Long toUserId = Long.valueOf(body.get("toUserId").toString());
    String message = body.get("message") != null ? body.get("message").toString() : "";

    friendService.sendFriendRequest(userId, toUserId, message);
    return Result.success("好友申请已发送");
  }

  /**
   * 获取收到的好友申请
   */
  @GetMapping("/requests")
  public Result<List<Map<String, Object>>> getReceivedRequests(HttpServletRequest request) {
    Long userId = getCurrentUserId(request);
    List<Map<String, Object>> requests = friendService.getReceivedRequests(userId);
    return Result.success(requests);
  }

  /**
   * 获取待处理申请数量
   */
  @GetMapping("/requests/count")
  public Result<Integer> getPendingRequestCount(HttpServletRequest request) {
    Long userId = getCurrentUserId(request);
    int count = friendService.getPendingRequestCount(userId);
    return Result.success(count);
  }

  /**
   * 同意好友申请
   */
  @PutMapping("/requests/{requestId}/accept")
  public Result<?> acceptRequest(
      @PathVariable Long requestId,
      HttpServletRequest request) {
    Long userId = getCurrentUserId(request);
    friendService.acceptRequest(requestId, userId);
    return Result.success("已添加为好友");
  }

  /**
   * 拒绝好友申请
   */
  @PutMapping("/requests/{requestId}/reject")
  public Result<?> rejectRequest(
      @PathVariable Long requestId,
      HttpServletRequest request) {
    Long userId = getCurrentUserId(request);
    friendService.rejectRequest(requestId, userId);
    return Result.success("已拒绝申请");
  }

  /**
   * 获取好友列表
   */
  @GetMapping
  public Result<List<Map<String, Object>>> getFriendList(HttpServletRequest request) {
    Long userId = getCurrentUserId(request);
    List<Map<String, Object>> friends = friendService.getFriendList(userId);
    return Result.success(friends);
  }

  /**
   * 删除好友
   */
  @DeleteMapping("/{friendId}")
  public Result<?> deleteFriend(
      @PathVariable Long friendId,
      HttpServletRequest request) {
    Long userId = getCurrentUserId(request);
    friendService.deleteFriend(userId, friendId);
    return Result.success("已删除好友");
  }
}
