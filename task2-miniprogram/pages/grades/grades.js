// 成绩查询
const storage = require('../../utils/storage.js');
const util = require('../../utils/util.js');

Page({
  data: {
    semesters: [],
    activeIdx: 0,
    filteredGrades: [],
    gpa: '0.00',
    avgScore: 0,
    totalCredit: 0
  },

  onLoad() {
    const grades = storage.get('grades', []);
    const semesters = [...new Set(grades.map(g => g.semester))].sort().reverse();
    this.setData({ semesters });
    this.selectSemester({ currentTarget: { dataset: { idx: 0 } } });
  },

  selectSemester(e) {
    const idx = parseInt(e.currentTarget.dataset.idx);
    const grades = storage.get('grades', []);
    const semester = this.data.semesters[idx];
    const filtered = grades.filter(g => g.semester === semester);

    const gpa = util.calcGPA(filtered);
    const totalCredit = filtered.reduce((s, g) => s + g.credit, 0);
    const avgScore = filtered.length === 0 ? 0 :
      (filtered.reduce((s, g) => s + g.score, 0) / filtered.length).toFixed(1);

    this.setData({
      activeIdx: idx,
      filteredGrades: filtered,
      gpa, avgScore, totalCredit
    });
  }
});
