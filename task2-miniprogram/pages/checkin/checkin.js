// 签到打卡
const storage = require('../../utils/storage.js');
const util = require('../../utils/util.js');

Page({
  data: {
    today: '',
    todayKey: '',
    checkedToday: false,
    streak: 0,
    weekDays: ['日', '一', '二', '三', '四', '五', '六'],
    dates: [],
    monthCheckin: 0,
    totalCheckin: 0
  },

  onLoad() {
    const today = new Date();
    this.setData({
      today: util.formatDate(today),
      todayKey: util.formatDate(today)
    });
    this.refresh();
  },

  onShow() {
    this.refresh();
  },

  refresh() {
    const checkins = storage.get('checkins', []);
    const checkedDates = checkins.filter(c => c.status === 'checked').map(c => c.date);
    const checkedToday = checkedDates.includes(this.data.todayKey);

    // 计算连续签到
    let streak = 0;
    const today = new Date();
    for (let i = 0; i < 365; i++) {
      const d = new Date(today);
      d.setDate(d.getDate() - i);
      const key = util.formatDate(d);
      if (checkedDates.includes(key)) {
        streak++;
      } else if (i === 0 && !checkedToday) {
        continue;
      } else {
        break;
      }
    }

    this.setData({
      checkedToday,
      streak,
      totalCheckin: checkedDates.length
    });
    this.buildCalendar(checkedDates);
  },

  buildCalendar(checkedDates) {
    const today = new Date();
    const year = today.getFullYear();
    const month = today.getMonth();
    const todayKey = util.formatDate(today);

    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const startWeekDay = firstDay.getDay();
    const daysInMonth = lastDay.getDate();

    const dates = [];
    for (let i = 0; i < startWeekDay; i++) {
      dates.push({ key: `pad-${i}`, day: '', isChecked: false, isToday: false, isFuture: false });
    }

    let monthCount = 0;
    for (let day = 1; day <= daysInMonth; day++) {
      const key = `${year}-${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
      const isChecked = checkedDates.includes(key);
      if (isChecked) monthCount++;
      const cellDate = new Date(year, month, day);
      const isFuture = cellDate > today;
      dates.push({
        key, day,
        isChecked,
        isToday: key === todayKey,
        isFuture
      });
    }

    this.setData({ dates, monthCheckin: monthCount });
  },

  doCheckin() {
    if (this.data.checkedToday) {
      util.toast('今日已签到');
      return;
    }
    const checkins = storage.get('checkins', []);
    checkins.push({ date: this.data.todayKey, status: 'checked' });
    storage.set('checkins', checkins);
    util.toast('签到成功!坚持每天签到~', 'success');
    this.refresh();
  }
});
