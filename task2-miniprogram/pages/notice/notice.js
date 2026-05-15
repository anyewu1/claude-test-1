// 通知公告
const storage = require('../../utils/storage.js');

Page({
  data: {
    notices: [],
    filteredNotices: [],
    filter: 'all',
    unreadCount: 0
  },

  onLoad() {
    this.refresh();
  },

  onShow() {
    this.refresh();
  },

  refresh() {
    const notices = storage.get('notices', []);
    const unreadCount = notices.filter(n => !n.read).length;
    this.setData({ notices, unreadCount });
    this.applyFilter();
  },

  applyFilter() {
    let result = this.data.notices;
    if (this.data.filter === 'unread') {
      result = result.filter(n => !n.read);
    } else if (this.data.filter === 'important') {
      result = result.filter(n => n.type === 'important');
    }
    this.setData({ filteredNotices: result });
  },

  setFilter(e) {
    this.setData({ filter: e.currentTarget.dataset.val });
    this.applyFilter();
  },

  viewNotice(e) {
    const id = e.currentTarget.dataset.id;
    const notices = storage.get('notices', []);
    const notice = notices.find(n => n.id === id);
    if (notice) {
      notice.read = true;
      storage.set('notices', notices);
      wx.showModal({
        title: notice.title,
        content: notice.content + '\n\n— ' + notice.author + ' · ' + notice.date,
        showCancel: false,
        success: () => this.refresh()
      });
    }
  }
});
