<template>
  <div class="page">
    <div class="toolbar">
      <div>
        <h2>管理后台</h2>
        <p>查看平台级数据概览，并在统一玻璃风格界面中管理用户状态。</p>
      </div>
    </div>

    <section class="metric-grid">
      <div class="metric-card">
        <span class="muted">用户数量</span>
        <strong class="metric-value">{{ stats.users || 0 }}</strong>
      </div>
      <div class="metric-card">
        <span class="muted">课程数量</span>
        <strong class="metric-value">{{ stats.courses || 0 }}</strong>
      </div>
      <div class="metric-card">
        <span class="muted">系统状态</span>
        <strong class="metric-value status-value">运行中</strong>
      </div>
    </section>

    <el-card class="panel-card">
      <template #header>
        <div class="section-head" style="margin-bottom: 0">
          <div>
            <h3>用户目录</h3>
            <span class="muted">查看用户名、昵称、邮箱和账号状态。</span>
          </div>
        </div>
      </template>
      <el-table :data="users" stripe>
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" effect="dark">
              {{ row.status === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { adminApi } from '../api/modules'

const stats = reactive({})
const users = ref([])

onMounted(async () => {
  Object.assign(stats, await adminApi.dashboard())
  users.value = await adminApi.users()
})
</script>

<style scoped>
.status-value {
  color: #fff;
  font-size: 22px;
}
</style>
