// pages/index/index.js
// 注意：接口地址需根据部署环境修改
// 生产环境建议使用HTTPS并修改baseUrl
const app = getApp();

Page({
  data: {
    currentCategory: 'image', // 当前选中分类，默认图片
    originalMediaList: [],     // 原始全量数据
    mediaList: []              // 过滤后的当前分类数据
  },
  
  onPullDownRefresh() {
    // 下拉刷新时重新获取最新数据
    this.fetchMediaList();
  },
  
  onLoad() {
    // 页面首次加载时获取数据
    this.fetchMediaList();
  },
  
  // 切换分类
  switchCategory(e) {
    const category = e.currentTarget.dataset.category;
    this.setData({
      currentCategory: category
    });
    // 根据新分类过滤数据
    this.filterMediaList();
  },
  
  // 过滤媒体列表
  filterMediaList() {
    const { currentCategory, originalMediaList } = this.data;
    const filteredList = originalMediaList.filter(item => item.type === currentCategory);
    this.setData({
      mediaList: filteredList
    });
  },
  
  // 获取媒体列表
  fetchMediaList() {
    // 调用接口获取数据
    wx.request({
      url: `${app.globalData.baseUrl}/api/media`,
      method: 'GET',
      success: (res) => {
        if (res.statusCode === 200 && Array.isArray(res.data)) {
          // 保存原始全量数据
          this.setData({
            originalMediaList: res.data
          });
          // 根据当前分类过滤数据
          this.filterMediaList();
        }
      },
      complete: () => {
        // 无论成功失败，都停止下拉刷新动画
        wx.stopPullDownRefresh();
      }
    });
  }
});