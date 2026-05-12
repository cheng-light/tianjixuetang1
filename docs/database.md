# 天机学堂数据库设计

## 核心表

`users` 用户表  
`roles` 角色表  
`user_roles` 用户角色关联表  
`courses` 课程表  
`course_chapters` 课程章节表  
`learning_progress` 学习进度表  
`homeworks` 作业表  
`homework_submissions` 作业提交表  
`exams` 考试表  
`exam_questions` 考题表  
`exam_records` 考试记录表

## 关系

- `users` 1 - N `courses.teacher_id`
- `users` N - N `roles` via `user_roles`
- `courses` 1 - N `course_chapters`
- `courses` 1 - N `homeworks`
- `homeworks` 1 - N `homework_submissions`
- `courses` 1 - N `exams`
- `exams` 1 - N `exam_questions`
- `exams` 1 - N `exam_records`
- `users` 1 - N `learning_progress`

## 初始化账号

- `admin / 123456`
- `teacher / 123456`
- `student / 123456`
