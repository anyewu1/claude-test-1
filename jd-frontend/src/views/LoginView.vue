<template>
  <div class="auth-page">
    <div class="auth-container">
      <div class="auth-logo" @click="$router.push('/')">
        <div class="logo-text">JD</div>
        <div class="logo-sub">多快好省 购物无忧</div>
      </div>

      <div class="auth-card">
        <h2 class="auth-title">账号登录</h2>

        <el-form :model="form" @submit.prevent="handleLogin">
          <el-form-item>
            <el-input
              v-model="form.username"
              placeholder="请输入用户名"
              size="large"
              prefix-icon="User"
              clearable
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              size="large"
              prefix-icon="Lock"
              show-password
              @keyup.enter="handleLogin"
            />
          </el-form-item>

          <div class="demo-tip">
            <el-tag>演示账号</el-tag>
            用户名：<b>demo</b>，密码：<b>123456</b>
          </div>

          <el-button
            type="primary"
            size="large"
            class="submit-btn"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form>

        <div class="auth-links">
          <span>还没有账号？</span>
          <RouterLink to="/register">立即注册</RouterLink>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { useCartStore } from '@/stores/cartStore'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const cartStore = useCartStore()
const loading = ref(false)
const form = ref({ username: '', password: '' })

async function handleLogin() {
  if (!form.value.username || !form.value.password) return
  loading.value = true
  try {
    await userStore.login(form.value)
    await cartStore.fetchCart()
    router.push(route.query.redirect || '/')
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

.demo-tip {
  background: #fff8e6;
  border: 1px solid #ffe58f;
  border-radius: 6px;
  padding: 10px 14px;
  font-size: 13px;
  color: #ad6800;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
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
