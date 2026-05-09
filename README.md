# 淘票票影评社区

## How to Run

### Docker启动（推荐）
```bash
# 确保已安装 Docker 和 Docker Compose
docker compose up --build -d

# 访问地址
# 前端: http://localhost:8081
# 后端API: http://localhost:8080/api
# MySQL: localhost:3398
```

### 本地启动
```bash
# 1. 启动MySQL数据库（端口3306），创建数据库 taopiaopiao
# 2. 执行 backend/src/main/resources/sql/init.sql 初始化数据

# 3. 启动后端（打包WAR部署到Tomcat）
cd backend
mvn clean package -DskipTests
# 将 target/taopiaopiao.war 部署到 Tomcat 的 webapps 目录
# 或使用 Tomcat Maven 插件：
mvn tomcat7:run

# 4. 启动前端
cd frontend
npm install
npm run serve
```

## Services

| 服务 | 端口 | 说明 |
|------|------|------|
| Frontend | 8081 | Vue2前端应用 |
| Backend | 8080 | SSM后端API (Tomcat) |
| MySQL | 3398 | 数据库服务 |

## 测试账号

| 用户名 | 密码 | 说明 |
|--------|------|------|
| admin | 123456 | 管理员账号 |
| user1 | 123456 | 普通用户1 |
| user2 | 123456 | 普通用户2 |

## 题目内容

开发一个基于SSM框架应用技术的电影评分与评论社区，主要功能包括：用户注册、登录。电影信息的展示，支持按类型、地区等筛选。用户对电影进行评分（1-5星）和文字评论。个人中心，查看“我评价过的电影”和“我的评论”。技术要求包括：使用Maven进行项目构建和依赖管理。使用Spring进行IoC管理。使用Spring MVC处理Web请求和响应，实现RESTful风格API。使用MyBatis作为持久层框架，实现动态SQL查询 。前端页面可使用JSP、Thymeleaf或与Vue/React等技术。在核心技术栈基础上，可以自主集成其他技术。 
可选如下题目： 
1.淘票票影评社区 


要按照评分标准来计划： 
编号 	 评分项目 	 评分标准 	 分值 	 得分 
1 	 项目结构 	 项目结构清晰，符合Maven规范 
符合三层架构规范 
SSM框架配置正确，整合无误 	 20 	 
2 	 核心功能实现 	 用户注册、登录，功能正常 	 15 	 
电影信息的展示，支持按类型、地区等筛选，功能正常 	 15 	 
用户对电影进行评分（1-5星）和文字评论，功能正常 	 15 	 
个人中心，查看“我评价过的电影”和“我的评论”，功能正常 	 15 	 
3 	 代码质量与设计 	 命名规范，结构清晰，有必要的注释。 	 5 	 
4 	 项目整体情况 	 项目可以正常运行 	 15 	 
界面设计合理，用户体验良好

### 功能特性

1. **用户管理**
   - 用户注册、登录
   - 个人中心

2. **电影展示**
   - 电影列表展示
   - 按类型筛选（动作、喜剧、爱情、科幻等）
   - 按地区筛选（中国、美国、日本、韩国等）
   - 电影详情页

3. **评分评论**
   - 1-5星评分
   - 文字评论
   - 查看所有评论

4. **个人中心**
   - 我评价过的电影
   - 我的评论列表

### 技术栈

- **后端**: Spring + Spring MVC + MyBatis (SSM)
- **前端**: Vue2 + Element UI + Axios
- **数据库**: MySQL 8.0
- **构建工具**: Maven + npm
- **容器化**: Docker + Docker Compose

### 项目结构

```
├── backend/                 # 后端项目
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/taopiaopiao/
│   │       │       ├── controller/    # 控制层
│   │       │       ├── service/       # 业务层
│   │       │       ├── mapper/        # 持久层接口
│   │       │       ├── entity/        # 实体类
│   │       │       ├── dto/           # 数据传输对象
│   │       │       ├── exception/     # 异常处理
│   │       │       ├── interceptor/   # 拦截器
│   │       │       ├── filter/        # 过滤器
│   │       │       ├── util/          # 工具类
│   │       │       └── common/        # 公共类
│   │       ├── resources/
│   │       │   ├── mapper/            # MyBatis映射文件
│   │       │   ├── sql/               # 数据库脚本
│   │       │   ├── applicationContext.xml  # Spring配置
│   │       │   ├── spring-mvc.xml     # Spring MVC配置
│   │       │   ├── mybatis-config.xml # MyBatis配置
│   │       │   └── db.properties      # 数据库配置
│   │       └── webapp/
│   │           └── WEB-INF/
│   │               └── web.xml        # Web部署描述符
│   ├── pom.xml
│   └── Dockerfile
├── frontend/                # 前端项目
│   ├── src/
│   │   ├── views/          # 页面组件
│   │   ├── components/     # 公共组件
│   │   ├── router/         # 路由配置
│   │   ├── store/          # Vuex状态管理
│   │   └── api/            # API接口
│   ├── package.json
│   └── Dockerfile
├── docker-compose.yml
└── README.md
```

## API接口列表

### 用户模块
| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | /api/user/register | 用户注册 | 否 |
| POST | /api/user/login | 用户登录 | 否 |
| GET | /api/user/me | 获取当前用户 | 是 |
| GET | /api/user/{id} | 获取用户信息 | 是 |
| PUT | /api/user/{id} | 更新用户信息 | 是 |

### 电影模块
| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | /api/movie/list | 电影列表(分页) | 否 |
| GET | /api/movie/{id} | 电影详情 | 否 |
| GET | /api/movie/filters | 获取筛选选项 | 否 |

### 评论模块
| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | /api/review/movie/{movieId} | 电影评论列表 | 否 |
| GET | /api/review/user/{userId} | 用户评论列表 | 否 |
| GET | /api/review/check | 检查用户评论 | 否 |
| POST | /api/review | 发表/更新评论 | 是 |
| DELETE | /api/review/{id} | 删除评论 | 是 |