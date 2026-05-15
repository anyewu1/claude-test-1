// 日程/校历
const storage = require('../../utils/storage.js');
const util = require('../../utils/util.js');

Page({
  data: {
    year: 2026,
    month: 5,
    weekDays: ['日', '一', '二', '三', '四', '五', '六'],
    dates: [],
    selectedDate: '',
    selectedEvents: [],
    allEvents: []
  },

  onLoad() {
    const today = new Date();
    this.setData({
      year: today.getFullYear(),
      month: today.getMonth() + 1,
      selectedDate: util.formatDate(today)
    });
    this.loadEvents();
    this.buildDates();
  },

  loadEvents() {
    const events = storage.get('events', []);
    this.setData({ allEvents: events });
  },

  buildDates() {
    const { year, month, allEvents } = this.data;
    const firstDay = new Date(year, month - 1, 1);
    const lastDay = new Date(year, month, 0);
    const startWeekDay = firstDay.getDay();
    const daysInMonth = lastDay.getDate();
    const todayKey = util.formatDate(new Date());

    const dates = [];

    // 前面补空格 (上月日期)
    const prevLastDay = new Date(year, month - 1, 0).getDate();
    for (let i = startWeekDay - 1; i >= 0; i--) {
      const day = prevLastDay - i;
      const m = month === 1 ? 12 : month - 1;
      const y = month === 1 ? year - 1 : year;
      const key = `${y}-${String(m).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
      dates.push({ key, day, isCurrentMonth: false, isToday: false, hasEvent: false, isSelected: false });
    }

    // 当月
    for (let day = 1; day <= daysInMonth; day++) {
      const key = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
      const hasEvent = allEvents.some(e => e.date === key);
      dates.push({
        key, day,
        isCurrentMonth: true,
        isToday: key === todayKey,
        hasEvent,
        isSelected: key === this.data.selectedDate
      });
    }

    // 后面补空格 (下月日期),凑齐 6 行 = 42 格
    let nextDay = 1;
    while (dates.length < 42) {
      const m = month === 12 ? 1 : month + 1;
      const y = month === 12 ? year + 1 : year;
      const key = `${y}-${String(m).padStart(2, '0')}-${String(nextDay).padStart(2, '0')}`;
      dates.push({ key, day: nextDay, isCurrentMonth: false, isToday: false, hasEvent: false, isSelected: false });
      nextDay++;
    }

    this.setData({ dates });
    this.updateSelectedEvents();
  },

  updateSelectedEvents() {
    const events = this.data.allEvents.filter(e => e.date === this.data.selectedDate);
    this.setData({ selectedEvents: events });
  },

  selectDate(e) {
    const key = e.currentTarget.dataset.key;
    const dates = this.data.dates.map(d => ({ ...d, isSelected: d.key === key }));
    this.setData({ dates, selectedDate: key });
    this.updateSelectedEvents();
  },

  prevMonth() {
    let { year, month } = this.data;
    month--;
    if (month < 1) { month = 12; year--; }
    this.setData({ year, month });
    this.buildDates();
  },

  nextMonth() {
    let { year, month } = this.data;
    month++;
    if (month > 12) { month = 1; year++; }
    this.setData({ year, month });
    this.buildDates();
  }
});
