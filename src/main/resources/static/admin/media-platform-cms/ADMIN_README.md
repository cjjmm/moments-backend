# 多媒体展示平台 - 管理后台

这是一个基于 Vue 3 + TypeScript + Element Plus 开发的管理后台系统,用于管理多媒体展示平台的用户和内容。

## 技术栈

- **前端框架**: Vue 3 (Composition API)
- **开发语言**: TypeScript
- **UI 组件库**: Element Plus
- **状态管理**: Pinia
- **路由管理**: Vue Router
- **HTTP 客户端**: Axios
- **图表库**: ECharts
- **日期处理**: Day.js
- **构建工具**: Vite

## 项目结构

```
src/
├── api/              # API 接口
│   └── admin.ts      # 管理员接口
├── components/       # 公共组件
│   └── admin/        # 管理后台组件
├── router/           # 路由配置
│   └── index.ts
├── stores/           # Pinia 状态管理
│   ├── auth.ts       # 用户认证
│   └── counter.ts
├── types/            # TypeScript 类型定义
│   └── index.ts
├── utils/            # 工具函数
│   └── request.ts    # Axios 封装
├── views/            # 页面组件
│   └── admin/        # 管理后台页面
│       ├── Dashboard.vue  # 统计分析
│       ├── Layout.vue     # 布局
│       ├── Login.vue      # 登录
│       ├── Posts.vue      # 内容管理
│       └── Users.vue      # 用户管理
├── App.vue
└── main.ts
```

## 功能模块

### 1. 用户认证
- 管理员登录
- Token 认证
- 路由守卫
- 自动跳转

### 2. 用户管理
- 查看用户列表(分页)
- 删除用户
- 查看用户详细信息
- 用户状态展示

### 3. 内容管理
- 查看内容列表(分页)
- 删除内容
- 按日期筛选
- 查看媒体文件预览
- 内容类型标签

### 4. 统计分析(加分项)
- 用户活跃度统计
  - 总用户数
  - 活跃用户数
  - 今日新增
- 内容统计
  - 总内容数
  - 今日新增
  - 本周新增
  - 内容类型分布(饼图)
- 趋势分析
  - 用户活跃度趋势(折线图)
  - 内容发布趋势(折线图)
  - 支持查看最近7天/30天

## API 接口

### 基础路径
```
http://localhost:8080/api/v1
```

### 管理员接口

#### 登录
- **POST** `/users/login`

#### 用户管理
- **GET** `/admin/users` - 获取用户列表
- **DELETE** `/admin/users/{userId}` - 删除用户

#### 内容管理
- **GET** `/posts` - 获取内容列表
- **DELETE** `/admin/posts/{postId}` - 删除内容

#### 统计分析
- **GET** `/admin/stats/users` - 用户活跃度统计
- **GET** `/admin/stats/posts` - 内容统计
- **GET** `/admin/stats/daily-active` - 日活跃趋势

## 开发指南

### 安装依赖
```bash
npm install
```

### 启动开发服务器
```bash
npm run dev
```

### 构建生产版本
```bash
npm run build
```

### 类型检查
```bash
npm run type-check
```

### 代码格式化
```bash
npm run format
```

### 代码检查
```bash
npm run lint
```

## 配置说明

### API 基础路径配置
在 `src/utils/request.ts` 中修改 `baseURL`:
```typescript
const request: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api/v1', // 修改为你的后端地址
  timeout: 30000,
})
```

### 路由配置
所有管理后台路由都在 `/admin` 路径下:
- `/admin/login` - 登录页
- `/admin` - 统计分析(默认首页)
- `/admin/users` - 用户管理
- `/admin/posts` - 内容管理

## 使用说明

### 1. 启动项目
```bash
npm run dev
```

### 2. 访问后台
打开浏览器访问: `http://localhost:5173`

### 3. 管理员登录
使用管理员账号登录系统

### 4. 功能使用
- **统计分析**: 查看平台整体数据和趋势
- **用户管理**: 查看和管理用户,支持删除操作
- **内容管理**: 查看和管理用户发布的内容,支持按日期筛选和删除

## 注意事项

1. 确保后端服务已启动并在 `http://localhost:8080` 运行
2. 登录时需要使用管理员账号(role 为 ADMIN)
3. 所有请求都会携带 Token 进行认证
4. Token 过期后会自动跳转到登录页
5. 管理员账号不能被删除

## 浏览器支持

- Chrome (最新版本)
- Firefox (最新版本)
- Safari (最新版本)
- Edge (最新版本)

## 开发环境要求

- Node.js: ^20.19.0 或 >=22.12.0
- npm: 最新版本

## 许可证

MIT
