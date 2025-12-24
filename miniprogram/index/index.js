// index/index.js
// 对接 Spring Boot 后端的首页逻辑
const app = getApp();

Page({
  data: {
    currentCategory: 'all',   // 当前选中分类: all, IMAGE, VIDEO, TEXT
    postList: [],             // 帖子列表
    loading: false,           // 加载状态
    page: 1,                  // 当前页码
    size: 10,                 // 每页数量
    hasMore: true,            // 是否有更多数据
    categories: [
      { key: 'all', name: '全部' },
      { key: 'IMAGE', name: '图片' },
      { key: 'VIDEO', name: '视频' },
      { key: 'TEXT', name: '文字' }
    ]
  },

  onLoad() {
    this.fetchPosts();
  },

  onPullDownRefresh() {
    // 下拉刷新
    this.setData({
      page: 1,
      postList: [],
      hasMore: true
    });
    this.fetchPosts();
  },

  onReachBottom() {
    // 触底加载更多
    if (this.data.hasMore && !this.data.loading) {
      this.fetchPosts();
    }
  },

  // 切换分类
  switchCategory(e) {
    const category = e.currentTarget.dataset.category;
    this.setData({
      currentCategory: category,
      page: 1,
      postList: [],
      hasMore: true
    });
    this.fetchPosts();
  },

  // 获取帖子列表
  fetchPosts() {
    if (this.data.loading) return;

    this.setData({ loading: true });

    // 构建请求参数
    let url = `${app.globalData.baseUrl}/posts?page=${this.data.page}&size=${this.data.size}`;

    // 如果选择了特定分类，添加筛选参数（根据你的后端API调整）
    // 注意：后端的 /posts 接口可能需要添加 mediaType 筛选参数

    wx.request({
      url: url,
      method: 'GET',
      header: {
        'Content-Type': 'application/json'
      },
      success: (res) => {
        console.log('API响应:', res.data);

        if (res.statusCode === 200 && res.data.code === 200) {
          const data = res.data.data;
          let list = data.list || [];

          // 如果选择了特定分类，前端过滤
          if (this.data.currentCategory !== 'all') {
            list = list.filter(item => item.mediaType === this.data.currentCategory);
          }

          // 合并数据
          const newList = [...this.data.postList, ...list];

          this.setData({
            postList: newList,
            page: this.data.page + 1,
            hasMore: newList.length < data.total
          });
        } else {
          wx.showToast({
            title: res.data.message || '获取数据失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        console.error('请求失败:', err);
        wx.showToast({
          title: '网络请求失败',
          icon: 'none'
        });
      },
      complete: () => {
        this.setData({ loading: false });
        wx.stopPullDownRefresh();
      }
    });
  },

  // 预览图片
  previewImage(e) {
    const urls = e.currentTarget.dataset.urls;
    const current = e.currentTarget.dataset.current;
    wx.previewImage({
      current: current,
      urls: urls
    });
  }
});