package com.moments.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moments.dto.TagVO;
import com.moments.entity.Tag;
import com.moments.mapper.TagMapper;
import com.moments.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签服务实现类
 */
@Service
public class TagServiceImpl implements TagService {

  @Autowired
  private TagMapper tagMapper;

  @Override
  public List<TagVO> getHotTags(Integer limit) {
    LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
    wrapper.orderByDesc(Tag::getUseCount)
        .last("LIMIT " + limit);

    List<Tag> tags = tagMapper.selectList(wrapper);

    return tags.stream()
        .map(this::convertToTagVO)
        .collect(Collectors.toList());
  }

  /**
   * 转换标签为VO
   */
  private TagVO convertToTagVO(Tag tag) {
    TagVO vo = new TagVO();
    vo.setTagName(tag.getTagName());
    vo.setUseCount(tag.getUseCount());
    return vo;
  }
}
