import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login',
    },
    {
      path: '/index.html',
      redirect: '/login',
    },
    {
      path: '/login',
      name: 'AdminLogin',
      component: () => import('@/views/admin/Login.vue'),
      meta: { requiresAuth: false },
    },
    {
      path: '/dashboard',
      component: () => import('@/views/admin/Layout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'Dashboard',
          component: () => import('@/views/admin/Dashboard.vue'),
        },
        {
          path: 'users',
          name: 'Users',
          component: () => import('@/views/admin/Users.vue'),
        },
        {
          path: 'posts',
          name: 'Posts',
          component: () => import('@/views/admin/Posts.vue'),
        },
      ],
    },
  ],
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  if (to.meta.requiresAuth) {
    // 需要认证的路由
    if (authStore.isAuthenticated()) {
      // 检查是否是管理员
      if (authStore.isAdmin()) {
        next()
      } else {
        // 不是管理员,跳转到登录页
        next('/login')
      }
    } else {
      // 未登录,跳转到登录页
      next('/login')
    }
  } else {
    // 不需要认证的路由
    if (to.path === '/login' && authStore.isAuthenticated() && authStore.isAdmin()) {
      // 已登录的管理员访问登录页,跳转到管理后台
      next('/dashboard')
    } else {
      next()
    }
  }
})

export default router
