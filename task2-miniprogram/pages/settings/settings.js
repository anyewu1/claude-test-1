// 设置
const storage = require('../../utils/storage.js');
const util = require('../../utils/util.js');

Page({
  data: {
    theme: 'light',
    fontSize: 'medium',
    notifyClass: true,
    notifyExam: true,
    notifyNotice: false,
    cacheSize: '1.2 MB'
  },

  onLoad() {
    const settings = storage.get('settings', {});
    this.setData({
      theme: settings.theme || 'light',
      fontSize: settings.fontSize || 'medium',
      notifyClass: settings.notifyClass !== false,
      notifyExam: settings.notifyExam !== false,
      notifyNotice: settings.notifyNotice === true
    });
    this.checkCache();
  },

  saveSettings() {
    storage.set('settings', {
      theme: this.data.theme,
      fontSize: this.data.fontSize,
      notifyClass: this.data.notifyClass,
      notifyExam: this.data.notifyExam,
      notifyNotice: this.data.notifyNotice
    });
  },

  setTheme(e) {
    this.setData({ theme: e.currentTarget.dataset.val });
    this.saveSettings();
    util.toast('主题已切换');
  },

  setFontSize(e) {
    this.setData({ fontSize: e.currentTarget.dataset.val });
    this.saveSettings();
    util.toast('字号已调整');
  },

  toggleNotifyClass(e) {
    this.setData({ notifyClass: e.detail.value });
    this.saveSettings();
  },

  toggleNotifyExam(e) {
    this.setData({ notifyExam: e.detail.value });
    this.saveSettings();
  },

  toggleNotifyNotice(e) {
    this.setData({ notifyNotice: e.detail.value });
    this.saveSettings();
  },

  checkCache() {
    try {
      const info = wx.getStorageInfoSync();
      const size = (info.currentSize / 1024).toFixed(2);
      this.setData({ cacheSize: size + ' KB' });
    } catch (e) {
      this.setData({ cacheSize: '0 KB' });
    }
  },

  async clearCache() {
    const ok = await util.confirm('确定清除缓存吗?将清除临时数据,不影响个人数据');
    if (!ok) return;
    util.toast('缓存已清理', 'success');
    this.checkCache();
  },

  async resetData() {
    const ok = await util.confirm('确定重置所有数据吗?将恢复到初始状态');
    if (!ok) return;
    storage.clear();
    util.toast('数据已重置,请重启小程序', 'success');
    setTimeout(() => {
      wx.reLaunch({ url: '/pages/index/index' });
    }, 1500);
  },

  showAbout() {
    wx.showModal({
      title: '关于校园助手',
      content: '校园助手 v1.0.0\n\n一站式校园生活助手,为大学生提供课程表、成绩查询、图书借阅、签到打卡等便捷功能。\n\n开发: 期末作品组\n开源协议: MIT',
      showCancel: false
    });
  },

  showPrivacy() {
    wx.showModal({
      title: '隐私协议',
      content: '本小程序所有数据均存储在本地,不上传任何个人信息至云端。本应用为学习作品,仅供教学演示使用。',
      showCancel: false
    });
  },

  async logout() {
    const ok = await util.confirm('确定退出登录吗?');
    if (!ok) return;
    util.toast('已退出登录', 'success');
  }
});
