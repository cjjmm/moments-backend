package com.moments.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moments.entity.PostTag;
import org.apache.ibatis.annotations.Mapper;

/**
 * 帖子-标签关联Mapper接口
 */
@Mapper
public interface PostTagMapper extends BaseMapper<PostTag> {
}
