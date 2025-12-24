// components/LikeButton/LikeButton.js
const app = getApp();

Component({
  properties: {
    postId: {
      type: Number,
      value: 0
    },
    initialLiked: {
      type: Boolean,
      value: false
    },
    initialCount: {
      type: Number,
      value: 0
    }
  },

  data: {
    isLiked: false,
    likeCount: 0
  },

  lifetimes: {
    attached() {
      this.setData({
        isLiked: this.properties.initialLiked,
        likeCount: this.properties.initialCount
      });

      // 如果已登录，获取实际点赞状态
      this.fetchLikeStatus();
    }
  },

  methods: {
    // 获取点赞状态
    fetchLikeStatus() {
      const token = wx.getStorageSync('token');
      if (!token) return;

      wx.request({
        url: `${app.globalData.baseUrl}/likes/${this.properties.postId}`,
        method: 'GET',
        header: {
          'Authorization': `Bearer ${token}`
        },
        success: (res) => {
          if (res.data.code === 200 && res.data.data) {
            this.setData({
              isLiked: res.data.data.liked
            });
          }
        }
      });
    },

    toggleLike() {
      const token = wx.getStorageSync('token');
      const postId = this.properties.postId;
      const isLiked = this.data.isLiked;

      if (!token) {
        wx.showToast({
          title: '请先登录',
          icon: 'none'
        });
        return;
      }

      if (isLiked) {
        // 取消点赞
        wx.request({
          url: `${app.globalData.baseUrl}/likes/${postId}`,
          method: 'DELETE',
          header: {
            'Authorization': `Bearer ${token}`
          },
          success: (res) => {
            if (res.data.code === 200) {
              this.setData({
                isLiked: false,
                likeCount: Math.max(0, this.data.likeCount - 1)
              });
              this.triggerEvent('likechanged', { liked: false });
            }
          },
          fail: (error) => {
            console.error('取消点赞失败:', error);
            wx.showToast({
              title: '操作失败',
              icon: 'none'
            });
          }
        });
      } else {
        // 点赞
        wx.request({
          url: `${app.globalData.baseUrl}/likes`,
          method: 'POST',
          header: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
          data: {
            postId: postId
          },
          success: (res) => {
            if (res.data.code === 200) {
              this.setData({
                isLiked: true,
                likeCount: this.data.likeCount + 1
              });
              this.triggerEvent('likechanged', { liked: true });
            }
          },
          fail: (error) => {
            console.error('点赞失败:', error);
            wx.showToast({
              title: '操作失败',
              icon: 'none'
            });
          }
        });
      }
    }
  }
});