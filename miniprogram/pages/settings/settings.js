// pages/settings/settings.js
const { put } = require('../../utils/request');
const app = getApp();

Page({
  data: {
    oldPassword: '',
    newPassword: '',
    confirmPassword: '',
    submitting: false
  },

  onLoad() {
    if (!app.checkLogin()) return;
  },

  onOldPasswordInput(e) {
    this.setData({ oldPassword: e.detail.value });
  },

  onNewPasswordInput(e) {
    this.setData({ newPassword: e.detail.value });
  },

  onConfirmPasswordInput(e) {
    this.setData({ confirmPassword: e.detail.value });
  },

  // 修改密码
  async handleChangePassword() {
    const { oldPassword, newPassword, confirmPassword } = this.data;

    if (!oldPassword) {
      wx.showToast({ title: '请输入当前密码', icon: 'none' });
      return;
    }

    if (!newPassword) {
      wx.showToast({ title: '请输入新密码', icon: 'none' });
      return;
    }

    if (newPassword.length < 6) {
      wx.showToast({ title: '新密码至少6个字符', icon: 'none' });
      return;
    }

    if (newPassword !== confirmPassword) {
      wx.showToast({ title: '两次密码不一致', icon: 'none' });
      return;
    }

    this.setData({ submitting: true });

    try {
      await put('/users/password', {
        oldPassword,
        newPassword
      });

      wx.showToast({ title: '密码修改成功', icon: 'success' });

      // 清空表单
      this.setData({
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      });

      // 建议重新登录
      setTimeout(() => {
        wx.showModal({
          title: '提示',
          content: '密码已修改，建议重新登录',
          showCancel: false,
          success: () => {
            app.clearLoginState();
            wx.navigateTo({
              url: '/pages/login/login'
            });
          }
        });
      }, 1500);

    } catch (err) {
      console.error('修改密码失败:', err);
    } finally {
      this.setData({ submitting: false });
    }
  }
});
