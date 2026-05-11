<template>
  <div class="home-page">
    <!-- Banner carousel -->
    <div class="banner-section">
      <el-carousel height="400px" :interval="4000" arrow="always">
        <el-carousel-item v-for="(banner, i) in banners" :key="i">
          <div class="banner-slide" :style="{ background: banner.bg }">
            <img :src="banner.img" :alt="banner.title" class="banner-img" />
            <div class="banner-content">
              <h2>{{ banner.title }}</h2>
              <p>{{ banner.subtitle }}</p>
              <el-button type="primary" @click="$router.push(banner.link)">
                {{ banner.cta }}
              </el-button>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <div class="page-container">
      <!-- Category quick nav -->
      <div class="category-nav card">
        <RouterLink
          v-for="cat in topCategories"
          :key="cat.id"
          :to="`/products?categoryId=${cat.id}`"
          class="cat-nav-item"
        >
          <span class="cat-emoji">{{ cat.icon }}</span>
          <span class="cat-label">{{ cat.name }}</span>
        </RouterLink>
      </div>

      <!-- Flash Sale Section -->
      <section class="flash-sale-section" v-if="flashSaleProducts.length">
        <div class="section-header">
          <div class="section-title">⚡ 限时闪购</div>
          <div class="countdown">
            <span class="countdown-label">距结束</span>
            <span class="time-block">{{ countdown.h }}</span>:
            <span class="time-block">{{ countdown.m }}</span>:
            <span class="time-block">{{ countdown.s }}</span>
          </div>
          <RouterLink to="/products?flashSale=1" class="view-all">查看全部 ›</RouterLink>
        </div>
        <div class="product-grid-4">
          <ProductCard
            v-for="product in flashSaleProducts"
            :key="product.id"
            :product="product"
          />
        </div>
      </section>

      <!-- Featured Products by Category -->
      <section v-for="section in categorySections" :key="section.id" class="category-section">
        <div class="section-header">
          <div class="section-title">{{ section.icon }} {{ section.name }}</div>
          <RouterLink :to="`/products?categoryId=${section.id}`" class="view-all">查看全部 ›</RouterLink>
        </div>
        <div class="product-grid-4" v-loading="loadingMap[section.id]">
          <ProductCard
            v-for="product in section.products"
            :key="product.id"
            :product="product"
          />
        </div>
      </section>

      <!-- AI Recommend Section -->
      <section class="ai-section card">
        <div class="ai-section-header">
          <span class="ai-icon">🤖</span>
          <div>
            <h3>AI智能推荐</h3>
            <p>告诉我您的需求，AI为您精准推荐</p>
          </div>
        </div>
        <div class="ai-input-area">
          <el-input
            v-model="aiQuery"
            placeholder="例如：适合学生用的平板，预算3000元"
            clearable
            @keyup.enter="getAIRecommend"
          />
          <el-input-number
            v-model="aiBudget"
            :min="0"
            placeholder="预算(元)"
            controls-position="right"
          />
          <el-button type="primary" @click="getAIRecommend" :loading="aiLoading">
            <el-icon><MagicStick /></el-icon> AI推荐
          </el-button>
        </div>
        <div v-if="aiResult.analysis" class="ai-result">
          <div class="ai-analysis">{{ aiResult.analysis }}</div>
          <div class="product-grid-3">
            <ProductCard
              v-for="product in aiResult.products"
              :key="product.id"
              :product="product"
            />
          </div>
        </div>
      </section>

      <!-- Recently Viewed -->
      <section class="recently-section" v-if="recentProducts.length">
        <div class="section-title">👁 最近浏览</div>
        <div class="recent-grid">
          <div
            class="recent-item card"
            v-for="item in recentProducts"
            :key="item.id"
            @click="$router.push(`/products/${item.productId}`)"
          >
            <img :src="item.coverImage" :alt="item.productName" class="recent-img" />
            <div class="recent-name">{{ item.productName }}</div>
            <div class="recent-price">¥{{ item.price }}</div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import ProductCard from '@/components/ProductCard.vue'
import { productApi } from '@/api/product'
import { aiApi } from '@/api/ai'
import { historyApi } from '@/api/history'
import { useUserStore } from '@/stores/userStore'

const banners = [
  {
    title: 'iPhone 15 Pro 系列',
    subtitle: '钛金属边框 · A17 Pro芯片 · 专业摄影',
    cta: '立即购买',
    link: '/products?categoryId=11',
    bg: 'linear-gradient(135deg, #1a1a2e, #16213e)',
    img: 'https://picsum.photos/seed/banner1/800/400'
  },
  {
    title: '超级大促',
    subtitle: '爆款手机 · 笔记本 · 家电 · 全场低至2折',
    cta: '查看活动',
    link: '/products',
    bg: 'linear-gradient(135deg, #e1251b, #ff6b35)',
    img: 'https://picsum.photos/seed/banner2/800/400'
  },
  {
    title: '数码新品首发',
    subtitle: '最新旗舰机型 · 新品预售 · 到货即发',
    cta: '抢先购',
    link: '/products?sortBy=createdAt',
    bg: 'linear-gradient(135deg, #0f3460, #533483)',
    img: 'https://picsum.photos/seed/banner3/800/400'
  }
]

const topCategories = ref([])
const flashSaleProducts = ref([])
const categorySections = ref([])
const loadingMap = ref({})
const countdown = ref({ h: '02', m: '30', s: '00' })
let countdownTimer = null

const aiQuery = ref('')
const aiBudget = ref(null)
const aiLoading = ref(false)
const aiResult = ref({ analysis: '', products: [] })

const recentProducts = ref([])
const userStore = useUserStore()

onMounted(async () => {
  try {
    const [catsRes, flashRes] = await Promise.all([
      productApi.topCategories(),
      productApi.flashSale()
    ])
    topCategories.value = catsRes.data
    flashSaleProducts.value = flashRes.data.slice(0, 8)

    // Load products for first 3 categories
    const sections = [
      { id: 1, icon: '📱', name: '手机数码' },
      { id: 2, icon: '💻', name: '电脑办公' },
      { id: 3, icon: '🏠', name: '家用电器' }
    ]
    categorySections.value = sections.map(s => ({ ...s, products: [] }))

    sections.forEach(async (s, idx) => {
      loadingMap.value[s.id] = true
      try {
        const res = await productApi.list({ categoryId: s.id, size: 8 })
        categorySections.value[idx].products = res.data.records
      } finally {
        loadingMap.value[s.id] = false
      }
    })
    // Load recently viewed for logged-in users
    if (userStore.isLoggedIn) {
      historyApi.recent(8).then(res => { recentProducts.value = res.data }).catch(() => {})
    }
  } catch (e) {
    console.error(e)
  }

  startCountdown()
})

onUnmounted(() => {
  clearInterval(countdownTimer)
})

function startCountdown() {
  let total = 2 * 3600 + 30 * 60
  countdownTimer = setInterval(() => {
    if (total <= 0) { total = 4 * 3600; return }
    total--
    const h = Math.floor(total / 3600)
    const m = Math.floor((total % 3600) / 60)
    const s = total % 60
    countdown.value = {
      h: String(h).padStart(2, '0'),
      m: String(m).padStart(2, '0'),
      s: String(s).padStart(2, '0')
    }
  }, 1000)
}

async function getAIRecommend() {
  if (!aiQuery.value.trim()) return
  aiLoading.value = true
  try {
    const res = await aiApi.recommend({ query: aiQuery.value, budget: aiBudget.value, limit: 6 })
    aiResult.value = res.data
  } catch {
    aiResult.value = { analysis: '推荐获取失败，请稍后重试', products: [] }
  } finally {
    aiLoading.value = false
  }
}
</script>

<style scoped>
.home-page {
  padding-bottom: 40px;
}

.banner-section {
  margin-bottom: 20px;
}

.banner-slide {
  width: 100%;
  height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.banner-img {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  opacity: 0.4;
}

.banner-content {
  position: relative;
  text-align: center;
  color: #fff;
  z-index: 1;
}

.banner-content h2 {
  font-size: 36px;
  margin-bottom: 12px;
  text-shadow: 0 2px 8px rgba(0,0,0,0.3);
}

.banner-content p {
  font-size: 16px;
  margin-bottom: 20px;
  opacity: 0.9;
}

.category-nav {
  display: flex;
  justify-content: space-around;
  padding: 20px;
  margin-bottom: 20px;
}

.cat-nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  text-decoration: none;
  transition: transform 0.2s;
}

.cat-nav-item:hover {
  transform: translateY(-3px);
}

.cat-emoji {
  font-size: 28px;
}

.cat-label {
  font-size: 13px;
  color: #333;
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  gap: 16px;
}

.countdown {
  display: flex;
  align-items: center;
  gap: 4px;
  margin-left: 16px;
}

.countdown-label {
  font-size: 13px;
  color: #666;
}

.time-block {
  background: #333;
  color: #fff;
  padding: 2px 6px;
  border-radius: 3px;
  font-size: 14px;
  font-weight: bold;
  font-family: monospace;
}

.view-all {
  margin-left: auto;
  color: var(--jd-red);
  font-size: 13px;
  text-decoration: none;
}

.view-all:hover {
  text-decoration: underline;
}

.flash-sale-section, .category-section {
  margin-bottom: 30px;
}

.product-grid-4 {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.product-grid-3 {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-top: 20px;
}

.ai-section {
  padding: 24px;
  margin-bottom: 30px;
}

.ai-section-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.ai-icon {
  font-size: 40px;
}

.ai-section-header h3 {
  font-size: 20px;
  color: #333;
  margin-bottom: 4px;
}

.ai-section-header p {
  font-size: 13px;
  color: #999;
}

.ai-input-area {
  display: flex;
  gap: 12px;
  align-items: center;
}

.ai-input-area .el-input {
  flex: 1;
}

.ai-result {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px dashed #e5e5e5;
}

.ai-analysis {
  padding: 12px 16px;
  background: #fff8f0;
  border-radius: 8px;
  border-left: 3px solid var(--jd-orange);
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 16px;
}

.recently-section {
  margin-bottom: 30px;
}

.recent-grid {
  display: flex;
  gap: 12px;
  margin-top: 16px;
  overflow-x: auto;
  padding-bottom: 8px;
}

.recent-item {
  min-width: 120px;
  cursor: pointer;
  padding: 10px;
  text-align: center;
  transition: box-shadow 0.2s;
  border-radius: 8px;
}

.recent-item:hover { box-shadow: 0 4px 16px rgba(0,0,0,0.1); }

.recent-img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 6px;
}

.recent-name {
  font-size: 12px;
  color: #333;
  overflow: hidden;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  line-height: 1.3;
  margin-bottom: 4px;
}

.recent-price {
  font-size: 13px;
  color: var(--jd-red);
  font-weight: bold;
}
</style>
