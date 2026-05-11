import http from './index'

export const productApi = {
  list: (params) => http.get('/products', { params }),
  search: (params) => http.get('/products/search', { params }),
  detail: (id) => http.get(`/products/${id}`),
  flashSale: () => http.get('/products/flash-sale'),
  recommend: (params) => http.get('/products/recommend', { params }),
  categories: () => http.get('/categories'),
  topCategories: () => http.get('/categories/top')
}
