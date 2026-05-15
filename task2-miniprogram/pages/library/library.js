// 图书馆
const storage = require('../../utils/storage.js');

Page({
  data: {
    books: [],
    filteredBooks: [],
    keyword: '',
    filter: 'all',
    stats: { total: 0, borrowed: 0, returned: 0 }
  },

  onLoad() {
    this.loadBooks();
  },

  onShow() {
    this.loadBooks();
  },

  loadBooks() {
    const books = storage.get('books', []);
    const stats = {
      total: books.length,
      borrowed: books.filter(b => b.status === 'borrowed').length,
      returned: books.filter(b => b.status === 'returned').length
    };
    this.setData({ books, stats });
    this.applyFilter();
  },

  applyFilter() {
    let result = this.data.books;
    if (this.data.filter !== 'all') {
      result = result.filter(b => b.status === this.data.filter);
    }
    if (this.data.keyword) {
      const kw = this.data.keyword.toLowerCase();
      result = result.filter(b =>
        b.title.toLowerCase().includes(kw) || b.author.toLowerCase().includes(kw)
      );
    }
    this.setData({ filteredBooks: result });
  },

  onSearchInput(e) {
    this.setData({ keyword: e.detail.value });
    this.applyFilter();
  },

  setFilter(e) {
    this.setData({ filter: e.currentTarget.dataset.val });
    this.applyFilter();
  },

  viewDetail(e) {
    wx.navigateTo({
      url: '/pages/book-detail/book-detail?id=' + e.currentTarget.dataset.id
    });
  }
});
