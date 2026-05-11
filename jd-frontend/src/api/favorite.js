import http from './index'

export const favoriteApi = {
  list: () => http.get('/favorites'),
  add: (productId) => http.post(`/favorites/${productId}`),
  remove: (productId) => http.delete(`/favorites/${productId}`),
  check: (productId) => http.get(`/favorites/check/${productId}`)
}
