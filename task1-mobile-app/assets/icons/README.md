# 图标资源说明

本目录存放项目使用的图标资源,全部采用 SVG 矢量格式,体积小、可无损缩放。

## 图标清单

| 文件名 | 说明 | 使用位置 | 来源 |
|--------|------|---------|------|
| `back.svg` | 返回箭头 | 导航栏左侧 | 自绘 SVG |
| `checkbox.svg` | 圆形勾选框 | 底部协议区 | 自绘 SVG |
| `logo.svg` | App Logo (粉色心形) | 项目主图 | 自绘 SVG |

## 状态栏图标说明

`index.html` 中状态栏的网络/电量/4G 等图标采用内联 SVG (`<svg>...</svg>` 直接写入 HTML),未单独导出文件,目的是减少 HTTP 请求,提升首屏加载速度。

## 版权说明

所有 SVG 图标均为本项目原创绘制 (Inline SVG),不涉及第三方版权,可自由用于学习和作业提交。

## 替换说明

如需替换为其他图标:
1. 准备同样命名的 SVG 文件覆盖即可
2. 推荐图标库:
   - [Iconfont](https://www.iconfont.cn) - 阿里图标
   - [Feather Icons](https://feathericons.com) - 极简线性
   - [Heroicons](https://heroicons.com) - Tailwind 出品
