import request from '@/utils/request'
import type {
  ApiResponse,
  LoginRequest,
  LoginResponse,
  User,
  PageParams,
  PageData,
  Post,
  UserStats,
  PostStats,
  DailyActiveData,
} from '@/types'

// 用户登录
export function login(data: LoginRequest): Promise<ApiResponse<LoginResponse>> {
  return request({
    url: '/users/login',
    method: 'post',
    data,
  })
}

// 管理员获取所有用户
export function getUsers(params: PageParams): Promise<ApiResponse<PageData<User>>> {
  return request({
    url: '/admin/users',
    method: 'get',
    params,
  })
}

// 管理员删除用户
export function deleteUser(userId: number): Promise<ApiResponse<null>> {
  return request({
    url: `/admin/users/${userId}`,
    method: 'delete',
  })
}

// 获取内容列表
export function getPosts(params: PageParams & { tag?: string; startDate?: string; endDate?: string }): Promise<ApiResponse<PageData<Post>>> {
  return request({
    url: '/posts',
    method: 'get',
    params,
  })
}

// 管理员删除内容
export function deletePost(postId: number): Promise<ApiResponse<null>> {
  return request({
    url: `/admin/posts/${postId}`,
    method: 'delete',
  })
}

// 获取用户活跃度统计
export function getUserStats(): Promise<ApiResponse<UserStats>> {
  return request({
    url: '/admin/stats/users',
    method: 'get',
  })
}

// 获取内容统计
export function getPostStats(): Promise<ApiResponse<PostStats>> {
  return request({
    url: '/admin/stats/posts',
    method: 'get',
  })
}

// 获取日活跃趋势
export function getDailyActive(days: number = 7): Promise<ApiResponse<DailyActiveData[]>> {
  return request({
    url: '/admin/stats/daily-active',
    method: 'get',
    params: { days },
  })
}
