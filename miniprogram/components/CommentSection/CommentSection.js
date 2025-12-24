// components/CommentSection/CommentSection.js
Component({
  properties: {
    postId: {
      type: Number,
      value: 0
    }
  },
  
  data: {
    comments: [],
    page: 1,
    size: 10,
    total: 0,
    hasMore: true,
    newComment: '',
    isSubmitting: false
  },
  
  lifetimes: {
    attached() {
      this.loadComments()
    }
  },
  
  methods: {
    loadComments() {
      const token = wx.getStorageSync('token')
      const { postId, page, size } = this.data
      
      wx.request({
        url: `http://localhost:8080/api/v1/comments/${postId}`,
        method: 'GET',
        data: {
          page: page,
          size: size
        },
        header: {
          'Authorization': token ? `Bearer ${token}` : ''
        },
        success: (res) => {
          if (res.data.code === 200) {
            const data = res.data.data
            const newComments = [...this.data.comments, ...data.list]
            this.setData({
              comments: newComments,
              total: data.total,
              hasMore: newComments.length < data.total
            })
          }
        },
        fail: (error) => {
          console.error('加载评论失败:', error)
        }
      })
    },
    
    onInput(e) {
      this.setData({
        newComment: e.detail.value
      })
    },
    
    submitComment() {
      const newComment = this.data.newComment.trim()
      if (!newComment) return
      
      const token = wx.getStorageSync('token')
      if (!token) {
        wx.showModal({
          title: '提示',
          content: '请先登录',
          showCancel: false
        })
        return
      }
      
      this.setData({ isSubmitting: true })
      
      wx.request({
        url: 'http://localhost:8080/api/v1/comments',
        method: 'POST',
        header: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        data: {
          postId: this.properties.postId,
          content: newComment
        },
        success: (res) => {
          if (res.data.code === 200) {
            const userInfo = wx.getStorageSync('userInfo') || {}
            const newCommentData = {
              ...res.data.data,
              avatar: userInfo.avatar || '/static/default-avatar.png',
              nickname: userInfo.nickname || '用户'
            }
            
            const updatedComments = [newCommentData, ...this.data.comments]
            this.setData({
              comments: updatedComments,
              newComment: '',
              total: this.data.total + 1
            })
            
            this.triggerEvent('commentadded')
            
            wx.showToast({
              title: '评论成功',
              icon: 'success'
            })
          }
        },
        fail: (error) => {
          console.error('评论失败:', error)
          wx.showToast({
            title: '评论失败',
            icon: 'none'
          })
        },
        complete: () => {
          this.setData({ isSubmitting: false })
        }
      })
    },
    
    loadMoreComments() {
      if (!this.data.hasMore) return
      
      this.setData({
        page: this.data.page + 1
      }, () => {
        this.loadComments()
      })
    },
    
    formatTime(time) {
      const date = new Date(time)
      const now = new Date()
      const diff = now - date
      
      if (diff < 60000) return '刚刚'
      if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
      if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
      if (diff < 604800000) return `${Math.floor(diff / 86400000)}天前`
      
      return `${date.getMonth() + 1}-${date.getDate()}`
    }
  }
})