import axios from 'axios'
import { ElMessage } from 'element-plus'

const http = axios.create({
  baseURL: '/api',
  timeout: 60000
})

http.interceptors.request.use(config => {
  const token = localStorage.getItem('jd_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

http.interceptors.response.use(
  res => {
    const data = res.data
    if (data.code === 200) return data
    ElMessage.error(data.message || '请求失败')
    return Promise.reject(new Error(data.message))
  },
  err => {
    if (err.response?.status === 401) {
      localStorage.removeItem('jd_token')
      localStorage.removeItem('jd_user')
      window.location.href = '/login'
    } else {
      ElMessage.error(err.response?.data?.message || '网络错误，请稍后重试')
    }
    return Promise.reject(err)
  }
)

export default http
