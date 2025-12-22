package com.moments.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moments.dto.UserVO;
import com.moments.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

  /**
   * 分页查询用户列表（包含发帖数量）
   */
  @Select("SELECT u.user_id, u.username, u.nickname, u.avatar, u.create_time, " +
      "IFNULL(COUNT(p.post_id), 0) AS post_count " +
      "FROM user u " +
      "LEFT JOIN post p ON u.user_id = p.user_id AND p.status = 1 " +
      "WHERE u.status = 1 " +
      "GROUP BY u.user_id " +
      "ORDER BY u.create_time DESC")
  IPage<UserVO> selectUserListWithPostCount(IPage<UserVO> page);
}
