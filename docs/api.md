# 接口文档

统一返回格式：

```json
{ "code": 200, "message": "success", "data": {} }
```

## 认证

- `POST /api/auth/login`

请求：
```json
{ "username": "admin", "password": "123456" }
```

## 课程

- `GET /api/courses`
- `GET /api/courses/{id}`
- `POST /api/courses`
- `POST /api/courses/{courseId}/chapters`

## 学习进度

- `GET /api/learning/progress`
- `POST /api/learning/progress`

## 作业

- `GET /api/homeworks?courseId=1`
- `POST /api/homeworks`
- `POST /api/homeworks/submissions`

## 考试

- `GET /api/exams?courseId=1`
- `GET /api/exams/{id}`
- `POST /api/exams`
- `POST /api/exams/{examId}/questions`
- `POST /api/exams/records`

## 文件

- `POST /api/files/upload`

## 管理端

- `GET /api/admin/dashboard`
- `GET /api/admin/users`
- `GET /api/admin/courses`
