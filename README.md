# 知识分享平台

## 项目介绍

知识分享平台是一个基于Spring Boot和Vue的全栈应用，旨在为用户提供一个分享知识、经验和项目的平台。用户可以发布文章、项目展示和经验分享，也可以浏览、评论和点赞他人的内容。平台支持分类、标签、搜索等功能，方便用户快速找到感兴趣的内容。

## 技术栈

### 后端技术栈

- **Spring Boot 3.0.5**：应用框架
- **MyBatis-Plus 3.5.3.1**：ORM框架
- **MySQL**：数据库
- **JWT**：用户认证
- **Redis**：缓存（可选）
- **Knife4j**：API文档

### 前端技术栈

- **Vue**：前端框架
- **Element UI**：UI组件库
- **Axios**：HTTP客户端

## 项目结构

项目采用多模块设计，主要包含以下模块：

```
knowledge/
├── know-pojo/        # 实体类模块
├── know-common/      # 公共工具模块
└── know-server/      # 服务模块
```

### 模块说明

- **know-pojo**：包含所有实体类、DTO、VO等
- **know-common**：包含公共工具类、异常处理、结果封装等
- **know-server**：包含控制器、服务实现、配置等

## 功能特性

### 用户功能

- 用户注册、登录、退出
- 用户信息查看和修改
- 密码修改

### 内容管理

- 文章发布、编辑、删除
- 项目展示发布、编辑、删除
- 经验分享发布、编辑、删除
- 评论功能
- 点赞功能
- 收藏功能

### 内容浏览

- 分类浏览
- 标签筛选
- 关键词搜索
- 热门内容推荐
- 轮播展示

### 其他功能

- 工具推荐
- 竞赛信息

## 数据库设计

系统包含以下主要数据表：

- **users**：用户信息
- **articles**：文章
- **projects**：项目
- **experiences**：经验分享
- **categories**：分类
- **tags**：标签
- **article_tags**：文章-标签关联
- **project_tags**：项目-标签关联
- **experience_tags**：经验-标签关联
- **comments**：评论
- **user_likes**：用户点赞
- **user_favorites**：用户收藏
- **tools**：工具
- **competitions**：竞赛
- **carousel_items**：轮播项

## 安装部署

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 5.7+
- Redis (可选)
- Node.js 14+ (前端)

### 后端部署步骤

1. **克隆项目**

```bash
git clone https://github.com/yourusername/knowledge.git
cd knowledge
```

2. **配置数据库**

修改 `know-server/src/main/resources/application.yml` 文件中的数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/knowledge?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=GMT%2b8
    username: your_username
    password: your_password
```

3. **初始化数据库**

执行 `database/init.sql` 脚本创建数据库和表结构。

4. **编译打包**

```bash
mvn clean package -DskipTests
```

5. **运行应用**

```bash
java -jar know-server/target/know-server-1.0-SNAPSHOT.jar
```

### 前端部署步骤

1. **进入前端目录**

```bash
cd knowledge-frontend
```

2. **安装依赖**

```bash
npm install
```

3. **配置API地址**

修改 `.env.production` 文件中的API地址：

```
VUE_APP_API_BASE_URL=http://your-api-host:8080/api
```

4. **编译打包**

```bash
npm run build
```

5. **部署静态文件**

将 `dist` 目录下的文件部署到Web服务器（如Nginx）。

## API文档

启动应用后，访问以下地址查看API文档：

```
http://localhost:8080/api/doc.html
```

## 开发指南

### 添加新实体

1. 在 `know-pojo` 模块中创建实体类
2. 在 `know-server` 模块中创建Mapper接口
3. 在 `know-server` 模块中创建Service接口和实现
4. 在 `know-server` 模块中创建Controller

### 自动填充字段

系统使用MyBatis-Plus的自动填充功能处理以下字段：

- `createTime`：创建时间
- `createBy`：创建人
- `updateTime`：更新时间
- `updateBy`：更新人
- `delFlag`：删除标志（逻辑删除）
- `viewCount`：浏览量
- `likeCount`：点赞数
- `commentCount`：评论数

## 贡献指南

1. Fork 本仓库
2. 创建您的特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交您的更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 打开一个 Pull Request

## 许可证

本项目采用 MIT 许可证 - 详情请参见 [LICENSE](LICENSE) 文件。

## 联系方式

- 项目维护者：yongyiq
- 邮箱：1763364834@qq.com

## 致谢

感谢所有为本项目做出贡献的开发者！
