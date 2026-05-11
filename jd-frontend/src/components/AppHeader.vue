<template>
  <header class="app-header">
    <!-- Top bar -->
    <div class="header-top">
      <div class="page-container top-bar">
        <span class="top-text">京东欢迎您！</span>
        <div class="top-actions">
          <template v-if="userStore.isLoggedIn">
            <span class="top-text">{{ userStore.username }}</span>
            <RouterLink to="/orders">我的订单</RouterLink>
            <RouterLink to="/favorites">我的收藏</RouterLink>
            <RouterLink to="/profile">会员中心</RouterLink>
            <RouterLink v-if="userStore.isAdmin" to="/admin" class="admin-link">管理后台</RouterLink>
            <a href="#" @click.prevent="handleLogout">退出</a>
          </template>
          <template v-else>
            <RouterLink to="/login">请登录</RouterLink>
            <RouterLink to="/register">免费注册</RouterLink>
          </template>
          <RouterLink to="/cart" class="cart-link">
            <el-badge :value="cartStore.cartCount || 0" :hidden="cartStore.cartCount === 0" :max="99">
              购物车
            </el-badge>
          </RouterLink>
        </div>
      </div>
    </div>

    <!-- Main header -->
    <div class="header-main">
      <div class="page-container main-bar">
        <RouterLink to="/" class="logo">
          <div class="logo-text">JD</div>
          <div class="logo-sub">多快好省</div>
        </RouterLink>

        <div class="search-bar">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索商品、品牌、分类"
            class="search-input"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #suffix>
              <el-button type="primary" @click="handleSearch" class="search-btn">
                <el-icon><Search /></el-icon>
              </el-button>
            </template>
          </el-input>
          <div v-if="suggestions.length" class="search-suggestions">
            <div
              v-for="s in suggestions"
              :key="s"
              class="suggestion-item"
              @click="selectSuggestion(s)"
            >{{ s }}</div>
          </div>
        </div>

        <RouterLink to="/cart" class="cart-btn">
          <el-badge :value="cartStore.cartCount" :hidden="cartStore.cartCount === 0" :max="99">
            <el-button>
              <el-icon><ShoppingCart /></el-icon>
              购物车
            </el-button>
          </el-badge>
        </RouterLink>
      </div>
    </div>

    <!-- Category nav -->
    <div class="header-nav">
      <div class="page-container nav-bar">
        <div class="all-categories" @mouseenter="showCategories = true" @mouseleave="showCategories = false">
          <el-icon><Menu /></el-icon> 全部商品分类
          <div v-if="showCategories" class="categories-panel">
            <div
              v-for="cat in categories"
              :key="cat.id"
              class="cat-item"
              @click="goCategory(cat.id)"
            >
              <span class="cat-icon">{{ cat.icon }}</span>
              <span class="cat-name">{{ cat.name }}</span>
              <span class="cat-children" v-if="cat.children">
                {{ cat.children.slice(0,3).map(c => c.name).join(' ') }}
              </span>
            </div>
          </div>
        </div>
        <nav class="quick-nav">
          <RouterLink to="/">首页</RouterLink>
          <RouterLink to="/products?categoryId=1">手机数码</RouterLink>
          <RouterLink to="/products?categoryId=2">电脑办公</RouterLink>
          <RouterLink to="/products?categoryId=3">家用电器</RouterLink>
          <RouterLink to="/products?categoryId=5">食品生鲜</RouterLink>
          <RouterLink to="/products?categoryId=6">美妆护肤</RouterLink>
          <a href="#" class="flash-link">⚡ 限时闪购</a>
        </nav>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { useCartStore } from '@/stores/cartStore'
import { productApi } from '@/api/product'
import { aiApi } from '@/api/ai'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const cartStore = useCartStore()

const searchKeyword = ref('')
const suggestions = ref([])
const showCategories = ref(false)
const categories = ref([])

onMounted(async () => {
  try {
    const res = await productApi.categories()
    categories.value = res.data
  } catch {}
  if (userStore.isLoggedIn) {
    cartStore.fetchCart()
  }
})

let suggestTimer = null
watch(searchKeyword, (val) => {
  clearTimeout(suggestTimer)
  if (!val.trim()) {
    suggestions.value = []
    return
  }
  suggestTimer = setTimeout(async () => {
    try {
      const res = await aiApi.suggest(val, 5)
      suggestions.value = res.data
    } catch {}
  }, 300)
})

function handleSearch() {
  suggestions.value = []
  if (searchKeyword.value.trim()) {
    router.push({ path: '/search', query: { keyword: searchKeyword.value } })
  }
}

function selectSuggestion(s) {
  searchKeyword.value = s
  suggestions.value = []
  handleSearch()
}

function goCategory(id) {
  showCategories.value = false
  router.push({ path: '/products', query: { categoryId: id } })
}

function handleLogout() {
  userStore.logout()
  router.push('/')
}
</script>

<style scoped>
.app-header {
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.header-top {
  background: #f5f5f5;
  border-bottom: 1px solid #e5e5e5;
  font-size: 12px;
}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 30px;
}

.top-text {
  color: #999;
}

.top-actions {
  display: flex;
  gap: 12px;
  align-items: center;
}

.top-actions a {
  color: #666;
  font-size: 12px;
}

.top-actions a:hover {
  color: var(--jd-red);
}

.admin-link {
  color: var(--jd-red) !important;
  font-weight: bold;
}

.header-main {
  background: #fff;
  padding: 12px 0;
}

.main-bar {
  display: flex;
  align-items: center;
  gap: 20px;
}

.logo {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-decoration: none;
  min-width: 80px;
}

.logo-text {
  font-size: 36px;
  font-weight: 900;
  color: var(--jd-red);
  line-height: 1;
  letter-spacing: 2px;
}

.logo-sub {
  font-size: 11px;
  color: #999;
  letter-spacing: 1px;
}

.search-bar {
  flex: 1;
  position: relative;
  max-width: 680px;
}

.search-input {
  width: 100%;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 4px 0 0 4px;
  box-shadow: 0 0 0 1px var(--jd-red) inset;
}

.search-btn {
  background: var(--jd-red);
  border: none;
  border-radius: 0 4px 4px 0;
  padding: 8px 20px;
  height: 38px;
}

.search-suggestions {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: #fff;
  border: 1px solid #e5e5e5;
  border-top: none;
  z-index: 200;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.suggestion-item {
  padding: 8px 16px;
  cursor: pointer;
  font-size: 14px;
}

.suggestion-item:hover {
  background: #f5f5f5;
  color: var(--jd-red);
}

.cart-btn {
  text-decoration: none;
}

.header-nav {
  background: var(--jd-red);
}

.nav-bar {
  display: flex;
  align-items: center;
  height: 36px;
}

.all-categories {
  position: relative;
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 0 16px;
  color: #fff;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
  background: rgba(0,0,0,0.2);
  height: 100%;
}

.categories-panel {
  position: absolute;
  top: 100%;
  left: 0;
  width: 240px;
  background: #fff;
  border: 1px solid #e5e5e5;
  box-shadow: 4px 0 12px rgba(0,0,0,0.1);
  z-index: 200;
  max-height: 480px;
  overflow-y: auto;
}

.cat-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  cursor: pointer;
  border-bottom: 1px solid #f5f5f5;
  transition: background 0.15s;
}

.cat-item:hover {
  background: #fff5f5;
}

.cat-icon {
  font-size: 18px;
}

.cat-name {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  min-width: 70px;
}

.cat-children {
  font-size: 12px;
  color: #999;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.quick-nav {
  display: flex;
  align-items: center;
  margin-left: 20px;
}

.quick-nav a {
  color: rgba(255,255,255,0.9);
  font-size: 13px;
  padding: 0 12px;
  height: 36px;
  display: flex;
  align-items: center;
  transition: background 0.15s;
}

.quick-nav a:hover, .quick-nav a.router-link-active {
  background: rgba(0,0,0,0.15);
}

.flash-link {
  color: #FFD700 !important;
  font-weight: bold;
}

.cart-link {
  color: #fff;
}
</style>
