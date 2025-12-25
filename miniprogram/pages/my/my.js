// pages/my/my.js
const { get } = require('../../utils/request');
const app = getApp();

Page({
  data: {
    userInfo: null,
    isLoggedIn: false,
    stats: {
      postCount: 0,
      likeCount: 0,
      commentCount: 0
    }
  },

  onShow() {
    this.checkLoginStatus();
  },

  // 检查登录状态
  checkLoginStatus() {
    const isLoggedIn = app.globalData.isLoggedIn;

    // 从本地存储获取最新的用户信息
    const storedUserInfo = wx.getStorageSync('userInfo');
    if (storedUserInfo) {
      app.globalData.userInfo = storedUserInfo;
    }
    const userInfo = app.globalData.userInfo;

    this.setData({ isLoggedIn, userInfo });

    if (isLoggedIn) {
      this.fetchUserStats();
    }
  },

  // 获取用户统计
  async fetchUserStats() {
    try {
      const res = await get('/posts/my?page=1&size=1');
      if (res.data) {
        this.setData({
          'stats.postCount': res.data.total || 0
        });
      }
    } catch (err) {
      console.error('获取统计失败:', err);
    }
  },

  // 跳转登录
  goToLogin() {
    wx.navigateTo({
      url: '/pages/login/login'
    });
  },

  // 跳转我的发布
  goToMyPosts() {
    if (!app.checkLogin()) return;
    wx.navigateTo({
      url: '/pages/my-posts/my-posts'
    });
  },

  // 跳转设置
  goToSettings() {
    if (!app.checkLogin()) return;
    wx.navigateTo({
      url: '/pages/settings/settings'
    });
  },

  // 跳转好友
  goToFriends() {
    if (!app.checkLogin()) return;
    wx.navigateTo({
      url: '/pages/friends/friends'
    });
  },

  // 退出登录
  handleLogout() {
    wx.showModal({
      title: '确认退出',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          app.clearLoginState();
          this.setData({
            isLoggedIn: false,
            userInfo: null
          });
          wx.showToast({ title: '已退出登录', icon: 'success' });
        }
      }
    });
  }
});
