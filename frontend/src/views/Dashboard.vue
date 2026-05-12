<template>
  <div class="page">
    <section class="hero-panel">
      <el-tag effect="dark" color="#ff8a1f">企业学习空间</el-tag>
      <h1>在同一套沉浸式界面里完成课程学习、进度跟踪、作业提交与考试认证。</h1>
      <p>天机学堂为企业和机构提供统一的学习入口，让课程、学习记录、教学管理和考试流程在一个清晰的工作台中完成。</p>
      <div class="hero-actions">
        <el-button type="primary" @click="$router.push('/courses')">开始学习</el-button>
        <el-button class="ghost-button" @click="$router.push('/profile')">查看进度</el-button>
      </div>
    </section>

    <section class="metric-grid">
      <div class="metric-card">
        <span class="muted">可学习课程</span>
        <strong class="metric-value">{{ courses.length }}</strong>
      </div>
      <div class="metric-card">
        <span class="muted">学习记录</span>
        <strong class="metric-value">{{ progress.length }}</strong>
      </div>
      <div class="metric-card">
        <span class="muted">当前角色</span>
        <strong class="metric-value role-value">{{ auth.roles.join(' / ') || '-' }}</strong>
      </div>
    </section>

    <section>
      <div class="section-head">
        <div>
          <h2>推荐课程</h2>
          <p>按企业学习场景整理核心课程，支持后端技术、业务学习与考试训练。</p>
        </div>
        <el-button class="ghost-button" @click="$router.push('/courses')">全部课程</el-button>
      </div>

      <div class="grid">
        <el-card v-for="course in courses.slice(0, 3)" :key="course.id" class="course-card" body-style="padding: 16px">
          <div class="course-cover">
            <el-image :src="course.coverUrl || fallbackCover" fit="cover" />
          </div>
          <h3 style="margin-top: 16px">{{ course.title }}</h3>
          <p>{{ course.description }}</p>
          <div class="tag-row">
            <el-tag effect="dark">{{ course.category || '通用课程' }}</el-tag>
            <span class="price">￥{{ course.price }}</span>
          </div>
        </el-card>
      </div>
    </section>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { courseApi, learningApi } from '../api/modules'
import { useAuthStore } from '../stores/auth'

const auth = useAuthStore()
const courses = ref([])
const progress = ref([])
const fallbackCover = 'https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=1200&q=80'

onMounted(async () => {
  courses.value = await courseApi.list()
  progress.value = await learningApi.progress()
})
</script>

<style scoped>
.role-value {
  font-size: 22px;
}
</style>
