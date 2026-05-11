import http from './index'

export const historyApi = {
  record: (productId) => http.post(`/history/${productId}`),
  recent: (limit = 10) => http.get('/history', { params: { limit } })
}
