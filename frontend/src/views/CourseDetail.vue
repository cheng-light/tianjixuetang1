<template>
  <div class="page" v-if="detail">
    <div class="toolbar">
      <div>
        <h2>{{ detail.course.title }}</h2>
        <p>{{ detail.course.category || '课程学习' }} · {{ detail.chapters.length }} 个章节 · 深色沉浸式学习播放器</p>
      </div>
      <el-button class="ghost-button" @click="$router.back()">返回</el-button>
    </div>

    <div class="learn-layout">
      <section>
        <div class="player-card">
          <video v-if="currentVideo" controls :src="currentVideo"></video>
          <div v-else class="empty-player">暂无视频资源</div>
        </div>

        <el-card class="panel-card" style="margin-top: 16px">
          <template #header>
            <div class="section-head" style="margin-bottom: 0">
              <div>
                <h3>{{ currentChapter?.title || '课程概览' }}</h3>
                <span class="muted">{{ currentChapter ? formatDuration(currentChapter.durationSeconds) : '请选择右侧章节开始学习' }}</span>
              </div>
              <el-button v-if="currentChapter" type="primary" @click="completeChapter">标记完成</el-button>
            </div>
          </template>
          <p>{{ detail.course.description }}</p>
          <el-progress :percentage="progressPercent" :stroke-width="10" />
        </el-card>
      </section>

      <aside>
        <el-card class="panel-card">
          <template #header>
            <div>
              <h3>学习目录</h3>
              <span class="muted">按章节推进学习进度，随时查看完成情况。</span>
            </div>
          </template>
          <div class="chapter-list">
            <button
              v-for="(chapter, index) in detail.chapters"
              :key="chapter.id"
              class="chapter-item"
              :class="{ active: currentChapter?.id === chapter.id }"
              @click="selectChapter(chapter)"
            >
              <span>
                <strong>{{ index + 1 }}. {{ chapter.title }}</strong>
                <small class="muted">{{ formatDuration(chapter.durationSeconds) }}</small>
              </span>
              <el-tag v-if="completedIds.includes(chapter.id)" type="success" effect="dark">已完成</el-tag>
              <el-tag v-else-if="chapter.freePreview" type="warning" effect="dark">试看</el-tag>
            </button>
          </div>
        </el-card>
      </aside>
    </div>
  </div>

  <div v-else class="page">
    <el-skeleton animated :rows="8" />
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { courseApi, learningApi } from '../api/modules'

const route = useRoute()
const detail = ref(null)
const currentChapter = ref(null)
const completedIds = ref([])

const currentVideo = computed(() => currentChapter.value?.videoUrl || '')
const progressPercent = computed(() => {
  const total = detail.value?.chapters?.length || 0
  if (!total) return 0
  return Math.round((completedIds.value.length / total) * 100)
})

async function load() {
  detail.value = await courseApi.detail(route.params.id)
  currentChapter.value = detail.value.chapters?.[0] || null
  const progress = await learningApi.progress(route.params.id)
  completedIds.value = progress.filter((item) => item.completed === 1).map((item) => item.chapterId)
}

function selectChapter(chapter) {
  currentChapter.value = chapter
}

async function completeChapter() {
  if (!currentChapter.value) return
  await learningApi.saveProgress({
    courseId: detail.value.course.id,
    chapterId: currentChapter.value.id,
    learnedSeconds: currentChapter.value.durationSeconds || 0,
    completed: 1
  })
  if (!completedIds.value.includes(currentChapter.value.id)) {
    completedIds.value.push(currentChapter.value.id)
  }
  ElMessage.success('学习进度已更新')
}

function formatDuration(seconds = 0) {
  return `${Math.ceil(seconds / 60) || 1} 分钟`
}

onMounted(load)
</script>

<style scoped>
.chapter-list {
  display: grid;
  gap: 10px;
}

.chapter-item span {
  display: grid;
  gap: 4px;
  text-align: left;
}

.chapter-item strong {
  color: rgba(255, 255, 255, 0.92);
  font-size: 14px;
}

.empty-player {
  height: 360px;
  display: grid;
  place-items: center;
  color: #fff;
}
</style>
