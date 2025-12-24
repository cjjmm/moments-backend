// pages/login/login.js
const { post } = require('../../utils/request');
const app = getApp();

Page({
  data: {
    username: '',
    password: '',
    loading: false
  },

  // 输入用户名
  onUsernameInput(e) {
    this.setData({ username: e.detail.value });
  },

  // 输入密码
  onPasswordInput(e) {
    this.setData({ password: e.detail.value });
  },

  // 登录
  async handleLogin() {
    const { username, password } = this.data;

    if (!username.trim()) {
      wx.showToast({ title: '请输入用户名', icon: 'none' });
      return;
    }

    if (!password) {
      wx.showToast({ title: '请输入密码', icon: 'none' });
      return;
    }

    this.setData({ loading: true });

    try {
      const res = await post('/users/login', { username, password }, false);

      // 保存登录状态
      app.setLoginState(res.data, res.data.token);

      wx.showToast({
        title: '登录成功',
        icon: 'success'
      });

      // 延迟跳转，让用户看到成功提示
      setTimeout(() => {
        wx.switchTab({
          url: '/pages/index/index'
        });
      }, 1000);

    } catch (err) {
      console.error('登录失败:', err);
    } finally {
      this.setData({ loading: false });
    }
  },

  // 跳转注册页
  goToRegister() {
    wx.navigateTo({
      url: '/pages/register/register'
    });
  }
});
