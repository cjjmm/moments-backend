package com.moments.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 文件服务接口
 */
public interface FileService {

  /**
   * 上传文件
   * 
   * @param file 文件
   * @param type 文件类型（IMAGE/VIDEO）
   * @return 文件URL和文件名
   */
  Map<String, Object> uploadFile(MultipartFile file, String type);
}
