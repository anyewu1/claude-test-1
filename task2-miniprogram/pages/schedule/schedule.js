// 课程表
const app = getApp();
const storage = require('../../utils/storage.js');

const PERIODS = [
  { num: 1, label: '上午1-2节', time: '08:00-09:40' },
  { num: 2, label: '上午3-4节', time: '10:00-11:40' },
  { num: 3, label: '下午5-6节', time: '14:00-15:40' },
  { num: 4, label: '下午7-8节', time: '16:00-17:40' }
];

Page({
  data: {
    semester: '',
    weekNum: 12,
    days: ['周一', '周二', '周三', '周四', '周五'],
    grid: [],
    courseTotal: 0
  },

  onLoad() {
    this.setData({ semester: app.globalData.currentSemester });
    this.buildGrid();
  },

  buildGrid() {
    const courses = storage.get('schedule', []);
    const grid = PERIODS.map(period => {
      const cells = [];
      for (let day = 1; day <= 5; day++) {
        const course = courses.find(c => c.day === day && c.period === period.num);
        cells.push(course || null);
      }
      return { period: period.num, label: period.label, time: period.time, cells };
    });
    this.setData({ grid, courseTotal: courses.length });
  },

  onCourseClick(e) {
    const courses = storage.get('schedule', []);
    const course = courses.find(c => c.id === e.currentTarget.dataset.id);
    if (course) {
      wx.showModal({
        title: course.name,
        content: `教师: ${course.teacher}\n地点: ${course.location}\n周次: 第 ${course.weeks} 周`,
        showCancel: false
      });
    }
  }
});
