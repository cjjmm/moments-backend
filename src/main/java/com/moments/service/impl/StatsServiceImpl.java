package com.moments.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moments.entity.Post;
import com.moments.entity.User;
import com.moments.mapper.PostMapper;
import com.moments.mapper.UserMapper;
import com.moments.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 统计分析服务实现类
 */
@Service
public class StatsServiceImpl implements StatsService {

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private PostMapper postMapper;

  @Override
  public Map<String, Object> getUserStats() {
    Map<String, Object> result = new HashMap<>();

    // 总用户数
    LambdaQueryWrapper<User> totalWrapper = new LambdaQueryWrapper<>();
    totalWrapper.eq(User::getStatus, 1);
    Long totalUsers = userMapper.selectCount(totalWrapper);
    result.put("totalUsers", totalUsers);

    // 今日新增用户
    LocalDateTime todayStart = LocalDate.now().atStartOfDay();
    LambdaQueryWrapper<User> todayWrapper = new LambdaQueryWrapper<>();
    todayWrapper.eq(User::getStatus, 1)
        .ge(User::getCreateTime, todayStart);
    Long newUsersToday = userMapper.selectCount(todayWrapper);
    result.put("newUsersToday", newUsersToday);

    // 本周新增用户
    LocalDateTime weekStart = LocalDate.now().minusDays(7).atStartOfDay();
    LambdaQueryWrapper<User> weekWrapper = new LambdaQueryWrapper<>();
    weekWrapper.eq(User::getStatus, 1)
        .ge(User::getCreateTime, weekStart);
    Long newUsersThisWeek = userMapper.selectCount(weekWrapper);
    result.put("newUsersThisWeek", newUsersThisWeek);

    // 活跃用户（有发帖的用户）
    LambdaQueryWrapper<Post> postWrapper = new LambdaQueryWrapper<>();
    postWrapper.eq(Post::getStatus, 1)
        .ge(Post::getCreateTime, weekStart);
    List<Post> weekPosts = postMapper.selectList(postWrapper);
    long activeUsers = weekPosts.stream()
        .map(Post::getUserId)
        .distinct()
        .count();
    result.put("activeUsers", activeUsers);

    return result;
  }

  @Override
  public Map<String, Object> getPostStats() {
    Map<String, Object> result = new HashMap<>();

    // 总帖子数
    LambdaQueryWrapper<Post> totalWrapper = new LambdaQueryWrapper<>();
    totalWrapper.eq(Post::getStatus, 1);
    Long totalPosts = postMapper.selectCount(totalWrapper);
    result.put("totalPosts", totalPosts);

    // 今日新增帖子
    LocalDateTime todayStart = LocalDate.now().atStartOfDay();
    LambdaQueryWrapper<Post> todayWrapper = new LambdaQueryWrapper<>();
    todayWrapper.eq(Post::getStatus, 1)
        .ge(Post::getCreateTime, todayStart);
    Long postsToday = postMapper.selectCount(todayWrapper);
    result.put("postsToday", postsToday);

    // 本周新增帖子
    LocalDateTime weekStart = LocalDate.now().minusDays(7).atStartOfDay();
    LambdaQueryWrapper<Post> weekWrapper = new LambdaQueryWrapper<>();
    weekWrapper.eq(Post::getStatus, 1)
        .ge(Post::getCreateTime, weekStart);
    Long postsThisWeek = postMapper.selectCount(weekWrapper);
    result.put("postsThisWeek", postsThisWeek);

    // 按类型统计
    Map<String, Long> postsByType = new HashMap<>();

    String[] types = { "IMAGE", "VIDEO", "TEXT" };
    for (String type : types) {
      LambdaQueryWrapper<Post> typeWrapper = new LambdaQueryWrapper<>();
      typeWrapper.eq(Post::getStatus, 1)
          .eq(Post::getMediaType, type);
      Long count = postMapper.selectCount(typeWrapper);
      postsByType.put(type, count);
    }
    result.put("postsByType", postsByType);

    return result;
  }

  @Override
  public List<Map<String, Object>> getDailyActiveStats(Integer days) {
    List<Map<String, Object>> result = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    for (int i = days - 1; i >= 0; i--) {
      LocalDate date = LocalDate.now().minusDays(i);
      LocalDateTime dayStart = date.atStartOfDay();
      LocalDateTime dayEnd = date.atTime(LocalTime.MAX);

      Map<String, Object> dayStats = new HashMap<>();
      dayStats.put("date", date.format(formatter));

      // 当日活跃用户（有发帖的用户数）
      LambdaQueryWrapper<Post> activeWrapper = new LambdaQueryWrapper<>();
      activeWrapper.eq(Post::getStatus, 1)
          .ge(Post::getCreateTime, dayStart)
          .le(Post::getCreateTime, dayEnd);
      List<Post> posts = postMapper.selectList(activeWrapper);
      long activeUsers = posts.stream()
          .map(Post::getUserId)
          .distinct()
          .count();
      dayStats.put("activeUsers", activeUsers);

      // 当日新发帖数
      dayStats.put("newPosts", posts.size());

      result.add(dayStats);
    }

    return result;
  }
}
