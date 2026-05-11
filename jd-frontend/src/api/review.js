import http from './index'

export const reviewApi = {
  listByProduct: (productId, params) => http.get(`/reviews/product/${productId}`, { params }),
  addReview: (productId, data) => http.post(`/reviews/product/${productId}`, data)
}
