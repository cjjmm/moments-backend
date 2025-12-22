import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { LoginRequest, LoginResponse } from '@/types'
import { login as loginApi } from '@/api/admin'
import { ElMessage } from 'element-plus'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string>('')
  const userInfo = ref<LoginResponse | null>(null)

  // 从localStorage加载数据
  const loadFromStorage = () => {
    const storedToken = localStorage.getItem('token')
    const storedUserInfo = localStorage.getItem('userInfo')

    if (storedToken) {
      token.value = storedToken
    }

    if (storedUserInfo) {
      try {
        userInfo.value = JSON.parse(storedUserInfo)
      } catch (e) {
        console.error('Failed to parse userInfo from localStorage', e)
      }
    }
  }

  // 登录
  const login = async (loginData: LoginRequest) => {
    try {
      const res = await loginApi(loginData)

      if (res.code === 200 && res.data) {
        token.value = res.data.token
        userInfo.value = res.data

        // 保存到localStorage
        localStorage.setItem('token', res.data.token)
        localStorage.setItem('userInfo', JSON.stringify(res.data))

        ElMessage.success('登录成功')
        return true
      } else {
        ElMessage.error(res.message || '登录失败')
        return false
      }
    } catch (error) {
      console.error('Login error:', error)
      return false
    }
  }

  // 登出
  const logout = () => {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    ElMessage.success('已退出登录')
  }

  // 检查是否已登录
  const isAuthenticated = () => {
    return !!token.value
  }

  // 检查是否是管理员
  const isAdmin = () => {
    return userInfo.value?.role === 'ADMIN'
  }

  // 初始化时加载数据
  loadFromStorage()

  return {
    token,
    userInfo,
    login,
    logout,
    isAuthenticated,
    isAdmin,
  }
})
