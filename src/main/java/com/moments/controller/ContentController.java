package com.moments.controller;

import com.moments.common.Result;
import com.moments.dto.ContentCheckRequest;
import com.moments.service.SensitiveWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 内容检测控制器
 */
@RestController
@RequestMapping("/api/v1/content")
@CrossOrigin(origins = "*")
public class ContentController {

  @Autowired
  private SensitiveWordService sensitiveWordService;

  /**
   * 敏感词检测
   */
  @PostMapping("/check")
  public Result<Map<String, Object>> checkContent(@Valid @RequestBody ContentCheckRequest request) {
    Map<String, Object> result = sensitiveWordService.checkContent(request.getContent());
    return Result.success(result);
  }
}
