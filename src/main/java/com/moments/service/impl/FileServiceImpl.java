package com.moments.service.impl;

import com.moments.exception.BusinessException;
import com.moments.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 文件服务实现类
 */
@Service
public class FileServiceImpl implements FileService {

  @Value("${file.upload-dir}")
  private String uploadDir;

  @Value("${file.base-url}")
  private String baseUrl;

  // 允许的图片类型
  private static final List<String> IMAGE_TYPES = Arrays.asList(
      "image/jpeg", "image/png", "image/gif", "image/webp");

  // 允许的视频类型
  private static final List<String> VIDEO_TYPES = Arrays.asList(
      "video/mp4", "video/mpeg", "video/quicktime", "video/x-msvideo");

  // 最大图片大小：10MB
  private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024;

  // 最大视频大小：50MB
  private static final long MAX_VIDEO_SIZE = 50 * 1024 * 1024;

  @Override
  public Map<String, Object> uploadFile(MultipartFile file, String type) {
    if (file == null || file.isEmpty()) {
      throw new BusinessException(400, "请选择要上传的文件");
    }

    String contentType = file.getContentType();
    String originalFilename = file.getOriginalFilename();

    // 验证文件类型
    if ("IMAGE".equalsIgnoreCase(type)) {
      if (!IMAGE_TYPES.contains(contentType)) {
        throw new BusinessException(400, "不支持的图片格式，仅支持 JPG、PNG、GIF、WEBP");
      }
      if (file.getSize() > MAX_IMAGE_SIZE) {
        throw new BusinessException(400, "图片大小不能超过10MB");
      }
    } else if ("VIDEO".equalsIgnoreCase(type)) {
      if (!VIDEO_TYPES.contains(contentType)) {
        throw new BusinessException(400, "不支持的视频格式，仅支持 MP4、MPEG、MOV、AVI");
      }
      if (file.getSize() > MAX_VIDEO_SIZE) {
        throw new BusinessException(400, "视频大小不能超过50MB");
      }
    } else {
      throw new BusinessException(400, "文件类型参数错误，仅支持 IMAGE 或 VIDEO");
    }

    // 生成文件名
    String extension = getFileExtension(originalFilename);
    String newFilename = UUID.randomUUID().toString().replace("-", "") + "." + extension;

    // 按日期分目录存储
    String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    String relativePath = type.toLowerCase() + "/" + datePath + "/" + newFilename;
    String fullPath = uploadDir + relativePath;

    // 创建目录
    File destFile = new File(fullPath);
    if (!destFile.getParentFile().exists()) {
      destFile.getParentFile().mkdirs();
    }

    // 保存文件
    try {
      file.transferTo(destFile);
    } catch (IOException e) {
      throw new BusinessException(500, "文件上传失败：" + e.getMessage());
    }

    // 返回结果
    Map<String, Object> result = new HashMap<>();
    result.put("url", baseUrl + relativePath);
    result.put("filename", newFilename);

    return result;
  }

  /**
   * 获取文件扩展名
   */
  private String getFileExtension(String filename) {
    if (filename == null || !filename.contains(".")) {
      return "unknown";
    }
    return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
  }
}
