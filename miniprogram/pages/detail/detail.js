// pages/detail/detail.js
const { get, post, delete: del } = require('../../utils/request');
const app = getApp();

Page({
  data: {
    postId: null,
    post: null,
    comments: [],
    myRating: 0,
    commentInput: '',
    loading: true,
    submitting: false,
    isOwner: false
  },

  onLoad(options) {
    if (options.id) {
      this.setData({ postId: options.id });
      this.fetchPostDetail();
      this.fetchComments();
      this.fetchMyRating();
    }
  },

  // 获取帖子详情
  async fetchPostDetail() {
    try {
      const res = await get(`/posts/${this.data.postId}`, {}, false);
      const post = res.data;

      // 判断是否是自己的帖子
      const isOwner = app.globalData.userInfo &&
        app.globalData.userInfo.userId === post.userId;

      this.setData({
        post,
        isOwner,
        loading: false
      });
    } catch (err) {
      console.error('获取详情失败:', err);
      wx.showToast({ title: '加载失败', icon: 'none' });
    }
  },

  // 获取评论列表
  async fetchComments() {
    try {
      const res = await get(`/comments/${this.data.postId}?page=1&size=50`, {}, false);
      if (res.data) {
        this.setData({ comments: res.data.list || [] });
      }
    } catch (err) {
      console.error('获取评论失败:', err);
    }
  },

  // 获取我的评分
  async fetchMyRating() {
    if (!app.globalData.isLoggedIn) return;

    try {
      const res = await get(`/ratings/${this.data.postId}`);
      if (res.data) {
        this.setData({ myRating: res.data.score || 0 });
      }
    } catch (err) {
      // 可能未评分
    }
  },

  // 评论输入
  onCommentInput(e) {
    this.setData({ commentInput: e.detail.value });
  },

  // 发表评论
  async submitComment() {
    if (!app.checkLogin()) return;

    const content = this.data.commentInput.trim();
    if (!content) {
      wx.showToast({ title: '请输入评论内容', icon: 'none' });
      return;
    }

    this.setData({ submitting: true });

    try {
      await post('/comments', {
        postId: parseInt(this.data.postId),
        content
      });

      wx.showToast({ title: '评论成功', icon: 'success' });
      this.setData({ commentInput: '' });
      this.fetchComments();
      this.fetchPostDetail(); // 刷新评论数

    } catch (err) {
      console.error('评论失败:', err);
    } finally {
      this.setData({ submitting: false });
    }
  },

  // 评分
  async onRating(e) {
    if (!app.checkLogin()) return;

    const score = e.currentTarget.dataset.score;

    try {
      await post('/ratings', {
        postId: parseInt(this.data.postId),
        score
      });

      wx.showToast({ title: '评分成功', icon: 'success' });
      this.setData({ myRating: score });
      this.fetchPostDetail(); // 刷新平均分

    } catch (err) {
      console.error('评分失败:', err);
    }
  },

  // 删除帖子
  deletePost() {
    wx.showModal({
      title: '确认删除',
      content: '删除后无法恢复，确定要删除吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await del(`/posts/${this.data.postId}`);
            wx.showToast({ title: '删除成功', icon: 'success' });
            setTimeout(() => {
              wx.navigateBack();
            }, 1000);
          } catch (err) {
            console.error('删除失败:', err);
          }
        }
      }
    });
  },

  // 编辑帖子
  editPost() {
    wx.navigateTo({
      url: `/pages/publish/publish?id=${this.data.postId}`
    });
  },

  // 预览图片
  previewImage(e) {
    const urls = e.currentTarget.dataset.urls;
    const current = e.currentTarget.dataset.current;
    wx.previewImage({ current, urls });
  }
});
