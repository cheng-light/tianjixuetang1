<template>
  <div class="page">
    <div class="toolbar">
      <div>
        <h2>课程中心</h2>
        <p>浏览课程、按关键词搜索，并进入沉浸式学习页面完成视频学习。</p>
      </div>
      <div class="tag-row">
        <el-input v-model="keyword" clearable placeholder="搜索课程" style="width: 240px" />
        <el-button v-if="canManage" type="primary" @click="dialog = true">新建课程</el-button>
      </div>
    </div>

    <el-skeleton :loading="loading" animated :count="3">
      <div class="grid">
        <el-card v-for="course in filteredCourses" :key="course.id" class="course-card" body-style="padding: 16px">
          <div class="course-cover">
            <el-image :src="course.coverUrl || fallbackCover" fit="cover" />
          </div>
          <div class="course-body">
            <div class="tag-row" style="margin-top: 16px">
              <el-tag effect="dark">{{ course.category || '通用课程' }}</el-tag>
              <el-tag type="warning" effect="dark">已发布</el-tag>
            </div>
            <h3>{{ course.title }}</h3>
            <p>{{ course.description }}</p>
            <div class="course-footer">
              <span class="price">￥{{ course.price }}</span>
              <el-button type="primary" @click="$router.push(`/courses/${course.id}`)">进入课程</el-button>
            </div>
          </div>
        </el-card>
      </div>
    </el-skeleton>

    <el-dialog v-model="dialog" title="创建课程" width="620px">
      <el-form :model="form" label-position="top">
        <el-form-item label="课程标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="课程分类"><el-input v-model="form.category" /></el-form-item>
        <el-form-item label="课程简介"><el-input v-model="form.description" type="textarea" :rows="4" /></el-form-item>
        <el-form-item label="课程价格"><el-input-number v-model="form.price" :min="0" style="width: 180px" /></el-form-item>
        <el-form-item label="课程封面">
          <el-upload :auto-upload="false" :on-change="uploadCover" :show-file-list="false">
            <el-button>上传封面</el-button>
          </el-upload>
          <el-text v-if="form.coverUrl" type="success">{{ form.coverUrl }}</el-text>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button class="ghost-button" @click="dialog = false">取消</el-button>
        <el-button type="primary" @click="create">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { courseApi, fileApi } from '../api/modules'
import { useAuthStore } from '../stores/auth'

const auth = useAuthStore()
const courses = ref([])
const keyword = ref('')
const loading = ref(true)
const dialog = ref(false)
const fallbackCover = 'https://images.unsplash.com/photo-1522202176988-66273c2fd55f?auto=format&fit=crop&w=1200&q=80'
const form = reactive({ title: '', category: '', description: '', coverUrl: '', price: 0, status: 'PUBLISHED' })

const canManage = computed(() => auth.roles.includes('ADMIN') || auth.roles.includes('TEACHER'))
const filteredCourses = computed(() => {
  const key = keyword.value.trim().toLowerCase()
  if (!key) return courses.value
  return courses.value.filter((course) => `${course.title} ${course.description} ${course.category}`.toLowerCase().includes(key))
})

async function load() {
  loading.value = true
  try {
    courses.value = await courseApi.list()
  } finally {
    loading.value = false
  }
}

async function uploadCover(file) {
  const data = await fileApi.upload(file.raw)
  form.coverUrl = data.url
}

async function create() {
  await courseApi.create(form)
  dialog.value = false
  Object.assign(form, { title: '', category: '', description: '', coverUrl: '', price: 0, status: 'PUBLISHED' })
  await load()
}

onMounted(load)
</script>

<style scoped>
.course-body {
  min-height: 210px;
  display: flex;
  flex-direction: column;
}

.course-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-top: auto;
}
</style>
