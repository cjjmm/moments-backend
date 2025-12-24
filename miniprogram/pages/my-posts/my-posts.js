// pages/my-posts/my-posts.js
const { get, delete: del } = require('../../utils/request');
const app = getApp();

Page({
  data: {
    postList: [],
    page: 1,
    size: 10,
    hasMore: true,
    loading: false
  },

  onLoad() {
    if (!app.checkLogin()) return;
    this.fetchMyPosts();
  },

  onPullDownRefresh() {
    this.setData({
      page: 1,
      postList: [],
      hasMore: true
    });
    this.fetchMyPosts();
  },

  onReachBottom() {
    if (this.data.hasMore && !this.data.loading) {
      this.fetchMyPosts();
    }
  },

  // 获取我的帖子
  async fetchMyPosts() {
    this.setData({ loading: true });

    try {
      const { page, size } = this.data;
      const res = await get(`/posts/my?page=${page}&size=${size}`);

      if (res.data) {
        const list = res.data.list || [];
        const newList = page === 1 ? list : [...this.data.postList, ...list];

        this.setData({
          postList: newList,
          page: page + 1,
          hasMore: list.length >= size
        });
      }
    } catch (err) {
      console.error('获取帖子失败:', err);
    } finally {
      this.setData({ loading: false });
      wx.stopPullDownRefresh();
    }
  },

  // 编辑帖子
  editPost(e) {
    const postId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/publish/publish?id=${postId}`
    });
  },

  // 删除帖子
  deletePost(e) {
    const postId = e.currentTarget.dataset.id;

    wx.showModal({
      title: '确认删除',
      content: '删除后无法恢复，确定要删除吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await del(`/posts/${postId}`);
            wx.showToast({ title: '删除成功', icon: 'success' });

            // 从列表中移除
            const postList = this.data.postList.filter(p => p.postId !== postId);
            this.setData({ postList });

          } catch (err) {
            console.error('删除失败:', err);
          }
        }
      }
    });
  },

  // 跳转详情
  goToDetail(e) {
    const postId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/detail/detail?id=${postId}`
    });
  }
});
