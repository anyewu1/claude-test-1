import http from './index'

export const orderApi = {
  create: (data) => http.post('/orders', data),
  list: (params) => http.get('/orders', { params }),
  detail: (id) => http.get(`/orders/${id}`),
  cancel: (id) => http.put(`/orders/${id}/cancel`),
  pay: (id) => http.put(`/orders/${id}/pay`),
  confirm: (id) => http.put(`/orders/${id}/confirm`),
  refund: (id) => http.put(`/orders/${id}/refund`)
}
