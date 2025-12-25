# 多媒体展示软件 API 接口文档

**版本**: v1.0  
**基础路径**: `http://your-server-ip:8080/api/v1`

---

## 1. 用户管理模块

### 1.1 用户注册
- **接口**: `POST /users/register`
- **描述**: 新用户注册
- **请求体**:
```json
{
  "username": "string",
  "password": "string",
  "nickname": "string",
  "avatar": "string (可选)"
}
```
- **响应**:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "userId": 1,
    "username": "user123",
    "nickname": "昵称",
    "token": "jwt-token-string"
  }
}
```

### 1.2 用户登录
- **接口**: `POST /users/login`
- **描述**: 用户登录
- **请求体**:
```json
{
  "username": "string",
  "password": "string"
}
```
- **响应**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "userId": 1,
    "username": "user123",
    "nickname": "昵称",
    "avatar": "头像URL",
    "token": "jwt-token-string"
  }
}
```

### 1.3 修改密码
- **接口**: `PUT /users/password`
- **描述**: 用户修改密码
- **请求头**: `Authorization: Bearer {token}`
- **请求体**:
```json
{
  "oldPassword": "string",
  "newPassword": "string"
}
```
- **响应**:
```json
{
  "code": 200,
  "message": "密码修改成功",
  "data": null
}
```

### 1.4 获取用户信息
- **接口**: `GET /users/{userId}`
- **描述**: 获取用户详细信息
- **响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "userId": 1,
    "username": "user123",
    "nickname": "昵称",
    "avatar": "头像URL",
    "email": "邮箱地址",
    "createTime": "2025-12-15T10:00:00"
  }
}
```

### 1.5 更新用户资料
- **接口**: `PUT /users/profile`
- **描述**: 用户更新个人资料（头像、邮箱）
- **请求头**: `Authorization: Bearer {token}`
- **请求体**:
```json
{
  "avatar": "头像URL (可选)",
  "email": "邮箱地址 (可选)"
}
```
- **响应**:
```json
{
  "code": 200,
  "message": "资料更新成功",
  "data": null
}
```

### 1.6 获取所有用户（管理员）
- **接口**: `GET /admin/users`
- **描述**: 管理员获取所有用户列表
- **请求头**: `Authorization: Bearer {admin-token}`
- **查询参数**: 
  - `page`: 页码，默认1
  - `size`: 每页数量，默认10
- **响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "list": [
      {
        "userId": 1,
        "username": "user123",
        "nickname": "昵称",
        "createTime": "2025-12-15T10:00:00",
        "postCount": 5
      }
    ]
  }
}
```

### 1.7 删除用户（管理员）
- **接口**: `DELETE /admin/users/{userId}`
- **描述**: 管理员删除用户
- **请求头**: `Authorization: Bearer {admin-token}`
- **响应**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

---

## 2. 内容（帖子）管理模块

### 2.1 发布内容
- **接口**: `POST /posts`
- **描述**: 用户发布新内容
- **请求头**: `Authorization: Bearer {token}`
- **请求体**:
```json
{
  "content": "文本内容",
  "mediaUrls": ["图片或视频URL数组"],
  "mediaType": "IMAGE|VIDEO|TEXT",
  "tags": ["标签1", "标签2"]
}
```
- **响应**:
```json
{
  "code": 200,
  "message": "发布成功",
  "data": {
    "postId": 1,
    "content": "文本内容",
    "createTime": "2025-12-15T10:00:00"
  }
}
```

### 2.2 获取内容列表（首页）
- **接口**: `GET /posts`
- **描述**: 获取所有内容列表（下拉刷新）
- **查询参数**: 
  - `page`: 页码，默认1
  - `size`: 每页数量，默认10
  - `tag`: 标签筛选（可选）
  - `startDate`: 开始日期（可选）
  - `endDate`: 结束日期（可选）
- **响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 50,
    "list": [
      {
        "postId": 1,
        "userId": 1,
        "username": "user123",
        "nickname": "昵称",
        "avatar": "头像URL",
        "content": "文本内容",
        "mediaUrls": ["URL1", "URL2"],
        "mediaType": "IMAGE",
        "tags": ["标签1"],
        "likeCount": 10,
        "commentCount": 5,
        "avgRating": 4.5,
        "createTime": "2025-12-15T10:00:00"
      }
    ]
  }
}
```

### 2.3 获取用户自己的内容
- **接口**: `GET /posts/my`
- **描述**: 获取当前用户发布的所有内容
- **请求头**: `Authorization: Bearer {token}`
- **查询参数**: 
  - `page`: 页码
  - `size`: 每页数量
- **响应**: 同2.2

### 2.4 获取内容详情
- **接口**: `GET /posts/{postId}`
- **描述**: 获取单个内容的详细信息
- **响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "postId": 1,
    "userId": 1,
    "username": "user123",
    "nickname": "昵称",
    "avatar": "头像URL",
    "content": "文本内容",
    "mediaUrls": ["URL1"],
    "mediaType": "IMAGE",
    "tags": ["标签1"],
    "likeCount": 10,
    "commentCount": 5,
    "avgRating": 4.5,
    "createTime": "2025-12-15T10:00:00",
    "updateTime": "2025-12-15T11:00:00"
  }
}
```

### 2.5 修改内容
- **接口**: `PUT /posts/{postId}`
- **描述**: 用户修改自己的内容
- **请求头**: `Authorization: Bearer {token}`
- **请求体**:
```json
{
  "content": "修改后的文本",
  "tags": ["新标签"]
}
```
- **响应**:
```json
{
  "code": 200,
  "message": "修改成功",
  "data": null
}
```

### 2.6 删除内容
- **接口**: `DELETE /posts/{postId}`
- **描述**: 用户删除自己的内容
- **请求头**: `Authorization: Bearer {token}`
- **响应**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

### 2.7 管理员删除内容
- **接口**: `DELETE /admin/posts/{postId}`
- **描述**: 管理员删除任意内容
- **请求头**: `Authorization: Bearer {admin-token}`
- **响应**: 同2.6

### 2.8 搜索内容
- **接口**: `GET /posts/search`
- **描述**: 根据关键词搜索内容
- **查询参数**: 
  - `keyword`: 搜索关键词
  - `page`: 页码
  - `size`: 每页数量
- **响应**: 同2.2

---

## 3. 评论模块

### 3.1 发表评论
- **接口**: `POST /comments`
- **描述**: 对内容发表评论
- **请求头**: `Authorization: Bearer {token}`
- **请求体**:
```json
{
  "postId": 1,
  "content": "评论内容"
}
```
- **响应**:
```json
{
  "code": 200,
  "message": "评论成功",
  "data": {
    "commentId": 1,
    "postId": 1,
    "userId": 1,
    "content": "评论内容",
    "createTime": "2025-12-15T10:00:00"
  }
}
```

### 3.2 获取内容的评论列表
- **接口**: `GET /comments/{postId}`
- **描述**: 获取某个内容的所有评论
- **查询参数**: 
  - `page`: 页码
  - `size`: 每页数量
- **响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 20,
    "list": [
      {
        "commentId": 1,
        "userId": 2,
        "username": "user456",
        "nickname": "评论者昵称",
        "avatar": "头像URL",
        "content": "评论内容",
        "createTime": "2025-12-15T10:00:00"
      }
    ]
  }
}
```

### 3.3 删除评论
- **接口**: `DELETE /comments/{commentId}`
- **描述**: 删除自己的评论
- **请求头**: `Authorization: Bearer {token}`
- **响应**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

---

## 4. 评分模块

### 4.1 对内容评分
- **接口**: `POST /ratings`
- **描述**: 用户对内容打分
- **请求头**: `Authorization: Bearer {token}`
- **请求体**:
```json
{
  "postId": 1,
  "score": 5
}
```
- **响应**:
```json
{
  "code": 200,
  "message": "评分成功",
  "data": {
    "ratingId": 1,
    "postId": 1,
    "score": 5
  }
}
```

### 4.2 获取用户对某内容的评分
- **接口**: `GET /ratings/{postId}`
- **描述**: 获取当前用户对某内容的评分
- **请求头**: `Authorization: Bearer {token}`
- **响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "ratingId": 1,
    "postId": 1,
    "score": 5
  }
}
```

---

## 5. 文件上传模块

### 5.1 上传图片/视频
- **接口**: `POST /upload`
- **描述**: 上传图片或视频文件
- **请求头**: 
  - `Authorization: Bearer {token}`
  - `Content-Type: multipart/form-data`
- **请求参数**: 
  - `file`: 文件（form-data）
  - `type`: 文件类型（IMAGE|VIDEO）
- **响应**:
```json
{
  "code": 200,
  "message": "上传成功",
  "data": {
    "url": "http://your-server/uploads/xxx.jpg",
    "filename": "xxx.jpg"
  }
}
```

---

## 6. 标签模块

### 6.1 获取热门标签
- **接口**: `GET /tags/hot`
- **描述**: 获取使用最多的标签
- **查询参数**: 
  - `limit`: 返回数量，默认10
- **响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "tagName": "旅游",
      "useCount": 100
    },
    {
      "tagName": "美食",
      "useCount": 80
    }
  ]
}
```

---

## 7. 统计分析模块（管理员）

### 7.1 用户活跃度统计
- **接口**: `GET /admin/stats/users`
- **描述**: 获取用户活跃度数据
- **请求头**: `Authorization: Bearer {admin-token}`
- **响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalUsers": 100,
    "activeUsers": 60,
    "newUsersToday": 5,
    "newUsersThisWeek": 20
  }
}
```

### 7.2 内容统计
- **接口**: `GET /admin/stats/posts`
- **描述**: 获取内容统计数据
- **请求头**: `Authorization: Bearer {admin-token}`
- **响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalPosts": 500,
    "postsToday": 30,
    "postsThisWeek": 150,
    "postsByType": {
      "IMAGE": 300,
      "VIDEO": 100,
      "TEXT": 100
    }
  }
}
```

### 7.3 日活跃趋势
- **接口**: `GET /admin/stats/daily-active`
- **描述**: 获取最近7天或30天的日活跃数据
- **请求头**: `Authorization: Bearer {admin-token}`
- **查询参数**: 
  - `days`: 天数（7或30），默认7
- **响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "date": "2025-12-09",
      "activeUsers": 45,
      "newPosts": 20
    },
    {
      "date": "2025-12-10",
      "activeUsers": 50,
      "newPosts": 25
    }
  ]
}
```

---

## 8. 加分项功能

### 8.1 敏感词检测
- **接口**: `POST /content/check`
- **描述**: 检测文本内容是否包含敏感词
- **请求体**:
```json
{
  "content": "待检测的文本内容"
}
```
- **响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "hasSensitiveWord": true,
    "sensitiveWords": ["敏感词1", "敏感词2"],
    "filteredContent": "已过滤的内容"
  }
}
```

---

## 通用响应格式

所有接口均返回统一格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 状态码说明
- `200`: 成功
- `400`: 请求参数错误
- `401`: 未授权（未登录或token失效）
- `403`: 无权限
- `404`: 资源不存在
- `500`: 服务器内部错误

### 错误响应示例
```json
{
  "code": 401,
  "message": "用户未登录",
  "data": null
}
```
