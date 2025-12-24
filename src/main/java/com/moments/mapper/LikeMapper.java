package com.moments.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moments.entity.Like;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 点赞Mapper接口
 */
@Mapper
public interface LikeMapper extends BaseMapper<Like> {

  /**
   * 检查用户是否已点赞
   */
  Like findByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);
}
