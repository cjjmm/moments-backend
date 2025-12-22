package com.moments.service;

import com.moments.dto.TagVO;

import java.util.List;

/**
 * 标签服务接口
 */
public interface TagService {

  /**
   * 获取热门标签
   * 
   * @param limit 返回数量
   */
  List<TagVO> getHotTags(Integer limit);
}
