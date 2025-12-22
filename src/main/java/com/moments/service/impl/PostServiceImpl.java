package com.moments.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moments.dto.PostCreateRequest;
import com.moments.dto.PostUpdateRequest;
import com.moments.dto.PostVO;
import com.moments.entity.Media;
import com.moments.entity.Post;
import com.moments.entity.PostTag;
import com.moments.entity.Tag;
import com.moments.entity.User;
import com.moments.exception.BusinessException;
import com.moments.mapper.MediaMapper;
import com.moments.mapper.PostMapper;
import com.moments.mapper.PostTagMapper;
import com.moments.mapper.TagMapper;
import com.moments.mapper.UserMapper;
import com.moments.service.PostService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 帖子服务实现类
 */
@Service
public class PostServiceImpl implements PostService {

  @Autowired
  private PostMapper postMapper;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private MediaMapper mediaMapper;

  @Autowired
  private TagMapper tagMapper;

  @Autowired
  private PostTagMapper postTagMapper;

  @Override
  @Transactional
  public Map<String, Object> createPost(Long userId, PostCreateRequest request) {
    // 创建帖子
    Post post = new Post();
    post.setUserId(userId);
    post.setContent(request.getContent());
    post.setMediaType(request.getMediaType());
    post.setLikeCount(0);
    post.setCommentCount(0);
    post.setViewCount(0);
    post.setRatingCount(0);
    post.setStatus(1);

    postMapper.insert(post);

    // 保存媒体文件
    if (request.getMediaUrls() != null && !request.getMediaUrls().isEmpty()) {
      int order = 0;
      for (String url : request.getMediaUrls()) {
        Media media = new Media();
        media.setPostId(post.getPostId());
        media.setMediaUrl(url);
        media.setMediaType(request.getMediaType());
        media.setSortOrder(order++);
        mediaMapper.insert(media);
      }
    }

    // 处理标签
    if (request.getTags() != null && !request.getTags().isEmpty()) {
      for (String tagName : request.getTags()) {
        // 查找或创建标签
        Tag tag = findOrCreateTag(tagName);

        // 创建帖子-标签关联
        PostTag postTag = new PostTag();
        postTag.setPostId(post.getPostId());
        postTag.setTagId(tag.getTagId());
        postTagMapper.insert(postTag);

        // 更新标签使用次数
        tag.setUseCount(tag.getUseCount() + 1);
        tagMapper.updateById(tag);
      }
    }

    // 返回结果
    Map<String, Object> result = new HashMap<>();
    result.put("postId", post.getPostId());
    result.put("content", post.getContent());
    result.put("createTime", post.getCreateTime());

    return result;
  }

  @Override
  public IPage<PostVO> getPostList(Integer page, Integer size, String tag, String startDate, String endDate) {
    Page<Post> pageParam = new Page<>(page, size);

    LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(Post::getStatus, 1);

    // 日期筛选
    if (startDate != null && !startDate.isEmpty()) {
      LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
      wrapper.ge(Post::getCreateTime, start);
    }
    if (endDate != null && !endDate.isEmpty()) {
      LocalDateTime end = LocalDate.parse(endDate).atTime(LocalTime.MAX);
      wrapper.le(Post::getCreateTime, end);
    }

    // 标签筛选
    if (tag != null && !tag.isEmpty()) {
      // 查找标签对应的帖子ID
      List<Long> postIds = getPostIdsByTagName(tag);
      if (postIds.isEmpty()) {
        return new Page<>(page, size);
      }
      wrapper.in(Post::getPostId, postIds);
    }

    wrapper.orderByDesc(Post::getCreateTime);

    IPage<Post> postPage = postMapper.selectPage(pageParam, wrapper);

    return convertToPostVOPage(postPage);
  }

  @Override
  public IPage<PostVO> getMyPosts(Long userId, Integer page, Integer size) {
    Page<Post> pageParam = new Page<>(page, size);

    LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(Post::getUserId, userId)
        .eq(Post::getStatus, 1)
        .orderByDesc(Post::getCreateTime);

    IPage<Post> postPage = postMapper.selectPage(pageParam, wrapper);

    return convertToPostVOPage(postPage);
  }

  @Override
  public PostVO getPostDetail(Long postId) {
    Post post = postMapper.selectById(postId);
    if (post == null || post.getStatus() == 0) {
      throw new BusinessException(404, "帖子不存在");
    }

    // 增加浏览数
    post.setViewCount(post.getViewCount() + 1);
    postMapper.updateById(post);

    return convertToPostVO(post);
  }

  @Override
  @Transactional
  public void updatePost(Long userId, Long postId, PostUpdateRequest request) {
    Post post = postMapper.selectById(postId);
    if (post == null || post.getStatus() == 0) {
      throw new BusinessException(404, "帖子不存在");
    }

    // 验证是否是帖子作者
    if (!post.getUserId().equals(userId)) {
      throw new BusinessException(403, "无权修改此帖子");
    }

    // 更新内容
    if (request.getContent() != null) {
      post.setContent(request.getContent());
    }
    postMapper.updateById(post);

    // 更新标签
    if (request.getTags() != null) {
      // 删除旧的标签关联
      LambdaQueryWrapper<PostTag> deleteWrapper = new LambdaQueryWrapper<>();
      deleteWrapper.eq(PostTag::getPostId, postId);
      postTagMapper.delete(deleteWrapper);

      // 添加新标签
      for (String tagName : request.getTags()) {
        Tag tag = findOrCreateTag(tagName);

        PostTag postTag = new PostTag();
        postTag.setPostId(postId);
        postTag.setTagId(tag.getTagId());
        postTagMapper.insert(postTag);
      }
    }
  }

  @Override
  @Transactional
  public void deletePost(Long userId, Long postId) {
    Post post = postMapper.selectById(postId);
    if (post == null || post.getStatus() == 0) {
      throw new BusinessException(404, "帖子不存在");
    }

    // 验证是否是帖子作者
    if (!post.getUserId().equals(userId)) {
      throw new BusinessException(403, "无权删除此帖子");
    }

    // 逻辑删除
    post.setStatus(0);
    postMapper.updateById(post);
  }

  @Override
  @Transactional
  public void adminDeletePost(Long postId) {
    Post post = postMapper.selectById(postId);
    if (post == null || post.getStatus() == 0) {
      throw new BusinessException(404, "帖子不存在");
    }

    // 逻辑删除
    post.setStatus(0);
    postMapper.updateById(post);
  }

  @Override
  public IPage<PostVO> searchPosts(String keyword, Integer page, Integer size) {
    Page<Post> pageParam = new Page<>(page, size);

    LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(Post::getStatus, 1)
        .like(Post::getContent, keyword)
        .orderByDesc(Post::getCreateTime);

    IPage<Post> postPage = postMapper.selectPage(pageParam, wrapper);

    return convertToPostVOPage(postPage);
  }

  // ==================== 辅助方法 ====================

  /**
   * 查找或创建标签
   */
  private Tag findOrCreateTag(String tagName) {
    LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(Tag::getTagName, tagName);
    Tag tag = tagMapper.selectOne(wrapper);

    if (tag == null) {
      tag = new Tag();
      tag.setTagName(tagName);
      tag.setUseCount(0);
      tagMapper.insert(tag);
    }

    return tag;
  }

  /**
   * 根据标签名获取帖子ID列表
   */
  private List<Long> getPostIdsByTagName(String tagName) {
    LambdaQueryWrapper<Tag> tagWrapper = new LambdaQueryWrapper<>();
    tagWrapper.eq(Tag::getTagName, tagName);
    Tag tag = tagMapper.selectOne(tagWrapper);

    if (tag == null) {
      return new ArrayList<>();
    }

    LambdaQueryWrapper<PostTag> postTagWrapper = new LambdaQueryWrapper<>();
    postTagWrapper.eq(PostTag::getTagId, tag.getTagId());
    List<PostTag> postTags = postTagMapper.selectList(postTagWrapper);

    return postTags.stream()
        .map(PostTag::getPostId)
        .collect(Collectors.toList());
  }

  /**
   * 转换帖子分页数据为VO分页数据
   */
  private IPage<PostVO> convertToPostVOPage(IPage<Post> postPage) {
    Page<PostVO> voPage = new Page<>(postPage.getCurrent(), postPage.getSize(), postPage.getTotal());

    List<PostVO> voList = postPage.getRecords().stream()
        .map(this::convertToPostVO)
        .collect(Collectors.toList());

    voPage.setRecords(voList);
    return voPage;
  }

  /**
   * 转换帖子为VO
   */
  private PostVO convertToPostVO(Post post) {
    PostVO vo = new PostVO();
    BeanUtils.copyProperties(post, vo);

    // 获取用户信息
    User user = userMapper.selectById(post.getUserId());
    if (user != null) {
      vo.setUsername(user.getUsername());
      vo.setNickname(user.getNickname());
      vo.setAvatar(user.getAvatar());
    }

    // 获取媒体URL列表
    LambdaQueryWrapper<Media> mediaWrapper = new LambdaQueryWrapper<>();
    mediaWrapper.eq(Media::getPostId, post.getPostId())
        .orderByAsc(Media::getSortOrder);
    List<Media> mediaList = mediaMapper.selectList(mediaWrapper);
    vo.setMediaUrls(mediaList.stream()
        .map(Media::getMediaUrl)
        .collect(Collectors.toList()));

    // 获取标签列表
    LambdaQueryWrapper<PostTag> postTagWrapper = new LambdaQueryWrapper<>();
    postTagWrapper.eq(PostTag::getPostId, post.getPostId());
    List<PostTag> postTags = postTagMapper.selectList(postTagWrapper);

    List<String> tagNames = new ArrayList<>();
    for (PostTag postTag : postTags) {
      Tag tag = tagMapper.selectById(postTag.getTagId());
      if (tag != null) {
        tagNames.add(tag.getTagName());
      }
    }
    vo.setTags(tagNames);

    return vo;
  }
}
