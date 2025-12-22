// API响应通用格式
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

// 分页请求参数
export interface PageParams {
  page?: number
  size?: number
}

// 分页响应数据
export interface PageData<T> {
  total: number
  list: T[]
}

// 用户信息
export interface User {
  userId: number
  username: string
  nickname: string
  avatar?: string
  role?: string
  status?: number
  createTime: string
  updateTime?: string
  postCount?: number
}

// 登录请求
export interface LoginRequest {
  username: string
  password: string
}

// 登录响应
export interface LoginResponse {
  userId: number
  username: string
  nickname: string
  avatar?: string
  token: string
  role?: string
}

// 内容(帖子)信息
export interface Post {
  postId: number
  userId: number
  username?: string
  nickname?: string
  avatar?: string
  content: string
  mediaUrls?: string[]
  mediaType: 'TEXT' | 'IMAGE' | 'VIDEO'
  tags?: string[]
  likeCount: number
  commentCount: number
  viewCount?: number
  avgRating?: number
  ratingCount?: number
  status: number
  createTime: string
  updateTime?: string
}

// 用户活跃度统计
export interface UserStats {
  totalUsers: number
  activeUsers: number
  newUsersToday: number
  newUsersThisWeek: number
}

// 内容统计
export interface PostStats {
  totalPosts: number
  postsToday: number
  postsThisWeek: number
  postsByType: {
    IMAGE: number
    VIDEO: number
    TEXT: number
  }
}

// 日活跃数据
export interface DailyActiveData {
  date: string
  activeUsers: number
  newPosts: number
}
