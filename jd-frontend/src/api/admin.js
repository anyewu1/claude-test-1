import http from './index'

export const adminApi = {
  // Products
  listProducts: (params) => http.get('/admin/products', { params }),
  createProduct: (data) => http.post('/admin/products', data),
  updateProduct: (id, data) => http.put(`/admin/products/${id}`, data),
  deleteProduct: (id) => http.delete(`/admin/products/${id}`),
  // Orders
  listOrders: (params) => http.get('/admin/orders', { params }),
  shipOrder: (id) => http.put(`/admin/orders/${id}/ship`),
  // Users
  listUsers: (params) => http.get('/admin/users', { params }),
  // Coupons
  listCoupons: () => http.get('/admin/coupons'),
  createCoupon: (data) => http.post('/admin/coupons', data),
  deleteCoupon: (id) => http.delete(`/admin/coupons/${id}`),
  // Embeddings
  rebuildEmbeddings: () => http.post('/admin/embeddings/rebuild')
}
