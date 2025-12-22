package com.moments.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moments.entity.Post;
import org.apache.ibatis.annotations.Mapper;

/**
 * 帖子Mapper接口
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {
}
