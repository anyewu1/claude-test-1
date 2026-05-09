# JD 电商平台 - 全栈项目

一个仿京东风格的完整全栈电商平台，技术栈：

- **前端**：Vue 3 + Composition API + Pinia + Vue Router + Element Plus
- **后端**：Spring Boot 3 + MyBatis Plus + Spring AI + H2（可切换 MySQL）
- **认证**：JWT
- **AI功能**：Spring AI（智能客服、商品推荐、搜索建议）

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
│           ├── schema.sql   # 数据库建表脚本
│           └── data.sql     # 初始演示数据
└── jd-frontend/             # Vue 3 前端
    ├── package.json
    ├── vite.config.js
    └── src/
        ├── api/             # Axios 接口封装
        ├── components/      # AppHeader、AppFooter、ProductCard、AIChat
        ├── views/           # 页面（首页、列表、详情、购物车、订单、登录...）
        ├── stores/          # Pinia 状态（userStore、cartStore）
        └── router/          # 路由配置（含鉴权守卫）
```

## 功能模块

### 用户功能
- 注册 / 登录（JWT 认证，Token 持久化到 localStorage）
- 个人资料管理（手机号、头像）
- 收货地址 CRUD（支持设置默认地址）

### 商品功能
- 首页：轮播横幅、分类导航、限时闪购、分类商品展示
- 商品列表：分类筛选、关键词搜索、价格区间、多维排序（综合/销量/价格/评分）
- 商品详情：多图切换、规格展示、用户评价、关联推荐
- 搜索：关键词 + AI 搜索建议联想

### 购物功能
- 购物车：增删改、单选/全选、实时小计
- 下单结算：地址选择/新增、备注、订单摘要
- 订单管理：按状态筛选、模拟支付、取消、确认收货

### AI 功能（Spring AI + OpenAI）
- 🤖 **AI 智能客服**：右下角悬浮聊天窗口，支持多轮对话
- 🔍 **AI 搜索建议**：搜索框输入时实时联想
- 🎯 **AI 商品推荐**：首页自然语言描述需求 + 预算，AI 分析并推荐

## 快速启动

### 后端

```bash
cd jd-backend
mvn spring-boot:run
```

使用 H2 内存数据库，**无需安装 MySQL**，启动即用。

- API 地址：`http://localhost:8080`
- H2 控制台：`http://localhost:8080/h2-console`（JDBC URL: `jdbc:h2:mem:jddb`，用户名 `sa`，密码空）

**启用 AI 功能（可选）**：
```bash
export OPENAI_API_KEY=your_openai_key
mvn spring-boot:run
```
未配置 API Key 时自动使用内置回复，AI 客服仍可正常使用。

### 前端

```bash
cd jd-frontend
npm install
npm run dev
```

访问 `http://localhost:5173`，API 请求通过 Vite Proxy 转发到后端 8080。

## 演示账号

| 账号  | 密码   | 权限     |
|-------|--------|----------|
| demo  | 123456 | 普通用户 |
| admin | 123456 | 管理员   |

## 接口文档

详见 [docs/API.md](docs/API.md)

## 切换 MySQL（生产环境）

修改 `jd-backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/jddb?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: your_password
```

执行 `schema.sql` 建表后执行 `data.sql` 导入演示数据即可。
