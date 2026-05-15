# 图片资源说明

本目录存放小程序所需的图标资源。

## tabBar 图标清单

微信小程序 tabBar 要求使用 PNG 格式图标,推荐尺寸 81×81 像素。

| 文件名 | 用途 | 颜色 |
|--------|------|------|
| `tab-home.png` | 首页 - 未选中 | 灰色 (#999999) |
| `tab-home-active.png` | 首页 - 选中 | 主题蓝 (#4A90E2) |
| `tab-schedule.png` | 课表 - 未选中 | 灰色 |
| `tab-schedule-active.png` | 课表 - 选中 | 主题蓝 |
| `tab-notice.png` | 通知 - 未选中 | 灰色 |
| `tab-notice-active.png` | 通知 - 选中 | 主题蓝 |
| `tab-me.png` | 我的 - 未选中 | 灰色 |
| `tab-me-active.png` | 我的 - 选中 | 主题蓝 |

## 替换为真实图标

当前的 tabBar 图标为程序生成的纯色占位 PNG。如需替换为带图形的真实图标:

### 方式一: 阿里 Iconfont (推荐)
1. 访问 [iconfont.cn](https://www.iconfont.cn)
2. 搜索并选择以下图标:
   - 首页: 房子/家
   - 课表: 日历/课程表
   - 通知: 铃铛/喇叭
   - 我的: 用户/头像
3. 下载 PNG 格式,尺寸 81×81
4. 制作两份: 灰色 (`#999999`) 和蓝色 (`#4A90E2`)
5. 重命名后覆盖本目录文件

### 方式二: 设计软件自制
使用 Figma / Sketch / Photoshop 绘制 81×81 透明背景 PNG,导出后放入本目录。

### 方式三: 使用图标库
- [Feather Icons](https://feathericons.com)
- [Heroicons](https://heroicons.com)
- [Tabler Icons](https://tabler-icons.io)

## 关于占位图标

当前的纯色占位 PNG 已经能满足小程序加载、编译、运行的所有要求,不影响功能演示。视觉上替换为图形图标会使应用更美观。
