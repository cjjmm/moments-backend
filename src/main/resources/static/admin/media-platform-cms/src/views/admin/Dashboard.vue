<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon user-icon">
              <el-icon><User /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ userStats.totalUsers }}</div>
              <div class="stats-label">总用户数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon active-icon">
              <el-icon><UserFilled /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ userStats.activeUsers }}</div>
              <div class="stats-label">活跃用户</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon post-icon">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ postStats.totalPosts }}</div>
              <div class="stats-label">总内容数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon today-icon">
              <el-icon><Clock /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ postStats.postsToday }}</div>
              <div class="stats-label">今日新增</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-row">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>内容类型分布</span>
            </div>
          </template>
          <div ref="postTypeChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>新增用户趋势</span>
              <el-radio-group v-model="userTrendDays" @change="fetchDailyActive">
                <el-radio-button :value="7">最近7天</el-radio-button>
                <el-radio-button :value="30">最近30天</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="userTrendChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>用户活跃度 & 内容发布趋势</span>
              <el-radio-group v-model="activeTrendDays" @change="fetchDailyActive">
                <el-radio-button :value="7">最近7天</el-radio-button>
                <el-radio-button :value="30">最近30天</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="activeTrendChartRef" class="chart-container-large"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import type { ECharts } from 'echarts'
import { getUserStats, getPostStats, getDailyActive } from '@/api/admin'
import type { UserStats, PostStats, DailyActiveData } from '@/types'

const userStats = ref<UserStats>({
  totalUsers: 0,
  activeUsers: 0,
  newUsersToday: 0,
  newUsersThisWeek: 0,
})

const postStats = ref<PostStats>({
  totalPosts: 0,
  postsToday: 0,
  postsThisWeek: 0,
  postsByType: {
    IMAGE: 0,
    VIDEO: 0,
    TEXT: 0,
  },
})

const dailyActiveData = ref<DailyActiveData[]>([])
const userTrendDays = ref(7)
const activeTrendDays = ref(7)

const postTypeChartRef = ref<HTMLElement>()
const userTrendChartRef = ref<HTMLElement>()
const activeTrendChartRef = ref<HTMLElement>()

let postTypeChart: ECharts | null = null
let userTrendChart: ECharts | null = null
let activeTrendChart: ECharts | null = null

// 获取统计数据
const fetchStats = async () => {
  try {
    const [userRes, postRes] = await Promise.all([getUserStats(), getPostStats()])

    if (userRes.code === 200 && userRes.data) {
      userStats.value = userRes.data
    }

    if (postRes.code === 200 && postRes.data) {
      postStats.value = postRes.data
      renderPostTypeChart()
    }
  } catch (error) {
    console.error('Failed to fetch stats:', error)
  }
}

// 获取日活跃数据
const fetchDailyActive = async () => {
  try {
    const res = await getDailyActive(activeTrendDays.value)
    if (res.code === 200 && res.data) {
      dailyActiveData.value = res.data
      renderUserTrendChart()
      renderActiveTrendChart()
    }
  } catch (error) {
    console.error('Failed to fetch daily active:', error)
  }
}

// 渲染内容类型分布饼图
const renderPostTypeChart = () => {
  if (!postTypeChartRef.value) return

  if (!postTypeChart) {
    postTypeChart = echarts.init(postTypeChartRef.value)
  }

  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)',
    },
    legend: {
      orient: 'vertical',
      left: 'left',
    },
    series: [
      {
        name: '内容类型',
        type: 'pie',
        radius: '50%',
        data: [
          { value: postStats.value.postsByType.IMAGE, name: '图片' },
          { value: postStats.value.postsByType.VIDEO, name: '视频' },
          { value: postStats.value.postsByType.TEXT, name: '文本' },
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)',
          },
        },
      },
    ],
  }

  postTypeChart.setOption(option)
}

// 渲染用户趋势折线图
const renderUserTrendChart = () => {
  if (!userTrendChartRef.value || dailyActiveData.value.length === 0) return

  if (!userTrendChart) {
    userTrendChart = echarts.init(userTrendChartRef.value)
  }

  const dates = dailyActiveData.value.map((item) => item.date)
  const activeUsers = dailyActiveData.value.map((item) => item.activeUsers)

  const option = {
    tooltip: {
      trigger: 'axis',
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: {
        rotate: 45,
      },
    },
    yAxis: {
      type: 'value',
    },
    series: [
      {
        name: '活跃用户数',
        type: 'line',
        data: activeUsers,
        smooth: true,
        itemStyle: {
          color: '#5470c6',
        },
        areaStyle: {
          color: 'rgba(84, 112, 198, 0.3)',
        },
      },
    ],
  }

  userTrendChart.setOption(option)
}

// 渲染活跃度趋势双轴折线图
const renderActiveTrendChart = () => {
  if (!activeTrendChartRef.value || dailyActiveData.value.length === 0) return

  if (!activeTrendChart) {
    activeTrendChart = echarts.init(activeTrendChartRef.value)
  }

  const dates = dailyActiveData.value.map((item) => item.date)
  const activeUsers = dailyActiveData.value.map((item) => item.activeUsers)
  const newPosts = dailyActiveData.value.map((item) => item.newPosts)

  const option = {
    tooltip: {
      trigger: 'axis',
    },
    legend: {
      data: ['活跃用户', '新增内容'],
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: {
        rotate: 45,
      },
    },
    yAxis: [
      {
        type: 'value',
        name: '活跃用户数',
        position: 'left',
      },
      {
        type: 'value',
        name: '新增内容数',
        position: 'right',
      },
    ],
    series: [
      {
        name: '活跃用户',
        type: 'line',
        data: activeUsers,
        smooth: true,
        itemStyle: {
          color: '#5470c6',
        },
      },
      {
        name: '新增内容',
        type: 'line',
        yAxisIndex: 1,
        data: newPosts,
        smooth: true,
        itemStyle: {
          color: '#91cc75',
        },
      },
    ],
  }

  activeTrendChart.setOption(option)
}

// 窗口大小改变时重新渲染图表
const handleResize = () => {
  postTypeChart?.resize()
  userTrendChart?.resize()
  activeTrendChart?.resize()
}

onMounted(async () => {
  await fetchStats()
  await fetchDailyActive()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  postTypeChart?.dispose()
  userTrendChart?.dispose()
  activeTrendChart?.dispose()
})
</script>

<style scoped>
.dashboard {
  width: 100%;
}

.stats-row {
  margin-bottom: 20px;
}

.stats-card {
  border-radius: 8px;
}

.stats-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.stats-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 30px;
  color: white;
}

.user-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.active-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.post-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.today-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stats-info {
  flex: 1;
}

.stats-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stats-label {
  font-size: 14px;
  color: #909399;
}

.charts-row {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  width: 100%;
  height: 300px;
}

.chart-container-large {
  width: 100%;
  height: 400px;
}
</style>
