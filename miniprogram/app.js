// app.js - 全局配置和状态管理
App({
  onLaunch() {
    // 小程序启动时执行
    console.log('小程序启动');

    // 尝试从本地存储恢复登录状态
    const token = wx.getStorageSync('token');
    const userInfo = wx.getStorageSync('userInfo');

    if (token && userInfo) {
      this.globalData.token = token;
      this.globalData.userInfo = userInfo;
      this.globalData.isLoggedIn = true;
    }
  },

  globalData: {
    // 后端 API 基础路径
    baseUrl: 'http://localhost:8080/api/v1',

    // 用户信息
    userInfo: null,
    token: null,
    isLoggedIn: false
  },

  // 设置登录状态
  setLoginState(userInfo, token) {
    this.globalData.userInfo = userInfo;
    this.globalData.token = token;
    this.globalData.isLoggedIn = true;

    // 持久化存储
    wx.setStorageSync('token', token);
    wx.setStorageSync('userInfo', userInfo);
  },

  // 清除登录状态
  clearLoginState() {
    this.globalData.userInfo = null;
    this.globalData.token = null;
    this.globalData.isLoggedIn = false;

    // 清除本地存储
    wx.removeStorageSync('token');
    wx.removeStorageSync('userInfo');
  },

  // 检查是否登录
  checkLogin() {
    if (!this.globalData.isLoggedIn) {
      wx.navigateTo({
        url: '/pages/login/login'
      });
      return false;
    }
    return true;
  }
});