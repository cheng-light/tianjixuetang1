<template>
  <div class="page">
    <div class="toolbar">
      <div>
        <h2>在线考试</h2>
        <p>开始课程考试、填写答案并在专注答题空间中完成提交。</p>
      </div>
      <el-input v-model="courseId" placeholder="课程 ID" style="width: 180px" @change="load" />
    </div>

    <div class="grid">
      <el-card v-for="exam in exams" :key="exam.id" class="panel-card">
        <el-tag type="primary" effect="dark">考试</el-tag>
        <h3 style="margin-top: 12px">{{ exam.title }}</h3>
        <p>考试时长 {{ exam.durationMinutes }} 分钟，及格分 {{ exam.passScore }} 分。</p>
        <el-button type="primary" @click="open(exam.id)">开始考试</el-button>
      </el-card>
    </div>

    <el-dialog v-model="visible" title="考试作答" width="720px">
      <div v-if="examDetail">
        <el-card v-for="(q, index) in examDetail.questions" :key="q.id" class="panel-card" style="margin-bottom: 12px">
          <strong>{{ index + 1 }}. {{ q.title }}</strong>
          <el-input v-model="answers[q.id]" style="margin-top: 12px" placeholder="请输入你的答案" />
        </el-card>
      </div>
      <template #footer>
        <el-button class="ghost-button" @click="visible = false">关闭</el-button>
        <el-button type="primary" @click="submit">提交试卷</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { examApi } from '../api/modules'

const courseId = ref(1)
const exams = ref([])
const visible = ref(false)
const examDetail = ref(null)
const currentExamId = ref(null)
const answers = reactive({})

async function load() {
  exams.value = await examApi.list(courseId.value)
}

async function open(id) {
  currentExamId.value = id
  examDetail.value = await examApi.detail(id)
  visible.value = true
}

async function submit() {
  await examApi.submit({
    examId: currentExamId.value,
    answersJson: JSON.stringify(answers)
  })
  visible.value = false
  ElMessage.success('试卷已提交')
}

onMounted(load)
</script>
