<template>
  <div class="page">
    <div class="toolbar">
      <div>
        <h2>个人中心</h2>
        <p>查看账号身份、学习记录和章节完成状态。</p>
      </div>
    </div>

    <div class="profile-layout">
      <el-card class="panel-card profile-card">
        <el-avatar :size="76" class="avatar-chip">{{ auth.user?.nickname?.slice(0, 1) || 'U' }}</el-avatar>
        <h3 style="margin-top: 16px">{{ auth.user?.nickname }}</h3>
        <p>{{ auth.user?.username }}</p>
        <div class="tag-row" style="justify-content: center">
          <el-tag v-for="role in auth.roles" :key="role" effect="dark">{{ role }}</el-tag>
        </div>
      </el-card>

      <el-card class="panel-card">
        <template #header>
          <h3>学习进度</h3>
        </template>
        <el-empty v-if="!progress.length" description="暂无学习记录" />
        <div v-for="item in progress" :key="item.id" class="progress-row">
          <div>
            <strong>课程 {{ item.courseId }} / 章节 {{ item.chapterId }}</strong>
            <p>已学习 {{ Math.ceil((item.learnedSeconds || 0) / 60) }} 分钟</p>
          </div>
          <el-tag :type="item.completed === 1 ? 'success' : 'warning'" effect="dark">
            {{ item.completed === 1 ? '已完成' : '学习中' }}
          </el-tag>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useAuthStore } from '../stores/auth'
import { learningApi } from '../api/modules'

const auth = useAuthStore()
const progress = ref([])

onMounted(async () => {
  progress.value = await learningApi.progress()
})
</script>

<style scoped>
.profile-layout {
  display: grid;
  grid-template-columns: 320px minmax(0, 1fr);
  gap: 18px;
}

.profile-card {
  text-align: center;
}

.progress-row {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  padding: 14px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.12);
}

.progress-row:last-child {
  border-bottom: 0;
}

@media (max-width: 960px) {
  .profile-layout {
    grid-template-columns: 1fr;
  }
}
</style>
