<template>
  <div class="page">
    <div class="toolbar">
      <div>
        <h2>作业任务</h2>
        <p>按课程查看作业内容，并在统一的深色学习空间中完成提交。</p>
      </div>
      <el-input v-model="courseId" placeholder="课程 ID" style="width: 180px" @change="load" />
    </div>

    <el-empty v-if="!list.length" description="暂无作业" />
    <el-card v-for="item in list" :key="item.id" class="panel-card" style="margin-bottom: 14px">
      <div class="section-head" style="margin-bottom: 8px">
        <div>
          <h3>{{ item.title }}</h3>
          <span class="muted">课程 ID：{{ item.courseId }}</span>
        </div>
        <el-tag type="warning" effect="dark">待提交</el-tag>
      </div>
      <p>{{ item.content }}</p>
      <el-input v-model="answers[item.id]" type="textarea" :rows="4" placeholder="请输入你的作业答案" />
      <div style="margin-top: 12px">
        <el-button type="primary" @click="submit(item.id)">提交作业</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { homeworkApi } from '../api/modules'

const courseId = ref(1)
const list = ref([])
const answers = reactive({})

async function load() {
  list.value = await homeworkApi.list(courseId.value)
}

async function submit(id) {
  await homeworkApi.submit({ homeworkId: id, answer: answers[id] || '已完成', attachmentUrl: '' })
  ElMessage.success('作业已提交')
}

onMounted(load)
</script>
