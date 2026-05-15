# 粉色登录页 (HTML + Vue3)

## 项目简介

本项目是期末作品——基于 HTML5 + Vue3 实现的移动端 APP 登录页面,采用清新粉色风格,模仿正规 APP 设计效果。

## 技术栈

- HTML5 / CSS3 (原生)
- Vue 3 (通过 CDN 引入,零构建)
- 响应式布局,适配移动端

## 目录结构

```
task1-mobile-app/
├── index.html                # 主入口文件 (单文件应用)
├── README.md                 # 本文档
├── docs/                     # 文档目录
│   ├── 设计过程说明.md
│   ├── 设计视频说明.md
│   └── 平时笔记.md           # Vue3 学习笔记
└── assets/
    └── icons/                # 图标图像目录
        ├── README.md
        ├── back.svg          # 返回箭头
        ├── checkbox.svg      # 勾选框
        └── logo.svg          # App Logo
```

## 快速运行

### 方式一: 直接双击
1. 双击 `index.html` 即可在默认浏览器打开
2. 推荐使用 Chrome、Edge、Safari 最新版本

### 方式二: 本地服务器 (推荐)
```bash
# Python 3
python -m http.server 8080

# Node.js
npx http-server -p 8080
```
然后访问 `http://localhost:8080`

### 方式三: VSCode Live Server
1. 安装 VSCode 扩展 "Live Server"
2. 右键 `index.html` → "Open with Live Server"

## 功能说明

| 元素 | 交互行为 |
|------|---------|
| 返回箭头 | 点击弹出"返回上一页"提示 |
| 账号输入框 | 输入手机号或邮箱,实时同步 (v-model) |
| 密码输入框 | 输入密码,密码遮蔽显示 |
| 忘记密码 | 点击弹出找回密码提示 |
| 登录按钮 | 校验账号密码和协议勾选,符合条件提示登录成功 |
| 手机短信登录 | 点击切换至短信登录提示 |
| 协议勾选框 | 点击切换勾选状态,控制登录按钮激活 |
| 用户协议/隐私政策 | 点击弹出对应协议提示 |
| 登录遇到问题 | 点击弹出客服联系方式 |

## 测试账号

任意手机号 (如 13800138000) + 任意密码,勾选协议后可登录。

## 浏览器兼容性

- Chrome 90+
- Edge 90+
- Safari 14+
- Firefox 88+
- 移动端: iOS Safari / Android Chrome

## 加分项说明

1. **VUE3 使用** - 通过 CDN 引入 Vue 3,使用 Composition API (`ref`, `computed`) 实现响应式数据绑定
2. **Markdown 笔记** - 详见 `docs/平时笔记.md`,涵盖 Vue3 核心知识点

## 开发者说明

详见 `docs/设计过程说明.md` 了解设计思路与开发过程。
