package com.moments.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moments.entity.FriendRequest;
import com.moments.entity.Friendship;
import com.moments.entity.User;
import com.moments.exception.BusinessException;
import com.moments.mapper.FriendRequestMapper;
import com.moments.mapper.FriendshipMapper;
import com.moments.mapper.UserMapper;
import com.moments.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 好友服务实现类
 */
@Service
public class FriendServiceImpl implements FriendService {

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private FriendRequestMapper friendRequestMapper;

  @Autowired
  private FriendshipMapper friendshipMapper;

  @Override
  public List<Map<String, Object>> searchUsers(String keyword, Long currentUserId) {
    if (keyword == null || keyword.trim().isEmpty()) {
      return new ArrayList<>();
    }

    // 搜索用户名或昵称包含关键词的用户
    LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
    wrapper.and(w -> w.like(User::getUsername, keyword)
        .or()
        .like(User::getNickname, keyword))
        .ne(User::getUserId, currentUserId) // 排除自己
        .eq(User::getStatus, 1) // 只搜索正常状态的用户
        .last("LIMIT 20");

    List<User> users = userMapper.selectList(wrapper);
    List<Map<String, Object>> result = new ArrayList<>();

    for (User user : users) {
      Map<String, Object> userMap = new HashMap<>();
      userMap.put("userId", user.getUserId());
      userMap.put("username", user.getUsername());
      userMap.put("nickname", user.getNickname());
      userMap.put("avatar", user.getAvatar());

      // 检查是否已是好友
      int isFriend = friendshipMapper.isFriend(currentUserId, user.getUserId());
      userMap.put("isFriend", isFriend > 0);

      // 检查是否已发送申请
      int hasPending = friendRequestMapper.countPendingRequest(currentUserId, user.getUserId());
      userMap.put("hasPendingRequest", hasPending > 0);

      result.add(userMap);
    }

    return result;
  }

  @Override
  public void sendFriendRequest(Long fromUserId, Long toUserId, String message) {
    // 检查是否给自己发送
    if (fromUserId.equals(toUserId)) {
      throw new BusinessException(400, "不能添加自己为好友");
    }

    // 检查用户是否存在
    User toUser = userMapper.selectById(toUserId);
    if (toUser == null) {
      throw new BusinessException(404, "用户不存在");
    }

    // 检查是否已经是好友
    int isFriend = friendshipMapper.isFriend(fromUserId, toUserId);
    if (isFriend > 0) {
      throw new BusinessException(400, "已经是好友了");
    }

    // 检查是否已发送过申请
    int hasPending = friendRequestMapper.countPendingRequest(fromUserId, toUserId);
    if (hasPending > 0) {
      throw new BusinessException(400, "已发送过申请，请等待对方处理");
    }

    // 创建申请
    FriendRequest request = new FriendRequest();
    request.setFromUserId(fromUserId);
    request.setToUserId(toUserId);
    request.setMessage(message);
    request.setStatus(0);

    friendRequestMapper.insert(request);
  }

  @Override
  public List<Map<String, Object>> getReceivedRequests(Long userId) {
    return friendRequestMapper.getReceivedRequests(userId);
  }

  @Override
  public int getPendingRequestCount(Long userId) {
    LambdaQueryWrapper<FriendRequest> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(FriendRequest::getToUserId, userId)
        .eq(FriendRequest::getStatus, 0);
    return Math.toIntExact(friendRequestMapper.selectCount(wrapper));
  }

  @Override
  @Transactional
  public void acceptRequest(Long requestId, Long userId) {
    FriendRequest request = friendRequestMapper.selectById(requestId);
    if (request == null) {
      throw new BusinessException(404, "申请不存在");
    }

    if (!request.getToUserId().equals(userId)) {
      throw new BusinessException(403, "无权处理此申请");
    }

    if (request.getStatus() != 0) {
      throw new BusinessException(400, "申请已处理");
    }

    // 更新申请状态
    request.setStatus(1);
    friendRequestMapper.updateById(request);

    // 创建双向好友关系
    Friendship friendship1 = new Friendship();
    friendship1.setUserId(request.getFromUserId());
    friendship1.setFriendId(request.getToUserId());
    friendshipMapper.insert(friendship1);

    Friendship friendship2 = new Friendship();
    friendship2.setUserId(request.getToUserId());
    friendship2.setFriendId(request.getFromUserId());
    friendshipMapper.insert(friendship2);
  }

  @Override
  public void rejectRequest(Long requestId, Long userId) {
    FriendRequest request = friendRequestMapper.selectById(requestId);
    if (request == null) {
      throw new BusinessException(404, "申请不存在");
    }

    if (!request.getToUserId().equals(userId)) {
      throw new BusinessException(403, "无权处理此申请");
    }

    if (request.getStatus() != 0) {
      throw new BusinessException(400, "申请已处理");
    }

    // 更新申请状态
    request.setStatus(2);
    friendRequestMapper.updateById(request);
  }

  @Override
  public List<Map<String, Object>> getFriendList(Long userId) {
    return friendshipMapper.getFriendList(userId);
  }

  @Override
  @Transactional
  public void deleteFriend(Long userId, Long friendId) {
    // 删除双向好友关系
    LambdaQueryWrapper<Friendship> wrapper1 = new LambdaQueryWrapper<>();
    wrapper1.eq(Friendship::getUserId, userId).eq(Friendship::getFriendId, friendId);
    friendshipMapper.delete(wrapper1);

    LambdaQueryWrapper<Friendship> wrapper2 = new LambdaQueryWrapper<>();
    wrapper2.eq(Friendship::getUserId, friendId).eq(Friendship::getFriendId, userId);
    friendshipMapper.delete(wrapper2);
  }
}
