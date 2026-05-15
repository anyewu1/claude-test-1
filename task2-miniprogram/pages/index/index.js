// 首页
const app = getApp();
const storage = require('../../utils/storage.js');
const util = require('../../utils/util.js');

Page({
  data: {
    studentInfo: {},
    today: '',
    weekDay: '',
    weekNum: 12,
    todayCourses: [],
    todayCourseCount: 0,
    todoCount: 0,
    unreadCount: 0,
    borrowCount: 0,
    pendingTodos: []
  },

  onLoad() {
    this.setData({ studentInfo: app.globalData.studentInfo });
    this.refreshData();
  },

  onShow() {
    this.refreshData();
  },

  refreshData() {
    const today = new Date();
    const weekDays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
    const dayOfWeek = today.getDay() || 7; // 周日为 7

    const schedule = storage.get('schedule', []);
    const todos = storage.get('todos', []);
    const notices = storage.get('notices', []);
    const books = storage.get('books', []);

    const todayCourses = schedule
      .filter(c => c.day === dayOfWeek)
      .sort((a, b) => a.period - b.period);

    const pendingTodos = todos.filter(t => !t.done).slice(0, 3);

    this.setData({
      today: util.formatDate(today),
      weekDay: weekDays[today.getDay()],
      todayCourses,
      todayCourseCount: todayCourses.length,
      todoCount: todos.filter(t => !t.done).length,
      unreadCount: notices.filter(n => !n.read).length,
      borrowCount: books.filter(b => b.status === 'borrowed').length,
      pendingTodos
    });
  },

  navToGrades() {
    wx.navigateTo({ url: '/pages/grades/grades' });
  },

  navToLibrary() {
    wx.navigateTo({ url: '/pages/library/library' });
  },

  navToCalendar() {
    wx.navigateTo({ url: '/pages/calendar/calendar' });
  },

  navToCheckin() {
    wx.navigateTo({ url: '/pages/checkin/checkin' });
  },

  navToSchedule() {
    wx.switchTab({ url: '/pages/schedule/schedule' });
  }
});
