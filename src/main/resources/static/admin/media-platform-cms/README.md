# 快速开始指南

## 项目概述

这个管理后台系统实现了完整的管理功能,包括用户管理、内容管理和统计分析(加分项)。

## 已实现的功能

### ✅ 核心功能
1. **用户管理**
   - 分页查看所有用户
   - 删除用户(管理员除外)
   - 显示用户详细信息(头像、昵称、角色、状态、发帖数等)
   - 刷新用户列表

2. **内容管理**
   - 分页查看所有内容
   - 删除内容
   - 按日期范围筛选
   - 查看媒体文件预览(图片/视频)
   - 显示内容详情(点赞、评论、浏览、评分等)

3. **用户认证**
   - 管理员登录
   - JWT Token 认证
   - 路由守卫(未登录自动跳转)
   - 权限验证(仅管理员可访问)

### ⭐ 加分项 - 统计分析
1. **数据卡片**
   - 总用户数
   - 活跃用户数
   - 总内容数
   - 今日新增内容

2. **可视化图表**
   - 内容类型分布(饼图)
   - 用户活跃度趋势(折线图)
   - 用户活跃度 & 内容发布双轴趋势图
   - 支持查看最近7天或30天数据

## 技术特点

- 🎨 使用 Element Plus UI 组件库,界面美观
- 📊 集成 ECharts 图表库,数据可视化
- 🔐 完善的权限控制和路由守卫
- 📱 响应式设计,适配不同屏幕
- 💪 TypeScript 类型安全
- 🚀 Vite 构建,开发体验极佳

## 启动步骤

### 1. 安装依赖(已完成)
```bash
npm install
```

### 2. 配置后端地址
编辑 `src/utils/request.ts`,修改 baseURL 为你的后端地址:
```typescript
baseURL: 'http://localhost:8080/api/v1'
```

### 3. 启动开发服务器
```bash
npm run dev
```

### 4. 访问系统
在浏览器中打开: http://localhost:5173

## 路由结构

```
/                          → 重定向到 /admin/login
/admin/login              → 登录页面
/admin                    → 管理后台布局
  ├─ /                    → 统计分析(Dashboard)
  ├─ /users               → 用户管理
  └─ /posts               → 内容管理
```

## API 接口对应

| 功能 | 接口 | 页面 |
|------|------|------|
| 登录 | POST /users/login | Login.vue |
| 获取用户列表 | GET /admin/users | Users.vue |
| 删除用户 | DELETE /admin/users/{userId} | Users.vue |
| 获取内容列表 | GET /posts | Posts.vue |
| 删除内容 | DELETE /admin/posts/{postId} | Posts.vue |
| 用户活跃度统计 | GET /admin/stats/users | Dashboard.vue |
| 内容统计 | GET /admin/stats/posts | Dashboard.vue |
| 日活跃趋势 | GET /admin/stats/daily-active | Dashboard.vue |

## 项目文件说明

### 核心文件
- `src/main.ts` - 应用入口,配置 Element Plus 和图标
- `src/App.vue` - 根组件,包含路由出口
- `src/router/index.ts` - 路由配置和路由守卫

### 工具文件
- `src/utils/request.ts` - Axios 封装,包含请求/响应拦截器
- `src/types/index.ts` - TypeScript 类型定义
- `src/api/admin.ts` - 管理员 API 接口

### 状态管理
- `src/stores/auth.ts` - 用户认证状态(登录、登出、权限检查)

### 页面组件
- `src/views/admin/Login.vue` - 登录页
- `src/views/admin/Layout.vue` - 管理后台布局(侧边栏+顶栏)
- `src/views/admin/Dashboard.vue` - 统计分析页
- `src/views/admin/Users.vue` - 用户管理页
- `src/views/admin/Posts.vue` - 内容管理页

## 功能演示

### 登录流程
1. 访问任意管理页面会自动跳转到登录页
2. 输入管理员账号和密码
3. 登录成功后跳转到统计分析页
4. 非管理员账号无法登录

### 用户管理
1. 点击左侧"用户管理"菜单
2. 查看用户列表,支持分页
3. 点击"删除"按钮删除用户
4. 管理员账号的删除按钮被禁用

### 内容管理
1. 点击左侧"内容管理"菜单
2. 查看内容列表,支持分页
3. 使用日期范围筛选内容
4. 鼠标悬停在"查看"按钮上预览媒体文件
5. 点击"删除"按钮删除内容

### 统计分析
1. 点击左侧"统计分析"菜单
2. 查看实时统计数据卡片
3. 查看内容类型分布饼图
4. 切换查看最近7天或30天的趋势数据
5. 所有图表支持鼠标悬停查看详细数据

## 注意事项

1. **首次运行**: 确保后端服务已启动
2. **Token 管理**: Token 存储在 localStorage,页面刷新不会丢失
3. **权限控制**: 只有角色为 ADMIN 的用户可以访问管理后台
4. **数据刷新**: 删除操作后会自动刷新列表

## 常见问题

### Q: 登录后跳转到登录页?
A: 检查用户角色是否为 ADMIN

### Q: 请求接口 401 错误?
A: Token 过期或无效,系统会自动跳转到登录页

### Q: 图表不显示?
A: 检查后端接口是否返回了正确的数据格式

### Q: 无法删除用户?
A: 管理员账号不能被删除,按钮已被禁用

## 下一步

1. 连接后端 API
2. 使用管理员账号登录测试
3. 测试各项功能是否正常

## 技术支持

如有问题,请查看:
- `ADMIN_README.md` - 完整项目文档
- API 接口文档 - 查看接口详细说明
