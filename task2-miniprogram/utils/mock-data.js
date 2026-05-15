// mock-data.js - 初始示例数据

module.exports = {
  // 课程表 (周一到周五, 每天 4 大节)
  schedule: [
    { id: '1', day: 1, period: 1, name: '高等数学', teacher: '王老师', location: '教1-301', weeks: '1-16', color: '#FF6B6B' },
    { id: '2', day: 1, period: 2, name: '大学英语', teacher: '李老师', location: '教2-205', weeks: '1-16', color: '#4ECDC4' },
    { id: '3', day: 1, period: 4, name: '体育', teacher: '张教练', location: '操场', weeks: '1-12', color: '#95E1D3' },
    { id: '4', day: 2, period: 1, name: '数据结构', teacher: '陈老师', location: '实验楼-A302', weeks: '1-16', color: '#A8E6CF' },
    { id: '5', day: 2, period: 2, name: '数据结构实验', teacher: '陈老师', location: '机房-3', weeks: '2-16', color: '#A8E6CF' },
    { id: '6', day: 2, period: 3, name: '线性代数', teacher: '赵老师', location: '教1-405', weeks: '1-16', color: '#FFD93D' },
    { id: '7', day: 3, period: 1, name: '操作系统', teacher: '刘老师', location: '教3-201', weeks: '1-16', color: '#6C5CE7' },
    { id: '8', day: 3, period: 2, name: '计算机网络', teacher: '孙老师', location: '教3-202', weeks: '1-16', color: '#FD79A8' },
    { id: '9', day: 3, period: 4, name: '马克思主义原理', teacher: '周老师', location: '教1-101', weeks: '1-16', color: '#E17055' },
    { id: '10', day: 4, period: 1, name: '高等数学', teacher: '王老师', location: '教1-301', weeks: '1-16', color: '#FF6B6B' },
    { id: '11', day: 4, period: 2, name: '数据库原理', teacher: '吴老师', location: '实验楼-A301', weeks: '1-16', color: '#74B9FF' },
    { id: '12', day: 4, period: 3, name: '软件工程', teacher: '郑老师', location: '教2-301', weeks: '1-16', color: '#A29BFE' },
    { id: '13', day: 5, period: 1, name: '大学英语', teacher: '李老师', location: '教2-205', weeks: '1-16', color: '#4ECDC4' },
    { id: '14', day: 5, period: 2, name: '计算机组成原理', teacher: '黄老师', location: '教3-405', weeks: '1-16', color: '#FAB1A0' },
    { id: '15', day: 5, period: 3, name: '形势与政策', teacher: '何老师', location: '教1-201', weeks: '1-8', color: '#FF7675' }
  ],

  // 成绩
  grades: [
    { id: '1', name: '高等数学(上)', credit: 5, score: 92, semester: '2024-2025-1', type: '必修' },
    { id: '2', name: '大学英语(一)', credit: 3, score: 87, semester: '2024-2025-1', type: '必修' },
    { id: '3', name: '程序设计基础', credit: 4, score: 95, semester: '2024-2025-1', type: '必修' },
    { id: '4', name: '思想道德与法治', credit: 2, score: 89, semester: '2024-2025-1', type: '必修' },
    { id: '5', name: '体育(一)', credit: 1, score: 85, semester: '2024-2025-1', type: '必修' },
    { id: '6', name: '军事理论', credit: 2, score: 90, semester: '2024-2025-1', type: '必修' },
    { id: '7', name: '高等数学(下)', credit: 5, score: 88, semester: '2024-2025-2', type: '必修' },
    { id: '8', name: '大学英语(二)', credit: 3, score: 91, semester: '2024-2025-2', type: '必修' },
    { id: '9', name: '数据结构', credit: 4, score: 93, semester: '2024-2025-2', type: '必修' },
    { id: '10', name: '线性代数', credit: 3, score: 86, semester: '2024-2025-2', type: '必修' },
    { id: '11', name: '中国近现代史纲要', credit: 2, score: 88, semester: '2024-2025-2', type: '必修' },
    { id: '12', name: '体育(二)', credit: 1, score: 90, semester: '2024-2025-2', type: '必修' }
  ],

  // 图书
  books: [
    {
      id: '1', title: '深入理解计算机系统', author: 'Randal E.Bryant',
      publisher: '机械工业出版社', isbn: '9787111544937',
      borrowDate: '2026-04-15', returnDate: '2026-06-15',
      status: 'borrowed', cover: '📕',
      summary: '从程序员的视角介绍计算机系统的基本概念,包括数据的机器表示、汇编、内存层次结构、链接、异常控制流、虚拟内存等。'
    },
    {
      id: '2', title: '算法导论', author: 'Thomas H.Cormen',
      publisher: '机械工业出版社', isbn: '9787111407010',
      borrowDate: '2026-04-20', returnDate: '2026-06-20',
      status: 'borrowed', cover: '📗',
      summary: '算法领域经典著作,深入浅出地介绍了各种算法,包含丰富的练习与思考题。'
    },
    {
      id: '3', title: '人类简史', author: '尤瓦尔·赫拉利',
      publisher: '中信出版社', isbn: '9787508647357',
      borrowDate: '2026-05-01', returnDate: '2026-07-01',
      status: 'borrowed', cover: '📘',
      summary: '从十万年前有生命迹象开始到 21 世纪资本、科技交织的人类发展史。'
    },
    {
      id: '4', title: '红楼梦', author: '曹雪芹',
      publisher: '人民文学出版社', isbn: '9787020002207',
      borrowDate: '2026-03-10', returnDate: '2026-05-10',
      status: 'returned', cover: '📙',
      summary: '中国古典四大名著之一,描写贾、史、王、薛四大家族的兴衰。'
    },
    {
      id: '5', title: 'JavaScript 高级程序设计', author: 'Matt Frisbie',
      publisher: '人民邮电出版社', isbn: '9787115545381',
      borrowDate: '2026-05-05', returnDate: '2026-07-05',
      status: 'borrowed', cover: '📓',
      summary: 'JavaScript 经典教程,全面涵盖 ECMAScript 6 及更新版本特性。'
    },
    {
      id: '6', title: '设计模式', author: 'Erich Gamma',
      publisher: '机械工业出版社', isbn: '9787111211266',
      borrowDate: '2026-04-25', returnDate: '2026-06-25',
      status: 'borrowed', cover: '📔',
      summary: 'GoF 经典著作,介绍 23 种设计模式的应用场景与实现。'
    }
  ],

  // 通知公告
  notices: [
    {
      id: '1', title: '关于2025-2026学年第二学期期末考试安排的通知',
      content: '各位同学,本学期期末考试将于6月15日至6月30日进行,具体科目时间表请查看附件。请同学们认真复习,诚信应考。',
      author: '教务处', date: '2026-05-10', read: false, type: 'important'
    },
    {
      id: '2', title: '校园马拉松报名通知',
      content: '为丰富校园文化生活,学校将于5月25日举办首届校园马拉松活动,欢迎全校师生踊跃报名参加。报名截止日期: 5月20日。',
      author: '体育部', date: '2026-05-08', read: false, type: 'activity'
    },
    {
      id: '3', title: '图书馆五一假期开馆通知',
      content: '五一假期(5月1日-5月5日)期间,图书馆将正常开馆,开馆时间为 8:00-22:00。',
      author: '图书馆', date: '2026-04-28', read: true, type: 'normal'
    },
    {
      id: '4', title: '关于评选优秀学生奖学金的通知',
      content: '本学年优秀学生奖学金评选工作即将启动,请符合条件的同学准备申请材料,详见附件评选办法。',
      author: '学工处', date: '2026-04-25', read: true, type: 'important'
    },
    {
      id: '5', title: '校园网络维护通知',
      content: '为提升网络服务质量,信息中心将于5月15日凌晨2:00-5:00进行网络系统维护,届时校园网将暂停服务。',
      author: '信息中心', date: '2026-04-20', read: true, type: 'normal'
    },
    {
      id: '6', title: '2026届毕业生招聘双选会',
      content: '5月20日(周三)9:00-16:00,体育馆将举办2026届毕业生招聘双选会,届时将有近百家企业到场招聘。',
      author: '就业指导中心', date: '2026-04-15', read: true, type: 'activity'
    }
  ],

  // 签到记录
  checkins: [
    { date: '2026-05-13', status: 'checked' },
    { date: '2026-05-12', status: 'checked' },
    { date: '2026-05-11', status: 'checked' },
    { date: '2026-05-10', status: 'checked' },
    { date: '2026-05-09', status: 'checked' },
    { date: '2026-05-08', status: 'checked' },
    { date: '2026-05-07', status: 'checked' }
  ],

  // 待办
  todos: [
    { id: '1', text: '完成数据结构第五章作业', done: false, priority: 'high', dueDate: '2026-05-16' },
    { id: '2', text: '复习高等数学期中考点', done: false, priority: 'high', dueDate: '2026-05-18' },
    { id: '3', text: '准备英语口语演讲', done: false, priority: 'medium', dueDate: '2026-05-20' },
    { id: '4', text: '归还图书馆借书', done: true, priority: 'low', dueDate: '2026-05-10' },
    { id: '5', text: '提交奖学金申请材料', done: false, priority: 'medium', dueDate: '2026-05-25' }
  ],

  // 校园日程/事件
  events: [
    { id: '1', date: '2026-05-15', title: '今日: 形势与政策考试', type: 'exam' },
    { id: '2', date: '2026-05-20', title: '英语口语展示', type: 'class' },
    { id: '3', date: '2026-05-25', title: '校园马拉松', type: 'activity' },
    { id: '4', date: '2026-06-01', title: '期末复习周开始', type: 'important' },
    { id: '5', date: '2026-06-15', title: '期末考试开始', type: 'exam' }
  ]
};
