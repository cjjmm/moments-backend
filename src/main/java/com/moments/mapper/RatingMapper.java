package com.moments.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moments.entity.Rating;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评分Mapper接口
 */
@Mapper
public interface RatingMapper extends BaseMapper<Rating> {
}
