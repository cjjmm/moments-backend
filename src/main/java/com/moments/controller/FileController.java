package com.moments.controller;

import com.moments.common.Result;
import com.moments.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/api/v1/upload")
@CrossOrigin(origins = "*")
public class FileController {

  @Autowired
  private FileService fileService;

  /**
   * 上传图片/视频
   * 
   * @param file 文件
   * @param type 文件类型（IMAGE/VIDEO）
   */
  @PostMapping
  public Result<Map<String, Object>> uploadFile(
      @RequestParam("file") MultipartFile file,
      @RequestParam(value = "type", defaultValue = "IMAGE") String type) {

    Map<String, Object> result = fileService.uploadFile(file, type);
    return Result.success("上传成功", result);
  }
}
