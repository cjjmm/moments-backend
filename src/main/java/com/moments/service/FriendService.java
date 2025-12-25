package com.moments.service;

import java.util.List;
import java.util.Map;

/**
 * 好友服务接口
 */
public interface FriendService {

  /**
   * 搜索用户
   */
  List<Map<String, Object>> searchUsers(String keyword, Long currentUserId);

  /**
   * 发送好友申请
   */
  void sendFriendRequest(Long fromUserId, Long toUserId, String message);

  /**
   * 获取收到的好友申请
   */
  List<Map<String, Object>> getReceivedRequests(Long userId);

  /**
   * 获取待处理申请数量
   */
  int getPendingRequestCount(Long userId);

  /**
   * 同意好友申请
   */
  void acceptRequest(Long requestId, Long userId);

  /**
   * 拒绝好友申请
   */
  void rejectRequest(Long requestId, Long userId);

  /**
   * 获取好友列表
   */
  List<Map<String, Object>> getFriendList(Long userId);

  /**
   * 删除好友
   */
  void deleteFriend(Long userId, Long friendId);
}
