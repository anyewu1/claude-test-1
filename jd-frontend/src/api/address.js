import http from './index'

export const addressApi = {
  list: () => http.get('/addresses'),
  add: (data) => http.post('/addresses', data),
  update: (id, data) => http.put(`/addresses/${id}`, data),
  delete: (id) => http.delete(`/addresses/${id}`),
  setDefault: (id) => http.put(`/addresses/${id}/default`)
}
