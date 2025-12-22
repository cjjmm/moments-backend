// app.js
App({
  onLaunch() {
    // 小程序启动时执行的初始化逻辑
    console.log('小程序启动')
  },
  onShow() {
    // 小程序显示时执行
  },
  onHide() {
    // 小程序隐藏时执行
  },
  globalData: {
    // 全局共享数据
    userInfo: null,
    baseUrl: 'http://localhost:3001' // 本地后端服务地址（端口已改为3001）
  }
})