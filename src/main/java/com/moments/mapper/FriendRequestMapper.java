package com.moments.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moments.entity.FriendRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 好友申请Mapper
 */
@Mapper
public interface FriendRequestMapper extends BaseMapper<FriendRequest> {

  /**
   * 获取收到的好友申请（包含申请人信息）
   */
  @Select("SELECT fr.request_id, fr.from_user_id, fr.message, fr.status, fr.create_time, " +
      "u.username, u.nickname, u.avatar " +
      "FROM friend_request fr " +
      "JOIN user u ON fr.from_user_id = u.user_id " +
      "WHERE fr.to_user_id = #{userId} AND fr.status = 0 " +
      "ORDER BY fr.create_time DESC")
  List<Map<String, Object>> getReceivedRequests(@Param("userId") Long userId);

  /**
   * 检查是否已发送过申请
   */
  @Select("SELECT COUNT(*) FROM friend_request " +
      "WHERE from_user_id = #{fromUserId} AND to_user_id = #{toUserId} AND status = 0")
  int countPendingRequest(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId);
}
