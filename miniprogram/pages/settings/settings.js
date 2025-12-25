// pages/settings/settings.js
const { put, get, uploadFile } = require('../../utils/request');
const app = getApp();

Page({
  data: {
    // 个人资料
    avatar: '',
    email: '',
    savingProfile: false,

    // 密码修改
    oldPassword: '',
    newPassword: '',
    confirmPassword: '',
    submitting: false
  },

  onLoad() {
    if (!app.checkLogin()) return;
    this.loadUserInfo();
  },

  onShow() {
    // 刷新用户信息
    if (app.globalData.isLoggedIn) {
      this.loadUserInfo();
    }
  },

  // 加载用户信息
  async loadUserInfo() {
    try {
      // 从 userInfo 对象中获取 userId
      const userInfo = app.globalData.userInfo;
      if (!userInfo || !userInfo.userId) {
        console.error('用户信息不存在');
        return;
      }

      const res = await get(`/users/${userInfo.userId}`);
      if (res.code === 200) {
        this.setData({
          avatar: res.data.avatar || '',
          email: res.data.email || ''
        });
      }
    } catch (err) {
      console.error('加载用户信息失败:', err);
    }
  },

  // 选择头像
  chooseAvatar() {
    wx.chooseMedia({
      count: 1,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const tempFilePath = res.tempFiles[0].tempFilePath;
        this.uploadAvatar(tempFilePath);
      }
    });
  },

  // 上传头像
  async uploadAvatar(filePath) {
    wx.showLoading({ title: '上传中...' });

    try {
      const res = await uploadFile(filePath, 'IMAGE');
      if (res.code === 200) {
        this.setData({ avatar: res.data.url });
        wx.showToast({ title: '头像已更换', icon: 'success' });
      }
    } catch (err) {
      console.error('上传头像失败:', err);
    } finally {
      wx.hideLoading();
    }
  },

  // 邮箱输入
  onEmailInput(e) {
    this.setData({ email: e.detail.value });
  },

  // 保存个人资料
  async saveProfile() {
    const { avatar, email } = this.data;

    // 简单的邮箱格式验证
    if (email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      wx.showToast({ title: '请输入正确的邮箱格式', icon: 'none' });
      return;
    }

    this.setData({ savingProfile: true });

    try {
      await put('/users/profile', { avatar, email });

      // 更新全局数据中的用户信息
      if (app.globalData.userInfo) {
        app.globalData.userInfo.avatar = avatar;
        app.globalData.userInfo.email = email;

        // 同步更新本地存储
        wx.setStorageSync('userInfo', app.globalData.userInfo);
      }

      wx.showToast({ title: '保存成功', icon: 'success' });
    } catch (err) {
      console.error('保存资料失败:', err);
    } finally {
      this.setData({ savingProfile: false });
    }
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
