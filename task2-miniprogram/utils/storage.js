// storage.js - wx.storage 封装

module.exports = {
  get(key, defaultValue = null) {
    try {
      const value = wx.getStorageSync(key);
      return value === '' ? defaultValue : value;
    } catch (e) {
      console.error('[storage.get]', key, e);
      return defaultValue;
    }
  },

  set(key, value) {
    try {
      wx.setStorageSync(key, value);
      return true;
    } catch (e) {
      console.error('[storage.set]', key, e);
      return false;
    }
  },

  remove(key) {
    try {
      wx.removeStorageSync(key);
      return true;
    } catch (e) {
      console.error('[storage.remove]', key, e);
      return false;
    }
  },

  clear() {
    try {
      wx.clearStorageSync();
      return true;
    } catch (e) {
      return false;
    }
  }
};
