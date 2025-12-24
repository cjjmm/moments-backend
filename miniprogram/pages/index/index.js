// pages/index/index.js
const { get } = require('../../utils/request');
const app = getApp();

Page({
  data: {
    posts: [],           // 帖子列表
    page: 1,             // 当前页码
    size: 10,            // 每页数量
    total: 0,            // 总数
    hasMore: true,       // 是否有更多
    loading: false,      // 加载状态
    currentTag: '',      // 当前选中的标签
    hotTags: []          // 热门标签
  },

  onLoad() {
    this.fetchPosts();
    this.fetchHotTags();
  },

  onShow() {
    // 每次显示页面时刷新数据
    this.refreshPosts();
  },

  onPullDownRefresh() {
    this.refreshPosts();
  },

  onReachBottom() {
    if (this.data.hasMore && !this.data.loading) {
      this.loadMorePosts();
    }
  },

  // 刷新帖子列表
  refreshPosts() {
    this.setData({
      page: 1,
      posts: [],
      hasMore: true
    }, () => {
      this.fetchPosts();
    });
  },

  // 获取帖子列表
  async fetchPosts() {
    if (this.data.loading) return;

    this.setData({ loading: true });

    try {
      const params = {
        page: this.data.page,
        size: this.data.size
      };

      // 如果有选中标签，添加标签筛选
      if (this.data.currentTag) {
        params.tag = this.data.currentTag;
      }

      const res = await get('/posts', params, false);

      if (res.data) {
        const newPosts = this.data.page === 1
          ? res.data.list
          : [...this.data.posts, ...res.data.list];

        this.setData({
          posts: newPosts,
          total: res.data.total,
          hasMore: newPosts.length < res.data.total
        });
      }
    } catch (err) {
      console.error('获取帖子列表失败:', err);
    } finally {
      this.setData({ loading: false });
      wx.stopPullDownRefresh();
    }
  },

  // 加载更多
  loadMorePosts() {
    this.setData({
      page: this.data.page + 1
    }, () => {
      this.fetchPosts();
    });
  },

  // 获取热门标签
  async fetchHotTags() {
    try {
      const res = await get('/tags/hot', { limit: 8 }, false);
      if (res.data) {
        this.setData({ hotTags: res.data });
      }
    } catch (err) {
      console.error('获取热门标签失败:', err);
    }
  },

  // 选择标签筛选
  selectTag(e) {
    const tag = e.currentTarget.dataset.tag;
    this.setData({
      currentTag: this.data.currentTag === tag ? '' : tag
    }, () => {
      this.refreshPosts();
    });
  },

  // 跳转到详情页
  goToDetail(e) {
    const postId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/detail/detail?id=${postId}`
    });
  },

  // 跳转到搜索页
  goToSearch() {
    wx.navigateTo({
      url: '/pages/search/search'
    });
  },

  // 预览图片
  previewImage(e) {
    const urls = e.currentTarget.dataset.urls;
    const current = e.currentTarget.dataset.current;
    wx.previewImage({ current, urls });
  },

  // 跳转到发布页
  goToPublish() {
    wx.switchTab({
      url: '/pages/publish/publish'
    });
  }
});