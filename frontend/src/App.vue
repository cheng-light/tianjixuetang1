<template>
  <el-container class="app-shell" :class="{ 'guest-shell': !auth.isLoggedIn }">
    <el-aside v-if="auth.isLoggedIn" width="250px" class="sidebar">
      <div class="brand">
        <span class="brand-mark">T</span>
        <div>
          <strong>天机学堂</strong>
          <small>沉浸式学习云空间</small>
        </div>
      </div>

      <el-menu :default-active="$route.path" router class="side-menu">
        <el-menu-item index="/dashboard">首页</el-menu-item>
        <el-menu-item index="/courses">课程中心</el-menu-item>
        <el-menu-item index="/homeworks">作业任务</el-menu-item>
        <el-menu-item index="/exams">在线考试</el-menu-item>
        <el-menu-item index="/profile">个人中心</el-menu-item>
        <el-menu-item v-if="auth.roles.includes('ADMIN')" index="/admin">管理后台</el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header v-if="auth.isLoggedIn" class="topbar">
        <div>
          <span class="topbar-title">{{ pageTitle }}</span>
          <small>与登录页统一的深色玻璃风格学习工作台。</small>
        </div>
        <div class="topbar-actions">
          <el-tag effect="dark" class="role-chip">{{ auth.roles.join(' / ') }}</el-tag>
          <el-avatar :size="38" class="avatar-chip">{{ auth.user?.nickname?.slice(0, 1) || 'U' }}</el-avatar>
          <el-button class="ghost-button" @click="logout">退出登录</el-button>
        </div>
      </el-header>

      <el-main class="main-area">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useAuthStore } from './stores/auth'
import { useRoute } from 'vue-router'

const auth = useAuthStore()
const route = useRoute()

const titles = {
  '/dashboard': '学习首页',
  '/courses': '课程中心',
  '/homeworks': '作业任务',
  '/exams': '考试空间',
  '/profile': '个人中心',
  '/admin': '管理后台'
}

const pageTitle = computed(() => titles[route.path] || '天机学堂')

function logout() {
  auth.logout()
  window.location.href = '/auth.html'
}
</script>
