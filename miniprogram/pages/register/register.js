// pages/register/register.js
const { post } = require('../../utils/request');
const app = getApp();

Page({
  data: {
    username: '',
    password: '',
    confirmPassword: '',
    nickname: '',
    loading: false
  },

  onUsernameInput(e) {
    this.setData({ username: e.detail.value });
  },

  onPasswordInput(e) {
    this.setData({ password: e.detail.value });
  },

  onConfirmPasswordInput(e) {
    this.setData({ confirmPassword: e.detail.value });
  },

  onNicknameInput(e) {
    this.setData({ nickname: e.detail.value });
  },

  // 注册
  async handleRegister() {
    const { username, password, confirmPassword, nickname } = this.data;

    if (!username.trim()) {
      wx.showToast({ title: '请输入用户名', icon: 'none' });
      return;
    }

    if (username.length < 3) {
      wx.showToast({ title: '用户名至少3个字符', icon: 'none' });
      return;
    }

    if (!password) {
      wx.showToast({ title: '请输入密码', icon: 'none' });
      return;
    }

    if (password.length < 6) {
      wx.showToast({ title: '密码至少6个字符', icon: 'none' });
      return;
    }

    if (password !== confirmPassword) {
      wx.showToast({ title: '两次密码不一致', icon: 'none' });
      return;
    }

    if (!nickname.trim()) {
      wx.showToast({ title: '请输入昵称', icon: 'none' });
      return;
    }

    this.setData({ loading: true });

    try {
      const res = await post('/users/register', {
        username,
        password,
        nickname
      }, false);

      // 注册成功，自动登录
      app.setLoginState(res.data, res.data.token);

      wx.showToast({
        title: '注册成功',
        icon: 'success'
      });

      setTimeout(() => {
        wx.switchTab({
          url: '/pages/index/index'
        });
      }, 1000);

    } catch (err) {
      console.error('注册失败:', err);
    } finally {
      this.setData({ loading: false });
    }
  },

  // 返回登录
  goToLogin() {
    wx.navigateBack();
  }
});
