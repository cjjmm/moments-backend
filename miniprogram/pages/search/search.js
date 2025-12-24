// pages/search/search.js
Page({
  data: {
    keyword: '',
    searchResults: [],
    searchHistory: [],
    hotTags: [],
    page: 1,
    size: 10,
    total: 0,
    loading: false,
    hasMore: true,
    sensitiveWarning: false
  },
  
  onLoad() {
    this.loadSearchHistory()
    this.loadHotTags()
  },
  
  onInput(e) {
    this.setData({
      keyword: e.detail.value
    })
  },
  
  doSearch() {
    const keyword = this.data.keyword.trim()
    if (!keyword) return
    
    this.setData({
      page: 1,
      searchResults: [],
      loading: true
    })
    
    this.saveToHistory(keyword)
    
    wx.request({
      url: 'http://localhost:8080/api/v1/posts/search',
      method: 'GET',
      data: {
        keyword: keyword,
        page: 1,
        size: this.data.size
      },
      success: (res) => {
        if (res.data.code === 200) {
          const data = res.data.data
          this.setData({
            searchResults: data.list,
            total: data.total,
            hasMore: data.list.length < data.total
          })
        }
      },
      fail: (error) => {
        console.error('搜索失败:', error)
        wx.showToast({
          title: '搜索失败',
          icon: 'none'
        })
      },
      complete: () => {
        this.setData({ loading: false })
      }
    })
  },
  
  loadSearchHistory() {
    const history = wx.getStorageSync('searchHistory') || []
    this.setData({
      searchHistory: history.slice(0, 10)
    })
  },
  
  loadHotTags() {
    wx.request({
      url: 'http://localhost:8080/api/v1/tags/hot',
      method: 'GET',
      data: { limit: 10 },
      success: (res) => {
        if (res.data.code === 200) {
          this.setData({
            hotTags: res.data.data
          })
        }
      }
    })
  },
  
  saveToHistory(keyword) {
    let history = wx.getStorageSync('searchHistory') || []
    history = history.filter(item => item !== keyword)
    history.unshift(keyword)
    history = history.slice(0, 20)
    wx.setStorageSync('searchHistory', history)
    this.setData({
      searchHistory: history.slice(0, 10)
    })
  },
  
  searchByHistory(e) {
    const keyword = e.currentTarget.dataset.keyword
    this.setData({ keyword }, () => {
      this.doSearch()
    })
  },
  
  searchByTag(e) {
    const tag = e.currentTarget.dataset.tag
    this.setData({ keyword: tag }, () => {
      this.doSearch()
    })
  },
  
  clearHistory() {
    wx.showModal({
      title: '提示',
      content: '确定清空搜索历史吗？',
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync('searchHistory')
          this.setData({ searchHistory: [] })
        }
      }
    })
  },
  
  loadMoreResults() {
    if (!this.data.hasMore || this.data.loading) return
    
    this.setData({
      page: this.data.page + 1,
      loading: true
    })
    
    wx.request({
      url: 'http://localhost:8080/api/v1/posts/search',
      method: 'GET',
      data: {
        keyword: this.data.keyword,
        page: this.data.page,
        size: this.data.size
      },
      success: (res) => {
        if (res.data.code === 200) {
          const data = res.data.data
          const newResults = [...this.data.searchResults, ...data.list]
          this.setData({
            searchResults: newResults,
            total: data.total,
            hasMore: newResults.length < data.total
          })
        }
      },
      complete: () => {
        this.setData({ loading: false })
      }
    })
  }
})