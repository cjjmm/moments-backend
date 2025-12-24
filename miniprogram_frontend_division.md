# 小程序前端开发任务分工表（A组 3人）

## 项目概述

小程序前端共包含：
- **9 个页面**：index、publish、my、detail、login、register、search、my-posts、settings
- **2 个组件**：CommentSection（评论区）、LikeButton（点赞按钮）
- **公共文件**：app.js、app.json、app.wxss、utils/request.js

---

## 成员 A1：架构与首页模块

**职责**：搭建小程序基础架构，负责首页和内容详情页

### 具体任务
1. 小程序基础配置（app.js、app.json、app.wxss）
2. 网络请求封装（utils/request.js）
3. 首页帖子列表展示、下拉刷新
4. 帖子详情页展示

### 对应代码文件
| 文件路径 | 说明 |
|----------|------|
| `miniprogram/app.js` | 全局配置、登录状态管理 |
| `miniprogram/app.json` | 页面路由、TabBar配置 |
| `miniprogram/app.wxss` | 全局样式 |
| `miniprogram/utils/request.js` | HTTP请求封装 |
| `miniprogram/pages/index/*` | 首页（4个文件：js/json/wxml/wxss） |
| `miniprogram/pages/detail/*` | 详情页（4个文件） |

---

## 成员 A2：发布与个人中心模块

**职责**：负责内容发布功能和用户个人相关页面

### 具体任务
1. 发布页面（文字/图片/视频上传）
2. 登录/注册页面
3. 个人中心页面
4. 我的帖子列表页
5. 设置页面

### 对应代码文件
| 文件路径 | 说明 |
|----------|------|
| `miniprogram/pages/publish/*` | 发布页（4个文件） |
| `miniprogram/pages/login/*` | 登录页（4个文件） |
| `miniprogram/pages/register/*` | 注册页（4个文件） |
| `miniprogram/pages/my/*` | 个人中心（4个文件） |
| `miniprogram/pages/my-posts/*` | 我的帖子（4个文件） |
| `miniprogram/pages/settings/*` | 设置页（4个文件） |

---

## 成员 A3：互动与搜索模块

**职责**：负责用户互动功能和搜索功能

### 具体任务
1. 点赞功能组件
2. 评论区组件
3. 搜索页面实现
4. 敏感词提示展示

### 对应代码文件
| 文件路径 | 说明 |
|----------|------|
| `miniprogram/components/LikeButton/*` | 点赞按钮组件（4个文件） |
| `miniprogram/components/CommentSection/*` | 评论区组件（4个文件） |
| `miniprogram/pages/search/*` | 搜索页（4个文件） |

---

## 代码量统计

| 成员 | 页面/组件数 | 文件数（约） |
|------|-------------|--------------|
| A1 | 2页面 + 全局配置 | 12 个 |
| A2 | 6页面 | 24 个 |
| A3 | 1页面 + 2组件 | 12 个 |

> **说明**：A2 任务页面数量较多，但登录/注册/设置页面逻辑相对简单；A3 任务量看似较少，但组件开发需要考虑复用性，难度略高。
