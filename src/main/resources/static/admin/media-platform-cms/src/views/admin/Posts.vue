<template>
  <div class="post-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>内容列表</span>
        </div>
      </template>

      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="日期范围">
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="YYYY-MM-DD"
              @change="handleDateChange"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="fetchPosts">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="resetSearch">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 内容表格 -->
      <el-table
        v-loading="loading"
        :data="postList"
        style="width: 100%"
        stripe
      >
        <el-table-column prop="postId" label="ID" width="80" />
        <el-table-column label="发布者" width="150">
          <template #default="{ row }">
            <div class="user-info">
              <el-avatar :size="30" :src="row.avatar" />
              <span>{{ row.nickname || row.username }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="内容" min-width="250">
          <template #default="{ row }">
            <div class="content-cell">
              {{ row.content }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="mediaType" label="类型" width="100">
          <template #default="{ row }">
            <el-tag
              :type="
                row.mediaType === 'IMAGE'
                  ? 'success'
                  : row.mediaType === 'VIDEO'
                    ? 'warning'
                    : 'info'
              "
            >
              {{
                row.mediaType === 'IMAGE'
                  ? '图片'
                  : row.mediaType === 'VIDEO'
                    ? '视频'
                    : '文本'
              }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="媒体" width="100">
          <template #default="{ row }">
            <el-popover
              v-if="row.mediaUrls && row.mediaUrls.length > 0"
              placement="right"
              :width="400"
              trigger="hover"
            >
              <template #reference>
                <el-button size="small" type="primary" link>
                  查看({{ row.mediaUrls.length }})
                </el-button>
              </template>
              <div class="media-preview">
                <img
                  v-for="(url, index) in row.mediaUrls"
                  :key="index"
                  :src="url"
                  alt="media"
                  class="preview-image"
                />
              </div>
            </el-popover>
            <span v-else>无</span>
          </template>
        </el-table-column>
        <el-table-column prop="likeCount" label="点赞" width="80" />
        <el-table-column prop="commentCount" label="评论" width="80" />
        <el-table-column prop="viewCount" label="浏览" width="80" />
        <el-table-column label="评分" width="100">
          <template #default="{ row }">
            <div v-if="row.avgRating">
              <el-rate
                :model-value="row.avgRating"
                disabled
                show-score
                text-color="#ff9900"
                score-template="{value}"
              />
            </div>
            <span v-else>暂无评分</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发布时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button type="danger" size="small" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchPosts"
          @current-change="fetchPosts"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { getPosts, deletePost } from '@/api/admin'
import type { Post } from '@/types'
import dayjs from 'dayjs'

const loading = ref(false)
const postList = ref<Post[]>([])
const dateRange = ref<[string, string] | null>(null)

const searchForm = reactive({
  startDate: '',
  endDate: '',
})

const pagination = ref({
  page: 1,
  size: 10,
  total: 0,
})

const fetchPosts = async () => {
  loading.value = true
  try {
    const res = await getPosts({
      page: pagination.value.page,
      size: pagination.value.size,
      startDate: searchForm.startDate,
      endDate: searchForm.endDate,
    })

    if (res.code === 200 && res.data) {
      postList.value = res.data.list
      pagination.value.total = res.data.total
    }
  } catch (error) {
    console.error('Failed to fetch posts:', error)
  } finally {
    loading.value = false
  }
}

const handleDateChange = (value: [string, string] | null) => {
  if (value) {
    searchForm.startDate = value[0]
    searchForm.endDate = value[1]
  } else {
    searchForm.startDate = ''
    searchForm.endDate = ''
  }
}

const resetSearch = () => {
  dateRange.value = null
  searchForm.startDate = ''
  searchForm.endDate = ''
  pagination.value.page = 1
  fetchPosts()
}

const handleDelete = (post: Post) => {
  ElMessageBox.confirm(
    `确定要删除这条内容吗?删除后无法恢复!`,
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      const res = await deletePost(post.postId)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        await fetchPosts()
      }
    } catch (error) {
      console.error('Failed to delete post:', error)
    }
  })
}

const formatDate = (date: string) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm:ss')
}

onMounted(() => {
  fetchPosts()
})
</script>

<style scoped>
.post-management {
  width: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-bar {
  margin-bottom: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.content-cell {
  max-height: 60px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.media-preview {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.preview-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 4px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
