import http from './http'

export const authApi = {
  login: (data) => http.post('/api/auth/login', data)
}

export const courseApi = {
  list: () => http.get('/api/courses'),
  detail: (id) => http.get(`/api/courses/${id}`),
  create: (data) => http.post('/api/courses', data),
  createChapter: (courseId, data) => http.post(`/api/courses/${courseId}/chapters`, data)
}

export const learningApi = {
  progress: (courseId) => http.get('/api/learning/progress', { params: { courseId } }),
  saveProgress: (data) => http.post('/api/learning/progress', data)
}

export const homeworkApi = {
  list: (courseId) => http.get('/api/homeworks', { params: { courseId } }),
  submit: (data) => http.post('/api/homeworks/submissions', data)
}

export const examApi = {
  list: (courseId) => http.get('/api/exams', { params: { courseId } }),
  detail: (id) => http.get(`/api/exams/${id}`),
  submit: (data) => http.post('/api/exams/records', data)
}

export const adminApi = {
  dashboard: () => http.get('/api/admin/dashboard'),
  users: () => http.get('/api/admin/users')
}

export const fileApi = {
  upload: (file) => {
    const form = new FormData()
    form.append('file', file)
    return http.post('/api/files/upload', form)
  }
}
