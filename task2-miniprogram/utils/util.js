// util.js - 通用工具函数

const formatNumber = n => {
  n = n.toString();
  return n[1] ? n : '0' + n;
};

module.exports = {
  // 日期格式化
  formatDate(date, fmt = 'YYYY-MM-DD') {
    const d = typeof date === 'string' ? new Date(date) : date;
    const o = {
      'YYYY': d.getFullYear(),
      'MM': formatNumber(d.getMonth() + 1),
      'DD': formatNumber(d.getDate()),
      'HH': formatNumber(d.getHours()),
      'mm': formatNumber(d.getMinutes()),
      'ss': formatNumber(d.getSeconds())
    };
    return fmt.replace(/YYYY|MM|DD|HH|mm|ss/g, k => o[k]);
  },

  // 相对时间
  timeAgo(date) {
    const diff = (new Date() - new Date(date)) / 1000;
    if (diff < 60) return '刚刚';
    if (diff < 3600) return Math.floor(diff / 60) + '分钟前';
    if (diff < 86400) return Math.floor(diff / 3600) + '小时前';
    if (diff < 2592000) return Math.floor(diff / 86400) + '天前';
    return this.formatDate(date);
  },

  // 生成 ID
  uuid() {
    return Date.now().toString(36) + Math.random().toString(36).slice(2, 8);
  },

  // GPA 计算
  calcGPA(grades) {
    if (!grades || grades.length === 0) return 0;
    let total = 0;
    let credits = 0;
    grades.forEach(g => {
      const point = this.scoreToPoint(g.score);
      total += point * g.credit;
      credits += g.credit;
    });
    return credits === 0 ? 0 : (total / credits).toFixed(2);
  },

  scoreToPoint(score) {
    if (score >= 90) return 4.0;
    if (score >= 85) return 3.7;
    if (score >= 82) return 3.3;
    if (score >= 78) return 3.0;
    if (score >= 75) return 2.7;
    if (score >= 72) return 2.3;
    if (score >= 68) return 2.0;
    if (score >= 64) return 1.5;
    if (score >= 60) return 1.0;
    return 0;
  },

  // 显示 Toast
  toast(title, icon = 'none', duration = 1500) {
    wx.showToast({ title, icon, duration });
  },

  // 显示确认对话框
  confirm(content, title = '提示') {
    return new Promise((resolve) => {
      wx.showModal({
        title,
        content,
        success: res => resolve(res.confirm)
      });
    });
  }
};
