// app.js - 校园助手小程序入口
const mockData = require('./utils/mock-data.js');
const storage = require('./utils/storage.js');

App({
  globalData: {
    userInfo: null,
    currentSemester: '2025-2026 学年第二学期',
    studentInfo: {
      name: '李同学',
      studentId: '20230101001',
      class: '计算机科学 2301',
      college: '信息工程学院',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=student'
    }
  },

  onLaunch() {
    // 首次启动注入示例数据
    if (!storage.get('initialized')) {
      storage.set('schedule', mockData.schedule);
      storage.set('grades', mockData.grades);
      storage.set('books', mockData.books);
      storage.set('notices', mockData.notices);
      storage.set('checkins', mockData.checkins);
      storage.set('todos', mockData.todos);
      storage.set('events', mockData.events);
      storage.set('settings', { theme: 'light', fontSize: 'medium' });
      storage.set('initialized', true);
      console.log('[App] 初始数据已注入');
    }
    console.log('[App] 校园助手启动');
  },

  onShow() {
    console.log('[App] 小程序显示');
  },

  onHide() {
    console.log('[App] 小程序隐藏');
  }
});
