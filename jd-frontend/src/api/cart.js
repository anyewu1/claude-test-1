import http from './index'

export const cartApi = {
  getCart: () => http.get('/cart'),
  addItem: (data) => http.post('/cart/items', data),
  updateItem: (id, data) => http.put(`/cart/items/${id}`, data),
  removeItem: (id) => http.delete(`/cart/items/${id}`),
  selectAll: (selected) => http.put('/cart/select-all', { selected }),
  clearCart: () => http.delete('/cart')
}
