import http from './index'

export const aiApi = {
  chat: (data) => http.post('/ai/chat', data),
  suggest: (keyword, limit = 5) => http.get('/ai/suggest', { params: { keyword, limit } }),
  recommend: (data) => http.post('/ai/recommend', data)
}
