# 部署文档

## 后端

1. 准备 MySQL 8 和 Java 17。
2. 执行 `sql/tianji_academy.sql`。
3. 修改 `backend/src/main/resources/application.yml` 中的数据源配置。
4. 启动：

```bash
cd backend
mvn spring-boot:run
```

## 前端

1. 安装 Node.js 18+。
2. 安装依赖：

```bash
cd frontend
npm install
```

3. 启动：

```bash
npm run dev
```

## 生产建议

- 前端使用 Nginx 部署静态文件。
- 后端打包成 jar 部署。
- 上传目录 `uploads/` 需要持久化。
- 生产环境把 JWT 密钥和数据库密码改成环境变量。
后端启动命令：
cd backend
mvn -gs ../maven-settings.xml -s ../maven-settings.xml spring-boot:run
前端启动命令：
cd frontend
npm.cmd install
npm.cmd run dev
浏览器访问：http://localhost:5173
