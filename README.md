# 期末作品提交

> 包含两个独立项目的完整作品集

## 项目一: 粉色登录页 (HTML + Vue3)

📁 [task1-mobile-app/](./task1-mobile-app/)

基于 HTML5 + Vue3 实现的移动端 APP 登录页面,精准还原参考设计,采用清新粉色风格。

**技术栈**: HTML5 / CSS3 / Vue3 (CDN 引入)

**亮点**:
- 单文件即可运行,无需构建
- 使用 Vue3 Composition API
- 响应式数据双向绑定
- 完整移动端适配

**关键文档**:
- [整体使用说明](./task1-mobile-app/README.md)
- [设计过程说明](./task1-mobile-app/docs/设计过程说明.md)
- [设计视频说明](./task1-mobile-app/docs/设计视频说明.md)
- [Vue3 平时笔记](./task1-mobile-app/docs/平时笔记.md) (加分项)

**快速运行**: 双击 `task1-mobile-app/index.html`

---

## 项目二: 校园助手 (微信小程序)

📁 [task2-miniprogram/](./task2-miniprogram/)

校园生活一站式助手,集成课程表、成绩查询、图书借阅、签到打卡、通知公告等核心功能。

**技术栈**: 原生微信小程序框架 (WXML + WXSS + JavaScript)

**亮点**:
- **10 个完整页面**: 首页、课表、成绩、图书、图书详情、通知、日程、签到、我的、设置
- 完整的本地数据存储与持久化
- 月历视图、签到日历、GPA 自动计算
- 4 个 tabBar + 6 个二级页面

**关键文档**:
- [项目说明](./task2-miniprogram/README.md)
- [使用说明](./task2-miniprogram/使用说明.md) (10 分)
- [部署说明](./task2-miniprogram/部署说明.md) (10 分)
- [开发说明](./task2-miniprogram/开发说明.md)

**快速运行**: 使用微信开发者工具导入 `task2-miniprogram/` 目录

---

## 提交清单

| 项目 | 内容 | 完成情况 |
|------|------|---------|
| Task 1 - HTML 源代码 | `task1-mobile-app/index.html` | ✅ |
| Task 1 - 设计过程说明 | `docs/设计过程说明.md` | ✅ |
| Task 1 - 整体使用说明 | `README.md` | ✅ |
| Task 1 - 图标图像目录 | `assets/icons/` | ✅ |
| Task 1 - 设计视频说明 | `docs/设计视频说明.md` (含录屏脚本,需本地录制) | ⚠️ |
| Task 1 - Vue3 加分 | 使用 Vue3 CDN | ✅ |
| Task 1 - Markdown 笔记 | `docs/平时笔记.md` | ✅ |
| Task 2 - 小程序源码 | `task2-miniprogram/` 完整目录 | ✅ |
| Task 2 - 页面数量 | 10 个页面 (满分 40) | ✅ |
| Task 2 - 说明文档 | `使用说明.md` (10 分) | ✅ |
| Task 2 - 部署文档 | `部署说明.md` (10 分) | ✅ |
| Task 2 - 兼容性美观性 | `rpx` 适配 + 安全区 + 主题色 (10 分) | ✅ |

## 目录树

```
claude-test-1/
├── README.md                      # 本文件 (总入口)
├── task1-mobile-app/              # 项目一: HTML + Vue3
│   ├── index.html
│   ├── README.md
│   ├── docs/
│   │   ├── 设计过程说明.md
│   │   ├── 设计视频说明.md
│   │   └── 平时笔记.md
│   └── assets/icons/
└── task2-miniprogram/             # 项目二: 微信小程序
    ├── app.js, app.json, app.wxss
    ├── project.config.json
    ├── sitemap.json
    ├── pages/ (10 个页面)
    ├── utils/
    ├── images/
    ├── README.md
    ├── 使用说明.md
    ├── 部署说明.md
    └── 开发说明.md
```
