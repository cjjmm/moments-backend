// pages/friends/friends.js
const { get, post, put, delete: del } = require('../../utils/request');
const app = getApp();

Page({
  data: {
    activeTab: 'list',  // list, add, requests
    friendList: [],
    searchKeyword: '',
    searchResults: [],
    hasSearched: false,
    requestList: [],
    pendingCount: 0
  },

  onLoad() {
    if (!app.checkLogin()) return;
  },

  onShow() {
    if (app.globalData.isLoggedIn) {
      this.loadData();
    }
  },

  // 加载数据
  loadData() {
    if (this.data.activeTab === 'list') {
      this.loadFriendList();
    } else if (this.data.activeTab === 'requests') {
      this.loadRequests();
    }
    this.loadPendingCount();
  },

  // 切换Tab
  switchTab(e) {
    const tab = e.currentTarget.dataset.tab;
    this.setData({ activeTab: tab });

    if (tab === 'list') {
      this.loadFriendList();
    } else if (tab === 'requests') {
      this.loadRequests();
    } else if (tab === 'add') {
      this.setData({ searchResults: [], hasSearched: false });
    }
  },

  // 加载好友列表
  async loadFriendList() {
    try {
      const res = await get('/friends');
      if (res.code === 200) {
        this.setData({ friendList: res.data || [] });
      }
    } catch (err) {
      console.error('加载好友列表失败:', err);
    }
  },

  // 加载申请列表
  async loadRequests() {
    try {
      const res = await get('/friends/requests');
      if (res.code === 200) {
        this.setData({ requestList: res.data || [] });
      }
    } catch (err) {
      console.error('加载申请列表失败:', err);
    }
  },

  // 加载待处理数量
  async loadPendingCount() {
    try {
      const res = await get('/friends/requests/count');
      if (res.code === 200) {
        this.setData({ pendingCount: res.data || 0 });
      }
    } catch (err) {
      console.error('加载待处理数量失败:', err);
    }
  },

  // 搜索输入
  onSearchInput(e) {
    this.setData({ searchKeyword: e.detail.value });
  },

  // 搜索用户
  async searchUsers() {
    const keyword = this.data.searchKeyword.trim();
    if (!keyword) {
      wx.showToast({ title: '请输入搜索关键词', icon: 'none' });
      return;
    }

    wx.showLoading({ title: '搜索中...' });
    try {
      const res = await get(`/friends/search?keyword=${encodeURIComponent(keyword)}`);
      if (res.code === 200) {
        this.setData({
          searchResults: res.data || [],
          hasSearched: true
        });
      }
    } catch (err) {
      console.error('搜索失败:', err);
    } finally {
      wx.hideLoading();
    }
  },

  // 发送好友申请
  sendRequest(e) {
    const user = e.currentTarget.dataset.user;

    wx.showModal({
      title: '添加好友',
      content: `确定向 ${user.nickname || user.username} 发送好友申请？`,
      editable: true,
      placeholderText: '请输入验证消息（可选）',
      success: async (res) => {
        if (res.confirm) {
          try {
            await post('/friends/request', {
              toUserId: user.userId,
              message: res.content || ''
            });

            wx.showToast({ title: '申请已发送', icon: 'success' });

            // 更新搜索结果中的状态
            const results = this.data.searchResults.map(item => {
              if (item.userId === user.userId) {
                return { ...item, hasPendingRequest: true };
              }
              return item;
            });
            this.setData({ searchResults: results });
          } catch (err) {
            console.error('发送申请失败:', err);
          }
        }
      }
    });
  },

  // 同意申请
  async acceptRequest(e) {
    const requestId = e.currentTarget.dataset.id;

    try {
      await put(`/friends/requests/${requestId}/accept`);
      wx.showToast({ title: '已添加好友', icon: 'success' });
      this.loadRequests();
      this.loadPendingCount();
    } catch (err) {
      console.error('同意申请失败:', err);
    }
  },

  // 拒绝申请
  async rejectRequest(e) {
    const requestId = e.currentTarget.dataset.id;

    wx.showModal({
      title: '确认',
      content: '确定要拒绝这个好友申请吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await put(`/friends/requests/${requestId}/reject`);
            wx.showToast({ title: '已拒绝', icon: 'success' });
            this.loadRequests();
            this.loadPendingCount();
          } catch (err) {
            console.error('拒绝申请失败:', err);
          }
        }
      }
    });
  },

  // 删除好友
  deleteFriend(e) {
    const friendId = e.currentTarget.dataset.id;

    wx.showModal({
      title: '确认删除',
      content: '确定要删除这个好友吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await del(`/friends/${friendId}`);
            wx.showToast({ title: '已删除', icon: 'success' });
            this.loadFriendList();
          } catch (err) {
            console.error('删除好友失败:', err);
          }
        }
      }
    });
  }
});
