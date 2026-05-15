// 我的
const app = getApp();
const storage = require('../../utils/storage.js');
const util = require('../../utils/util.js');

Page({
  data: {
    studentInfo: {},
    checkinDays: 0,
    borrowCount: 0,
    gpa: '0.00'
  },

  onShow() {
    const studentInfo = app.globalData.studentInfo;
    const checkins = storage.get('checkins', []);
    const books = storage.get('books', []);
    const grades = storage.get('grades', []);

    this.setData({
      studentInfo,
      checkinDays: checkins.filter(c => c.status === 'checked').length,
      borrowCount: books.filter(b => b.status === 'borrowed').length,
      gpa: util.calcGPA(grades)
    });
  },

  navTo(e) {
    const url = e.currentTarget.dataset.url;
    const tabPages = [];
    if (tabPages.includes(url)) {
      wx.switchTab({ url });
    } else {
      wx.navigateTo({ url });
    }
  },

  showInfo(e) {
    wx.showModal({
      title: '提示',
      content: e.currentTarget.dataset.info,
      showCancel: false
    });
  }
});
