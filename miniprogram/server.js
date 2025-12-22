const express = require('express');
const app = express();
const PORT = 3001;

// 模拟媒体数据（符合接口文档格式）
const mockMediaList = [
  {
    id: '1',
    type: 'image',
    url: 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=600&fit=crop'
  },
  {
    id: '2',
    type: 'video',
    url: 'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4'
  },
  {
    id: '3',
    type: 'image',
    url: 'https://images.unsplash.com/photo-1541647376583-8934aaf3448a?w=800&h=600&fit=crop'
  },
  {
    id: '4',
    type: 'image',
    url: 'https://images.unsplash.com/photo-1530281700549-e82e7bf110d6?w=800&h=600&fit=crop'
  },
  {
    id: '5',
    type: 'video',
    url: 'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4'
  }
];

// CORS中间件
app.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
  res.header('Access-Control-Allow-Headers', 'Content-Type, Authorization');
  next();
});

// 获取媒体列表API
app.get('/api/media', (req, res) => {
  setTimeout(() => {
    res.json(mockMediaList);
  }, 500); // 添加延迟，模拟网络请求
});

// 启动服务器
app.listen(PORT, () => {
  console.log(`Server is running on http://localhost:${PORT}`);
});
