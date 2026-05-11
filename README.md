# JD 电商平台 - 全栈项目

一个仿京东风格的完整全栈电商平台，技术栈：

- **前端**：Vue 3 + Composition API + Pinia + Vue Router + Element Plus
- **后端**：Spring Boot 3 + MyBatis Plus + Spring AI + MySQL
- **认证**：JWT
- **AI 功能**：Spring AI（智能客服、商品推荐、搜索建议、向量语义搜索）— 兼容任何 OpenAI 格式接口

## 项目结构

```
├── docs/API.md              # 接口文档
├── jd-backend/              # Spring Boot 后端
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/jd/
│       │   ├── config/      # Web、安全、全局异常配置
│       │   ├── controller/  # REST 控制器
│       │   ├── service/     # 业务逻辑
│       │   ├── mapper/      # MyBatis Plus Mapper
│       │   ├── entity/      # 实体类
│       │   ├── dto/         # 请求/响应 DTO
│       │   └── util/        # JWT、密码工具
│       └── resources/
│           ├── application.yml
│           ├── schema.sql   # 数据库建表脚本（MySQL）
│           └── data.sql     # 初始演示数据
└── jd-frontend/             # Vue 3 前端
    ├── package.json
    ├── vite.config.js
    └── src/
        ├── api/             # Axios 接口封装
        ├── components/      # AppHeader、AppFooter、ProductCard、AIChat
        ├── views/           # 页面（首页、列表、详情、购物车、订单、登录、管理后台...）
        ├── stores/          # Pinia 状态（userStore、cartStore、favoriteStore）
        └── router/          # 路由配置（含鉴权守卫）
```

## 功能模块

### 用户功能
- 注册 / 登录（JWT 认证，Token 持久化到 localStorage）
- 个人资料管理（手机号、头像）
- 收货地址 CRUD（支持设置默认地址）
- 浏览历史 / 收藏夹

### 商品功能
- 首页：轮播横幅、分类导航、限时闪购、分类商品展示、最近浏览
- 商品列表：分类筛选、关键词搜索、价格区间、多维排序（综合/销量/价格/评分）
- 商品详情：多图切换、规格展示、真实用户评价（含写评价）、关联推荐
- 搜索：关键词 + AI 搜索建议联想 + 向量语义搜索

### 购物功能
- 购物车：增删改、单选/全选、实时小计
- 下单结算：地址选择/新增、备注、优惠码使用、订单摘要
- 订单管理：按状态筛选、模拟支付、取消、确认收货、申请退款

### 管理后台（ADMIN 角色）
- 商品管理：CRUD、上下架、库存
- 订单管理：查看全部订单、标记发货
- 优惠券管理：创建满减/折扣券
- 用户管理：用户列表
- 向量索引：一键重建商品 embedding

### AI 功能（Spring AI，OpenAI 兼容接口）
- 🤖 **AI 智能客服**：右下角悬浮聊天窗口，支持多轮对话
- 🔍 **AI 搜索建议**：搜索框输入时实时联想
- 🎯 **AI 商品推荐**：自然语言描述需求 + 预算，AI 分析并推荐
- 🧠 **向量语义搜索**：用 embedding 模型为商品建索引，余弦相似度匹配

## 快速启动

### 1. 准备 MySQL

需要本地或远程运行的 MySQL 5.7+ / 8.0。默认会自动创建数据库 `jddb`：

```bash
# 启动 MySQL（macOS/Linux 示例）
brew services start mysql      # macOS
sudo systemctl start mysql     # Linux

# 默认连接：jdbc:mysql://localhost:3306/jddb，用户 root，无密码
# 通过环境变量自定义：
export MYSQL_URL="jdbc:mysql://localhost:3306/jddb?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true"
export MYSQL_USER=root
export MYSQL_PASSWORD=your_password
```

### 2. 启动后端

```bash
cd jd-backend
mvn spring-boot:run
```

首次启动会自动建表 + 灌入演示数据（schema.sql + data.sql）。  
**保留数据**：之后启动设置 `SPRING_SQL_INIT_MODE=never` 即可跳过重建表。

- API 地址：`http://localhost:8080`

### 3. 启动前端

```bash
cd jd-frontend
npm install
npm run dev
```

访问 `http://localhost:5173`，API 请求通过 Vite Proxy 转发到后端 8080。

## AI 配置（OpenAI 兼容接口）

支持任何 OpenAI 兼容的服务（OpenAI / DeepSeek / Moonshot / Zhipu / OpenRouter / 本地 vLLM 等）。  
通过环境变量自由指定 base URL、聊天模型、向量模型：

```bash
# OpenAI（默认）
export OPENAI_API_KEY=sk-xxx
export AI_BASE_URL=https://api.openai.com
export AI_CHAT_MODEL=gpt-4o-mini
export AI_EMBEDDING_MODEL=text-embedding-3-small

# DeepSeek
export OPENAI_API_KEY=sk-deepseek-xxx
export AI_BASE_URL=https://api.deepseek.com
export AI_CHAT_MODEL=deepseek-chat
# DeepSeek 没有 embedding，向量功能用别的服务

# Moonshot Kimi
export OPENAI_API_KEY=sk-moonshot-xxx
export AI_BASE_URL=https://api.moonshot.cn/v1
export AI_CHAT_MODEL=moonshot-v1-8k

# Zhipu GLM
export OPENAI_API_KEY=your-zhipu-key
export AI_BASE_URL=https://open.bigmodel.cn/api/paas/v4
export AI_CHAT_MODEL=glm-4-plus
export AI_EMBEDDING_MODEL=embedding-3
```

未配置 API Key 时 AI 客服自动使用内置回复，向量搜索禁用，其他功能正常。

应用启动后会为所有商品异步生成 embedding；管理后台「重建向量索引」按钮可手动重建。

## 演示账号

| 账号  | 密码   | 权限     |
|-------|--------|----------|
| demo  | 123456 | 普通用户 |
| admin | 123456 | 管理员（可访问 /admin） |

## 演示优惠码

| 码        | 类型   | 优惠       | 门槛   |
|-----------|--------|------------|--------|
| WELCOME10 | 满减   | 减¥10      | 满¥50  |
| SAVE20    | 满减   | 减¥20      | 满¥100 |
| SAVE50    | 满减   | 减¥50      | 满¥300 |
| DISCOUNT9 | 折扣   | 9折        | 无门槛 |
| VIP8      | 折扣   | 8折        | 满¥500 |

## 接口文档

详见 [docs/API.md](docs/API.md)
