CREATE DATABASE IF NOT EXISTS tianji_academy DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tianji_academy;

DROP TABLE IF EXISTS exam_records;
DROP TABLE IF EXISTS exam_questions;
DROP TABLE IF EXISTS exams;
DROP TABLE IF EXISTS homework_submissions;
DROP TABLE IF EXISTS homeworks;
DROP TABLE IF EXISTS learning_progress;
DROP TABLE IF EXISTS course_chapters;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(64) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  nickname VARCHAR(64) NOT NULL,
  email VARCHAR(128),
  mobile VARCHAR(32),
  avatar VARCHAR(255),
  status TINYINT NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE roles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(32) NOT NULL UNIQUE,
  name VARCHAR(64) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_roles (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE courses (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(128) NOT NULL,
  description TEXT,
  cover_url VARCHAR(255),
  teacher_id BIGINT,
  category VARCHAR(64),
  price DECIMAL(10,2) NOT NULL DEFAULT 0,
  status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_courses_teacher FOREIGN KEY (teacher_id) REFERENCES users(id)
);

CREATE TABLE course_chapters (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  course_id BIGINT NOT NULL,
  title VARCHAR(128) NOT NULL,
  video_url VARCHAR(255),
  duration_seconds INT NOT NULL DEFAULT 0,
  sort_order INT NOT NULL DEFAULT 0,
  free_preview TINYINT NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_course_chapters_course FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE TABLE learning_progress (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  course_id BIGINT NOT NULL,
  chapter_id BIGINT NOT NULL,
  learned_seconds INT NOT NULL DEFAULT 0,
  completed TINYINT NOT NULL DEFAULT 0,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_progress_user_chapter (user_id, chapter_id),
  CONSTRAINT fk_progress_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_progress_course FOREIGN KEY (course_id) REFERENCES courses(id),
  CONSTRAINT fk_progress_chapter FOREIGN KEY (chapter_id) REFERENCES course_chapters(id)
);

CREATE TABLE homeworks (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  course_id BIGINT NOT NULL,
  title VARCHAR(128) NOT NULL,
  content TEXT NOT NULL,
  deadline DATETIME,
  created_by BIGINT,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_homeworks_course FOREIGN KEY (course_id) REFERENCES courses(id),
  CONSTRAINT fk_homeworks_creator FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE homework_submissions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  homework_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  answer TEXT NOT NULL,
  attachment_url VARCHAR(255),
  score INT,
  feedback VARCHAR(512),
  submitted_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  graded_at DATETIME,
  UNIQUE KEY uk_submission_homework_user (homework_id, user_id),
  CONSTRAINT fk_submissions_homework FOREIGN KEY (homework_id) REFERENCES homeworks(id),
  CONSTRAINT fk_submissions_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE exams (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  course_id BIGINT NOT NULL,
  title VARCHAR(128) NOT NULL,
  duration_minutes INT NOT NULL DEFAULT 60,
  pass_score INT NOT NULL DEFAULT 60,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_exams_course FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE TABLE exam_questions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  exam_id BIGINT NOT NULL,
  question_type VARCHAR(20) NOT NULL,
  title TEXT NOT NULL,
  options_json JSON,
  answer VARCHAR(255) NOT NULL,
  score INT NOT NULL DEFAULT 5,
  sort_order INT NOT NULL DEFAULT 0,
  CONSTRAINT fk_exam_questions_exam FOREIGN KEY (exam_id) REFERENCES exams(id)
);

CREATE TABLE exam_records (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  exam_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  answers_json JSON NOT NULL,
  score INT NOT NULL DEFAULT 0,
  passed TINYINT NOT NULL DEFAULT 0,
  submitted_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_exam_records_exam FOREIGN KEY (exam_id) REFERENCES exams(id),
  CONSTRAINT fk_exam_records_user FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO roles(code, name) VALUES ('ADMIN', 'Admin'), ('TEACHER', 'Teacher'), ('STUDENT', 'Student');
INSERT INTO users(username, password, nickname, email) VALUES
('admin', '{noop}123456', 'Admin', 'admin@tianji.local'),
('teacher', '{noop}123456', 'Teacher', 'teacher@tianji.local'),
('student', '{noop}123456', 'Student', 'student@tianji.local');
INSERT INTO user_roles(user_id, role_id) VALUES (1, 1), (2, 2), (3, 3);

INSERT INTO courses(title, description, cover_url, teacher_id, category, price, status) VALUES
('Spring Boot Enterprise Practice', 'A full course covering REST API, security, JWT and business modules.', '/uploads/sample-cover.jpg', 2, 'Backend', 199.00, 'PUBLISHED');
INSERT INTO course_chapters(course_id, title, video_url, duration_seconds, sort_order, free_preview) VALUES
(1, 'Architecture and Project Setup', '/uploads/sample-video.mp4', 900, 1, 1),
(1, 'JWT Login and Authorization', '/uploads/sample-video.mp4', 1200, 2, 0);
INSERT INTO homeworks(course_id, title, content, created_by) VALUES
(1, 'Finish Auth API Integration', 'Submit your notes for login, user profile and authorization integration.', 2);
INSERT INTO exams(course_id, title, duration_minutes, pass_score) VALUES
(1, 'Spring Boot Basic Exam', 30, 60);
INSERT INTO exam_questions(exam_id, question_type, title, options_json, answer, score, sort_order) VALUES
(1, 'SINGLE', 'Which mechanism is commonly used for stateless authentication?', JSON_ARRAY('JWT', 'CSS', 'HTML', 'SQL'), 'JWT', 10, 1);
