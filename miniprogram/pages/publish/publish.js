// pages/publish/publish.js
const { get, post, put, uploadFile } = require('../../utils/request');
const app = getApp();

Page({
  data: {
    postId: null,    // 编辑模式时的帖子ID
    isEdit: false,   // 是否编辑模式
    content: '',
    mediaType: 'TEXT',  // TEXT, IMAGE, VIDEO
    mediaUrls: [],      // 已上传的媒体URL
    localFiles: [],     // 本地选择的文件
    tags: [],           // 标签列表
    tagInput: '',       // 标签输入
    submitting: false
  },

  onLoad(options) {
    // 检查登录
    if (!app.checkLogin()) return;

    // 编辑模式
    if (options.id) {
      this.setData({
        postId: options.id,
        isEdit: true
      });
      this.loadPostData();
    }
  },

  // 加载帖子数据（编辑模式）
  async loadPostData() {
    try {
      const res = await get(`/posts/${this.data.postId}`, {}, false);
      const post = res.data;

      this.setData({
        content: post.content || '',
        mediaType: post.mediaType || 'TEXT',
        mediaUrls: post.mediaUrls || [],
        tags: post.tags || []
      });
    } catch (err) {
      console.error('加载帖子失败:', err);
    }
  },

  // 切换媒体类型
  switchMediaType(e) {
    const type = e.currentTarget.dataset.type;
    this.setData({
      mediaType: type,
      localFiles: [],
      mediaUrls: this.data.isEdit ? this.data.mediaUrls : []
    });
  },

  // 内容输入
  onContentInput(e) {
    this.setData({ content: e.detail.value });
  },

  // 标签输入
  onTagInput(e) {
    this.setData({ tagInput: e.detail.value });
  },

  // 添加标签
  addTag() {
    const tag = this.data.tagInput.trim();
    if (!tag) return;

    if (this.data.tags.includes(tag)) {
      wx.showToast({ title: '标签已存在', icon: 'none' });
      return;
    }

    if (this.data.tags.length >= 5) {
      wx.showToast({ title: '最多5个标签', icon: 'none' });
      return;
    }

    this.setData({
      tags: [...this.data.tags, tag],
      tagInput: ''
    });
  },

  // 删除标签
  removeTag(e) {
    const index = e.currentTarget.dataset.index;
    const tags = [...this.data.tags];
    tags.splice(index, 1);
    this.setData({ tags });
  },

  // 快捷添加标签
  quickAddTag(e) {
    const tag = e.currentTarget.dataset.tag;
    if (this.data.tags.includes(tag)) {
      wx.showToast({ title: '标签已存在', icon: 'none' });
      return;
    }

    if (this.data.tags.length >= 5) {
      wx.showToast({ title: '最多5个标签', icon: 'none' });
      return;
    }

    this.setData({
      tags: [...this.data.tags, tag]
    });
  },

  // 选择图片
  chooseImage() {
    wx.chooseImage({
      count: 9 - this.data.localFiles.length,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        this.setData({
          localFiles: [...this.data.localFiles, ...res.tempFilePaths]
        });
      }
    });
  },

  // 选择视频
  chooseVideo() {
    wx.chooseVideo({
      sourceType: ['album', 'camera'],
      maxDuration: 60,
      success: (res) => {
        this.setData({
          localFiles: [res.tempFilePath]
        });
      }
    });
  },

  // 删除本地文件
  removeFile(e) {
    const index = e.currentTarget.dataset.index;
    const files = [...this.data.localFiles];
    files.splice(index, 1);
    this.setData({ localFiles: files });
  },

  // 上传文件
  async uploadFiles() {
    const { localFiles, mediaType } = this.data;
    const urls = [...this.data.mediaUrls];

    for (let i = 0; i < localFiles.length; i++) {
      wx.showLoading({ title: `上传中 ${i + 1}/${localFiles.length}` });

      try {
        const res = await uploadFile(localFiles[i], mediaType);
        if (res.data && res.data.url) {
          urls.push(res.data.url);
        }
      } catch (err) {
        console.error('上传失败:', err);
        wx.hideLoading();
        throw err;
      }
    }

    wx.hideLoading();
    return urls;
  },

  // 发布/更新
  async handleSubmit() {
    const { content, mediaType, tags, isEdit, postId, localFiles } = this.data;

    // 验证
    if (!content.trim()) {
      wx.showToast({ title: '请输入内容', icon: 'none' });
      return;
    }

    // 如果是图片或视频类型，检查是否有媒体
    if (mediaType !== 'TEXT' && localFiles.length === 0 && this.data.mediaUrls.length === 0) {
      wx.showToast({ title: `请选择${mediaType === 'IMAGE' ? '图片' : '视频'}`, icon: 'none' });
      return;
    }

    this.setData({ submitting: true });

    try {
      // 上传新文件
      let mediaUrls = [...this.data.mediaUrls];
      if (localFiles.length > 0) {
        mediaUrls = await this.uploadFiles();
      }

      const postData = {
        content: content.trim(),
        mediaType,
        mediaUrls,
        tags
      };

      if (isEdit) {
        // 更新
        await put(`/posts/${postId}`, {
          content: content.trim(),
          tags
        });
        wx.showToast({ title: '更新成功', icon: 'success' });
      } else {
        // 新建
        await post('/posts', postData);
        wx.showToast({ title: '发布成功', icon: 'success' });
      }

      setTimeout(() => {
        wx.switchTab({ url: '/pages/index/index' });
      }, 1000);

    } catch (err) {
      console.error('发布失败:', err);
    } finally {
      this.setData({ submitting: false });
    }
  }
});
