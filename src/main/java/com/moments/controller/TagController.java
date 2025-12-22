package com.moments.controller;

import com.moments.common.Result;
import com.moments.dto.TagVO;
import com.moments.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签控制器
 */
@RestController
@RequestMapping("/api/v1/tags")
@CrossOrigin(origins = "*")
public class TagController {

  @Autowired
  private TagService tagService;

  /**
   * 获取热门标签
   * 
   * @param limit 返回数量，默认10
   */
  @GetMapping("/hot")
  public Result<List<TagVO>> getHotTags(
      @RequestParam(defaultValue = "10") Integer limit) {

    List<TagVO> tags = tagService.getHotTags(limit);
    return Result.success(tags);
  }
}
