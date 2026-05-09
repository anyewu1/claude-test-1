import http from './index'

export const authApi = {
  login: (data) => http.post('/auth/login', data),
  register: (data) => http.post('/auth/register', data),
  getProfile: () => http.get('/auth/profile'),
  updateProfile: (data) => http.put('/auth/profile', data)
}
