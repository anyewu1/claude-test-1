<template>
  <div class="auth-page">
    <div class="auth-container">
      <div class="auth-logo" @click="$router.push('/')">
        <div class="logo-text">JD</div>
        <div class="logo-sub">多快好省 购物无忧</div>
      </div>

      <div class="auth-card">
        <h2 class="auth-title">免费注册</h2>

        <el-form :model="form" @submit.prevent="handleRegister">
          <el-form-item>
            <el-input v-model="form.username" placeholder="用户名（3-20位字母数字）" size="large" prefix-icon="User" clearable />
          </el-form-item>
          <el-form-item>
            <el-input v-model="form.email" placeholder="邮箱（选填）" size="large" prefix-icon="Message" clearable />
          </el-form-item>
          <el-form-item>
            <el-input v-model="form.phone" placeholder="手机号（选填）" size="large" prefix-icon="Phone" clearable />
          </el-form-item>
          <el-form-item>
            <el-input v-model="form.password" type="password" placeholder="密码（至少6位）" size="large" prefix-icon="Lock" show-password />
          </el-form-item>

          <el-button type="primary" size="large" class="submit-btn" :loading="loading" @click="handleRegister">
            立即注册
          </el-button>
        </el-form>

        <div class="auth-links">
          <span>已有账号？</span>
          <RouterLink to="/login">立即登录</RouterLink>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { useCartStore } from '@/stores/cartStore'

const router = useRouter()
const userStore = useUserStore()
const cartStore = useCartStore()
const loading = ref(false)
const form = ref({ username: '', email: '', phone: '', password: '' })

async function handleRegister() {
  if (!form.value.username || !form.value.password) return
  loading.value = true
  try {
    await userStore.register(form.value)
    await cartStore.fetchCart()
    router.push('/')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #e1251b 0%, #c0160d 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.auth-container {
  width: 100%;
  max-width: 420px;
}

.auth-logo {
  text-align: center;
  margin-bottom: 24px;
  cursor: pointer;
}

.logo-text {
  font-size: 52px;
  font-weight: 900;
  color: #fff;
  text-shadow: 0 2px 8px rgba(0,0,0,0.2);
}

.logo-sub {
  font-size: 14px;
  color: rgba(255,255,255,0.85);
}

.auth-card {
  background: #fff;
  border-radius: 12px;
  padding: 36px;
  box-shadow: 0 12px 40px rgba(0,0,0,0.15);
}

.auth-title {
  text-align: center;
  font-size: 22px;
  margin-bottom: 28px;
  color: #333;
}

.submit-btn {
  width: 100%;
  background: var(--jd-red);
  border-color: var(--jd-red);
  font-size: 16px;
  height: 44px;
}

.auth-links {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #666;
}

.auth-links a {
  color: var(--jd-red);
  font-weight: bold;
}
</style>
