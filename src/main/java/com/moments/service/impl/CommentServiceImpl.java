package com.moments.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moments.dto.CommentCreateRequest;
import com.moments.dto.CommentVO;
import com.moments.entity.Comment;
import com.moments.entity.Post;
import com.moments.entity.User;
import com.moments.exception.BusinessException;
import com.moments.mapper.CommentMapper;
import com.moments.mapper.PostMapper;
import com.moments.mapper.UserMapper;
import com.moments.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 评论服务实现类
 */
@Service
public class CommentServiceImpl implements CommentService {

  @Autowired
  private CommentMapper commentMapper;

  @Autowired
  private PostMapper postMapper;

  @Autowired
  private UserMapper userMapper;

  @Override
  @Transactional
  public Map<String, Object> createComment(Long userId, CommentCreateRequest request) {
    // 检查帖子是否存在
    Post post = postMapper.selectById(request.getPostId());
    if (post == null || post.getStatus() == 0) {
      throw new BusinessException(404, "帖子不存在");
    }

    // 创建评论
    Comment comment = new Comment();
    comment.setPostId(request.getPostId());
    comment.setUserId(userId);
    comment.setContent(request.getContent());
    comment.setStatus(1);

    commentMapper.insert(comment);

    // 更新帖子评论数
    post.setCommentCount(post.getCommentCount() + 1);
    postMapper.updateById(post);

    // 返回结果
    Map<String, Object> result = new HashMap<>();
    result.put("commentId", comment.getCommentId());
    result.put("postId", comment.getPostId());
    result.put("userId", comment.getUserId());
    result.put("content", comment.getContent());
    result.put("createTime", comment.getCreateTime());

    return result;
  }

  @Override
  public IPage<CommentVO> getCommentsByPostId(Long postId, Integer page, Integer size) {
    // 检查帖子是否存在
    Post post = postMapper.selectById(postId);
    if (post == null || post.getStatus() == 0) {
      throw new BusinessException(404, "帖子不存在");
    }

    Page<Comment> pageParam = new Page<>(page, size);

    LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(Comment::getPostId, postId)
        .eq(Comment::getStatus, 1)
        .orderByDesc(Comment::getCreateTime);

    IPage<Comment> commentPage = commentMapper.selectPage(pageParam, wrapper);

    // 转换为VO
    Page<CommentVO> voPage = new Page<>(commentPage.getCurrent(), commentPage.getSize(), commentPage.getTotal());

    List<CommentVO> voList = commentPage.getRecords().stream()
        .map(this::convertToCommentVO)
        .collect(Collectors.toList());

    voPage.setRecords(voList);
    return voPage;
  }

  @Override
  @Transactional
  public void deleteComment(Long userId, Long commentId) {
    Comment comment = commentMapper.selectById(commentId);
    if (comment == null || comment.getStatus() == 0) {
      throw new BusinessException(404, "评论不存在");
    }

    // 验证是否是评论作者
    if (!comment.getUserId().equals(userId)) {
      throw new BusinessException(403, "无权删除此评论");
    }

    // 逻辑删除
    comment.setStatus(0);
    commentMapper.updateById(comment);

    // 更新帖子评论数
    Post post = postMapper.selectById(comment.getPostId());
    if (post != null && post.getCommentCount() > 0) {
      post.setCommentCount(post.getCommentCount() - 1);
      postMapper.updateById(post);
    }
  }

  /**
   * 转换评论为VO
   */
  private CommentVO convertToCommentVO(Comment comment) {
    CommentVO vo = new CommentVO();
    vo.setCommentId(comment.getCommentId());
    vo.setUserId(comment.getUserId());
    vo.setContent(comment.getContent());
    vo.setCreateTime(comment.getCreateTime());

    // 获取用户信息
    User user = userMapper.selectById(comment.getUserId());
    if (user != null) {
      vo.setUsername(user.getUsername());
      vo.setNickname(user.getNickname());
      vo.setAvatar(user.getAvatar());
    }

    return vo;
  }
}
