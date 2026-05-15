// 图书详情
const storage = require('../../utils/storage.js');
const util = require('../../utils/util.js');

Page({
  data: {
    book: null
  },

  onLoad(options) {
    const books = storage.get('books', []);
    const book = books.find(b => b.id === options.id);
    this.setData({ book });
  },

  async onRenew() {
    const confirmed = await util.confirm('确定续借此图书吗?续借后应还日期延后 30 天');
    if (!confirmed) return;
    const books = storage.get('books', []);
    const book = books.find(b => b.id === this.data.book.id);
    if (book) {
      const d = new Date(book.returnDate);
      d.setDate(d.getDate() + 30);
      book.returnDate = util.formatDate(d);
      storage.set('books', books);
      this.setData({ book });
      util.toast('续借成功', 'success');
    }
  },

  async onReturn() {
    const confirmed = await util.confirm('确定归还此图书吗?');
    if (!confirmed) return;
    const books = storage.get('books', []);
    const book = books.find(b => b.id === this.data.book.id);
    if (book) {
      book.status = 'returned';
      storage.set('books', books);
      this.setData({ book });
      util.toast('归还成功', 'success');
      setTimeout(() => wx.navigateBack(), 1000);
    }
  }
});
