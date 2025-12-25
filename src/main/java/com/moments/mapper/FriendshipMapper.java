package com.moments.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moments.entity.Friendship;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 好友关系Mapper
 */
@Mapper
public interface FriendshipMapper extends BaseMapper<Friendship> {

  /**
   * 获取好友列表（包含好友信息）
   */
  @Select("SELECT f.id, f.friend_id, f.create_time, " +
      "u.username, u.nickname, u.avatar " +
      "FROM friendship f " +
      "JOIN user u ON f.friend_id = u.user_id " +
      "WHERE f.user_id = #{userId} " +
      "ORDER BY f.create_time DESC")
  List<Map<String, Object>> getFriendList(@Param("userId") Long userId);

  /**
   * 检查是否已是好友
   */
  @Select("SELECT COUNT(*) FROM friendship WHERE user_id = #{userId} AND friend_id = #{friendId}")
  int isFriend(@Param("userId") Long userId, @Param("friendId") Long friendId);
}
