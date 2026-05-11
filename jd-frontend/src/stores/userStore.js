import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '@/api/auth'
import { ElMessage } from 'element-plus'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('jd_token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('jd_user') || 'null'))

  const isLoggedIn = computed(() => !!token.value)
  const username = computed(() => userInfo.value?.username || '')
  const avatar = computed(() => userInfo.value?.avatar || '')
  const isAdmin = computed(() => userInfo.value?.role === 'ADMIN')

  async function login(data) {
    const res = await authApi.login(data)
    token.value = res.data.token
    userInfo.value = res.data
    localStorage.setItem('jd_token', res.data.token)
    localStorage.setItem('jd_user', JSON.stringify(res.data))
    ElMessage.success('登录成功')
  }

  async function register(data) {
    const res = await authApi.register(data)
    token.value = res.data.token
    userInfo.value = res.data
    localStorage.setItem('jd_token', res.data.token)
    localStorage.setItem('jd_user', JSON.stringify(res.data))
    ElMessage.success('注册成功')
  }

  function logout() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('jd_token')
    localStorage.removeItem('jd_user')
    ElMessage.success('已退出登录')
  }

  async function fetchProfile() {
    if (!isLoggedIn.value) return
    const res = await authApi.getProfile()
    userInfo.value = { ...userInfo.value, ...res.data }
    localStorage.setItem('jd_user', JSON.stringify(userInfo.value))
  }

  return { token, userInfo, isLoggedIn, isAdmin, username, avatar, login, register, logout, fetchProfile }
})
