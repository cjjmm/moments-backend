// utils/request.js - 封装 HTTP 请求工具

const app = getApp();

/**
 * 封装 wx.request
 * @param {Object} options - 请求配置
 * @param {string} options.url - 请求路径（不含 baseUrl）
 * @param {string} options.method - 请求方法，默认 GET
 * @param {Object} options.data - 请求数据
 * @param {boolean} options.auth - 是否需要认证，默认 true
 */
function request(options) {
  return new Promise((resolve, reject) => {
    const { url, method = 'GET', data = {}, auth = true } = options;

    // 构建请求头
    const header = {
      'Content-Type': 'application/json'
    };

    // 如果需要认证，添加 Token
    if (auth && app.globalData.token) {
      header['Authorization'] = `Bearer ${app.globalData.token}`;
    }

    wx.request({
      url: `${app.globalData.baseUrl}${url}`,
      method,
      data,
      header,
      success: (res) => {
        console.log(`[${method}] ${url}`, res.data);

        if (res.statusCode === 200) {
          if (res.data.code === 200) {
            resolve(res.data);
          } else if (res.data.code === 401) {
            // Token 失效，清除登录状态
            app.clearLoginState();
            wx.showToast({
              title: '请重新登录',
              icon: 'none'
            });
            wx.navigateTo({
              url: '/pages/login/login'
            });
            reject(res.data);
          } else {
            wx.showToast({
              title: res.data.message || '请求失败',
              icon: 'none'
            });
            reject(res.data);
          }
        } else {
          wx.showToast({
            title: `请求错误: ${res.statusCode}`,
            icon: 'none'
          });
          reject(res);
        }
      },
      fail: (err) => {
        console.error(`[${method}] ${url} 失败:`, err);
        wx.showToast({
          title: '网络请求失败',
          icon: 'none'
        });
        reject(err);
      }
    });
  });
}

/**
 * 上传文件
 * @param {string} filePath - 本地文件路径
 * @param {string} type - 文件类型 IMAGE|VIDEO
 */
function uploadFile(filePath, type = 'IMAGE') {
  return new Promise((resolve, reject) => {
    wx.uploadFile({
      url: `${app.globalData.baseUrl}/upload`,
      filePath: filePath,
      name: 'file',
      formData: {
        type: type
      },
      header: {
        'Authorization': `Bearer ${app.globalData.token}`
      },
      success: (res) => {
        console.log('上传结果:', res);
        try {
          const data = JSON.parse(res.data);
          if (data.code === 200) {
            resolve(data);
          } else {
            wx.showToast({
              title: data.message || '上传失败',
              icon: 'none'
            });
            reject(data);
          }
        } catch (e) {
          reject(e);
        }
      },
      fail: (err) => {
        console.error('上传失败:', err);
        wx.showToast({
          title: '上传失败',
          icon: 'none'
        });
        reject(err);
      }
    });
  });
}

module.exports = {
  request,
  uploadFile,
  // 便捷方法
  get: (url, data = {}, auth = true) => request({ url, method: 'GET', data, auth }),
  post: (url, data = {}, auth = true) => request({ url, method: 'POST', data, auth }),
  put: (url, data = {}, auth = true) => request({ url, method: 'PUT', data, auth }),
  delete: (url, data = {}, auth = true) => request({ url, method: 'DELETE', data, auth })
};
