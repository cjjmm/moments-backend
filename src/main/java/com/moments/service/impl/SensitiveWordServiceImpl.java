package com.moments.service.impl;

import com.moments.entity.SensitiveWord;
import com.moments.mapper.SensitiveWordMapper;
import com.moments.service.SensitiveWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 敏感词服务实现类
 */
@Service
public class SensitiveWordServiceImpl implements SensitiveWordService {

  @Autowired
  private SensitiveWordMapper sensitiveWordMapper;

  // 敏感词缓存
  private Set<String> sensitiveWordSet = new HashSet<>();

  /**
   * 初始化时加载敏感词到内存
   */
  @PostConstruct
  public void init() {
    loadSensitiveWords();
  }

  /**
   * 从数据库加载敏感词
   */
  public void loadSensitiveWords() {
    List<SensitiveWord> words = sensitiveWordMapper.selectList(null);
    sensitiveWordSet.clear();
    for (SensitiveWord word : words) {
      sensitiveWordSet.add(word.getWord().toLowerCase());
    }
  }

  @Override
  public Map<String, Object> checkContent(String content) {
    Map<String, Object> result = new HashMap<>();

    List<String> foundWords = new ArrayList<>();
    String lowerContent = content.toLowerCase();

    // 检测敏感词
    for (String word : sensitiveWordSet) {
      if (lowerContent.contains(word)) {
        foundWords.add(word);
      }
    }

    result.put("hasSensitiveWord", !foundWords.isEmpty());
    result.put("sensitiveWords", foundWords);
    result.put("filteredContent", filterContent(content));

    return result;
  }

  @Override
  public String filterContent(String content) {
    if (content == null || content.isEmpty()) {
      return content;
    }

    String filteredContent = content;

    for (String word : sensitiveWordSet) {
      // 不区分大小写替换
      String replacement = generateReplacement(word.length());
      filteredContent = filteredContent.replaceAll("(?i)" + escapeRegex(word), replacement);
    }

    return filteredContent;
  }

  /**
   * 生成替换字符串（用*替换）
   */
  private String generateReplacement(int length) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
      sb.append("*");
    }
    return sb.toString();
  }

  /**
   * 转义正则表达式特殊字符
   */
  private String escapeRegex(String str) {
    return str.replaceAll("([\\\\\\[\\]{}().*+?^$|])", "\\\\$1");
  }
}
