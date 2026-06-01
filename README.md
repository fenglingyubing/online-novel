# online-novel 在线小说阅读系统

## 项目简介

online-novel 是一个基于 Spring Boot 3 开发的在线小说阅读系统，支持用户阅读小说、加入书架、发表评论、记录阅读历史；支持作家发布作品、管理章节、提交审核；支持管理员进行小说审核、章节审核、公告管理、推荐管理和用户管理。

本项目由本人从 0 到 1 独立设计并开发，包含数据库设计、接口开发、权限认证、Redis 缓存、文件上传、接口文档编写和线上部署等完整流程。

## 在线演示

- 后端项目地址：https://github.com/fenglingyubing/online-novel
- 项目演示地址：https://novel.fenglingyubing.dpdns.org/
- 接口文档：`src/main/resources/docs/api.md`

测试账号：

| 角色 | 用户名 | 密码 |
| ---- | ------ | ---- |
| 普通用户 | zhangsan | 123456 |
| 作家 | fengling | 123456 |
| 管理员 | admin | 123456 |

## 技术栈

| 分类 | 技术 |
| --- | --- |
| 后端框架 | Spring Boot 3 |
| ORM 框架 | MyBatis-Plus |
| 数据库 | MySQL |
| 缓存 | Redis |
| 权限认证 | JWT |
| 文件存储 | 阿里云 OSS |
| 构建工具 | Maven |
| 接口风格 | RESTful API |
| 部署相关 | Docker、Docker Compose、Nginx |

## 功能模块

### 用户端

- 用户注册、登录、退出登录
- 用户信息查询与修改
- 用户头像上传
- 小说分类查询
- 小说列表查询
- 小说详情查询
- 小说章节阅读
- 最新上架小说查询
- 首页推荐小说查询
- 加入书架、移出书架、书架列表查询
- 阅读历史记录与查询
- 小说评论与评论回复

### 作家端

- 作家注册
- 作家首页数据查询
- 作品列表查询
- 新建作品并提交审核
- 小说信息修改并提交审核
- 小说封面上传
- 章节新增、编辑、删除
- 草稿箱管理
- 章节审核状态查询
- 作家公告查询

### 管理员端

- 用户列表查询
- 用户封禁与解封
- 新书审核
- 小说信息变更审核
- 章节审核
- 推荐小说管理
- 公告创建、修改、发布和查询

## 项目亮点

### 1. 从 0 到 1 独立完成完整业务闭环

项目围绕用户端、作家端、管理员端三类角色展开，覆盖小说阅读、内容创作、内容审核、评论互动、书架管理、公告推荐等业务流程，不是单一表结构的简单 CRUD 项目。

### 2. 基于 JWT + Redis 实现登录认证

系统使用 JWT 生成登录令牌，并将 Token 存入 Redis，实现登录态管理、退出登录、接口权限拦截和多角色访问控制。

认证流程：

1. 用户登录成功后生成 JWT。
2. 后端将 JWT 写入 Redis，并设置过期时间。
3. 前端请求需要登录的接口时，在请求头中携带 `Authorization: Bearer token`。
4. 拦截器校验 JWT 合法性，并从 Redis 校验登录状态。
5. 校验通过后将用户信息写入线程上下文，供后续业务逻辑使用。

### 3. 阅读历史使用 Redis 异步落库

阅读历史属于高频更新场景，如果每次阅读进度变化都直接写入 MySQL，会产生较多重复写入。

本项目采用 Redis 缓冲阅读历史：

- 使用 Redis Hash 保存用户最新阅读进度。
- 使用 Redis ZSet 标记需要同步的用户和小说。
- 通过定时任务批量同步到 MySQL。
- 同步失败后重新加入 ZSet，等待下次重试。

该方案减少了阅读历史频繁写库的压力，同时保证数据最终可以同步到数据库。

### 4. 多角色内容审核流程

系统实现了作家提交、管理员审核、审核结果回写的内容管理流程，支持新书审核、小说信息变更审核和章节审核，避免作家提交内容后直接展示到用户端。

### 5. 数据库表结构围绕业务场景设计

项目围绕用户、作家、小说、章节、书架、评论、审核、公告、推荐、阅读历史等业务设计数据表，并针对常用查询场景添加唯一索引和联合索引。

例如：

- 用户名唯一索引，避免重复注册。
- 用户书架唯一索引，避免重复加入同一本小说。
- 阅读历史用户-小说唯一索引，方便更新阅读进度。
- 小说分类、发布状态、更新时间联合索引，优化列表查询。

### 6. 接入阿里云 OSS 实现文件上传

系统接入阿里云 OSS，用于存储用户头像和小说封面，避免文件直接存储在应用服务器，提高文件访问和部署的灵活性。

## 系统架构 / 核心流程

### 登录认证流程

```text
用户登录
   ↓
校验用户名和密码
   ↓
生成 JWT
   ↓
JWT 写入 Redis
   ↓
返回 Token 给前端
   ↓
前端请求接口时携带 Token
   ↓
拦截器校验 JWT 和 Redis 登录态
   ↓
放行请求并写入用户上下文
```

### 阅读历史同步流程

```text
用户阅读小说章节
   ↓
更新阅读进度
   ↓
最新进度写入 Redis Hash
   ↓
用户-小说标识写入 Redis ZSet
   ↓
定时任务扫描待同步数据
   ↓
同步到 MySQL
   ↓
同步失败则重新加入 ZSet 等待重试
```

### 小说发布审核流程

```text
作家创建/修改小说或章节
   ↓
提交审核
   ↓
管理员查看审核列表
   ↓
管理员审核通过或驳回
   ↓
审核通过后内容正式展示
```

## 项目目录

```text
online-novel
├── .mvn/                         # Maven Wrapper 配置
├── src/
│   ├── main/
│   │   ├── java/com/fengling/
│   │   │   ├── common/           # 通用模块：常量、上下文、DTO、异常、响应、工具类
│   │   │   ├── config/           # 项目配置类
│   │   │   ├── controller/       # 接口控制层
│   │   │   │   ├── admin/        # 管理员端接口
│   │   │   │   ├── author/       # 作家端接口
│   │   │   │   └── front/        # 用户端接口
│   │   │   ├── entity/           # 实体类与业务 DTO
│   │   │   │   └── dto/          # 请求/响应 DTO
│   │   │   ├── interceptor/      # 登录认证与可选登录拦截器
│   │   │   ├── mapper/           # MyBatis-Plus Mapper 接口
│   │   │   ├── service/          # 业务服务接口
│   │   │   │   └── impl/         # 业务服务实现
│   │   │   └── task/             # 定时任务
│   │   └── resources/
│   │       ├── docs/             # 项目文档
│   │       ├── mapper/           # MyBatis XML 映射文件
│   │       ├── sql/              # 数据库初始化脚本
│   │       ├── static/           # 静态资源目录
│   │       ├── templates/        # 模板目录
│   │       ├── application.yml   # 应用配置文件
│   │       ├── Dockerfile        # Docker 镜像构建文件
│   │       └── docker-compose.yml# Docker Compose 配置
│   └── test/                     # 测试代码
├── pom.xml                       # Maven 项目配置
├── mvnw                          # Linux/macOS Maven Wrapper
├── mvnw.cmd                      # Windows Maven Wrapper
├── HELP.md
├── LEARNING_PLAN.md
└── README.md
```

## 环境要求

- JDK 21 或以上
- Maven 3.8 或以上
- MySQL 8.x
- Redis 6.x 或以上
- 阿里云 OSS Bucket

> 当前 `Dockerfile` 使用 `eclipse-temurin:21-jre` 作为运行环境。

## 本地启动

### 1. 克隆项目

```bash
git clone https://github.com/fenglingyubing/online-novel.git
cd online-novel
```

### 2. 创建数据库

```sql
CREATE DATABASE `online-novel` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

### 3. 初始化数据库

执行数据库脚本：

```text
src/main/resources/sql/init.sql
```

如果需要导入完整数据，可以执行：

```text
src/main/resources/sql/online-novel.sql
```

### 4. 配置环境变量

项目通过环境变量读取数据库、JWT 和 OSS 配置。可以在系统环境变量中配置，也可以在本地创建 `.env` 文件。

```env
MYSQL_USERNAME=root
MYSQL_PASSWORD=your_mysql_password

JWT_SECRET=your_jwt_secret_at_least_32_chars

ALIYUN_OSS_ACCESS_KEY_ID=your_access_key_id
ALIYUN_OSS_ACCESS_KEY_SECRET=your_access_key_secret
```

> `.env` 文件包含敏感信息，不要提交到 Git 仓库。

### 5. 启动依赖服务

确保本地 MySQL 和 Redis 已启动，并且 Redis 使用默认地址：

```text
127.0.0.1:6379
```

### 6. 启动后端服务

Windows：

```bash
mvnw.cmd spring-boot:run
```

Linux/macOS：

```bash
./mvnw spring-boot:run
```

服务启动后默认监听：

```text
http://localhost:8080
```

## 环境变量说明

| 变量名 | 是否必填 | 说明 |
| --- | --- | --- |
| `MYSQL_USERNAME` | 是 | MySQL 用户名 |
| `MYSQL_PASSWORD` | 是 | MySQL 密码 |
| `JWT_SECRET` | 是 | JWT 签名密钥，建议不少于 32 个字符 |
| `ALIYUN_OSS_ACCESS_KEY_ID` | 是 | 阿里云 OSS AccessKey ID |
| `ALIYUN_OSS_ACCESS_KEY_SECRET` | 是 | 阿里云 OSS AccessKey Secret |

## 数据库初始化

数据库脚本位于：

```text
src/main/resources/sql/init.sql
src/main/resources/sql/online-novel.sql
```

说明：

- `init.sql`：用于初始化基础表结构。
- `online-novel.sql`：用于导入完整数据库结构或示例数据。

## 接口文档

接口文档位于：

```text
src/main/resources/docs/api.md
```

登录认证方式：

```http
Authorization: Bearer token值
```

## 部署说明

项目提供了 Docker 相关配置文件：

```text
src/main/resources/Dockerfile
src/main/resources/docker-compose.yml
src/main/resources/docs/部署.md
```

部署时需要准备：

- MySQL
- Redis
- 后端应用 Jar 包
- Nginx 反向代理配置
- 阿里云 OSS 环境变量

生产环境建议：

- 不要将数据库密码、JWT 密钥、OSS 密钥写死在配置文件中。
- 使用环境变量或服务器配置文件管理敏感信息。
- 关闭生产环境 SQL 控制台打印。
- 使用 Nginx 统一代理前端静态资源和后端接口。

## 后续优化

- 增加 Swagger / Knife4j 在线接口文档。
- 增加核心业务单元测试和接口测试。
- 拆分开发环境和生产环境配置。
- 完善 Docker Compose 一键部署流程。
- 增加接口限流，防止恶意请求。
- 增加日志文件输出和异常监控。
- 优化小说搜索、评论点赞等高频功能。
