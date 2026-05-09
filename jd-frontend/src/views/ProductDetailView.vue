<template>
  <div class="product-detail-page" v-loading="loading">
    <div class="page-container" v-if="product">
      <!-- Breadcrumb -->
      <el-breadcrumb class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/products', query: { categoryId: product.categoryId } }">
          {{ product.categoryName }}
        </el-breadcrumb-item>
        <el-breadcrumb-item>{{ product.name }}</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- Product main info -->
      <div class="product-main card">
        <!-- Images -->
        <div class="img-section">
          <div class="main-img-wrap">
            <img :src="currentImage" :alt="product.name" class="main-img" />
          </div>
          <div class="thumb-list">
            <div
              v-for="(img, i) in product.imageList"
              :key="i"
              class="thumb"
              :class="{ active: currentImgIdx === i }"
              @click="currentImgIdx = i"
            >
              <img :src="img" alt="" />
            </div>
          </div>
        </div>

        <!-- Info -->
        <div class="info-section">
          <h1 class="product-name">{{ product.name }}</h1>

          <div class="price-block">
            <div class="current-price">
              <span class="sym">¥</span>
              <span class="price">{{ product.price }}</span>
            </div>
            <div v-if="product.originalPrice" class="original">
              原价：<del>¥{{ product.originalPrice }}</del>
              <el-tag type="danger" size="small" style="margin-left: 8px">
                {{ discount }}折
              </el-tag>
            </div>
          </div>

          <div class="meta-block">
            <div class="meta-row">
              <span class="meta-label">评价</span>
              <el-rate :model-value="product.rating" disabled show-score text-color="#e1251b" />
              <span class="review-count">({{ product.ratingCount?.toLocaleString() }}条)</span>
            </div>
            <div class="meta-row">
              <span class="meta-label">销量</span>
              <span>{{ product.sales?.toLocaleString() }} 件</span>
            </div>
            <div class="meta-row">
              <span class="meta-label">库存</span>
              <span :class="{ 'low-stock': product.stock < 10 }">
                {{ product.stock > 0 ? (product.stock < 10 ? `仅剩${product.stock}件` : '有货') : '已售罄' }}
              </span>
            </div>
          </div>

          <!-- Quantity -->
          <div class="quantity-section">
            <span class="meta-label">数量</span>
            <el-input-number
              v-model="quantity"
              :min="1"
              :max="product.stock"
              :disabled="product.stock === 0"
            />
            <span class="stock-tip">库存 {{ product.stock }} 件</span>
          </div>

          <!-- Actions -->
          <div class="action-buttons">
            <el-button
              size="large"
              type="danger"
              @click="buyNow"
              :disabled="product.stock === 0"
            >
              立即购买
            </el-button>
            <el-button
              size="large"
              @click="addToCart"
              :loading="adding"
              :disabled="product.stock === 0"
              class="add-cart-btn"
            >
              <el-icon><ShoppingCart /></el-icon>
              加入购物车
            </el-button>
          </div>

          <!-- Guarantees -->
          <div class="guarantees">
            <span class="guarantee-item">🛡️ 正品保障</span>
            <span class="guarantee-item">🚚 次日达</span>
            <span class="guarantee-item">🔄 7天退货</span>
            <span class="guarantee-item">📦 免运费</span>
          </div>
        </div>
      </div>

      <!-- Tabs: Description & Reviews -->
      <div class="tabs-section card">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="商品详情" name="description">
            <div class="description-content">
              <p>{{ product.description }}</p>
            </div>
          </el-tab-pane>
          <el-tab-pane label="用户评价" name="reviews">
            <div class="reviews-placeholder">
              <el-rate :model-value="product.rating" disabled show-score size="large" />
              <p style="color:#999;margin-top:12px">
                共 {{ product.ratingCount?.toLocaleString() }} 条评价，
                好评率 {{ Math.floor(product.rating / 5 * 100) }}%
              </p>
              <el-divider />
              <div class="mock-review" v-for="r in mockReviews" :key="r.user">
                <div class="review-header">
                  <div class="reviewer-av">{{ r.user.charAt(0) }}</div>
                  <div>
                    <div class="reviewer-name">{{ r.user }}</div>
                    <el-rate :model-value="r.rating" disabled size="small" />
                  </div>
                  <span class="review-date">{{ r.date }}</span>
                </div>
                <p class="review-text">{{ r.text }}</p>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>

      <!-- Recommendations -->
      <section class="recommend-section" v-if="recommendations.length">
        <div class="section-title">相关推荐</div>
        <div class="product-grid">
          <ProductCard v-for="p in recommendations" :key="p.id" :product="p" />
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import ProductCard from '@/components/ProductCard.vue'
import { productApi } from '@/api/product'
import { useCartStore } from '@/stores/cartStore'
import { useUserStore } from '@/stores/userStore'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()
const userStore = useUserStore()

const loading = ref(false)
const adding = ref(false)
const product = ref(null)
const currentImgIdx = ref(0)
const quantity = ref(1)
const activeTab = ref('description')
const recommendations = ref([])

const currentImage = computed(() => {
  const imgs = product.value?.imageList || []
  return imgs[currentImgIdx.value] || product.value?.coverImage || ''
})

const discount = computed(() => {
  if (!product.value?.originalPrice) return null
  return (product.value.price / product.value.originalPrice * 10).toFixed(1)
})

const mockReviews = [
  { user: '王**', rating: 5, date: '2024-03-15', text: '商品质量非常好，物流很快，第二天就到了，包装也很完整，非常满意！' },
  { user: '李**', rating: 4, date: '2024-03-10', text: '整体体验不错，性能强劲，就是价格有点贵，但京东正品有保障。' },
  { user: '张**', rating: 5, date: '2024-03-08', text: '买了很久了，一直用得很好，强烈推荐！售后服务也很贴心。' }
]

onMounted(() => loadProduct())
watch(() => route.params.id, loadProduct)

async function loadProduct() {
  loading.value = true
  try {
    const res = await productApi.detail(route.params.id)
    product.value = res.data
    currentImgIdx.value = 0

    const recRes = await productApi.recommend({ productId: res.data.id, limit: 4 })
    recommendations.value = recRes.data
  } finally {
    loading.value = false
  }
}

async function addToCart() {
  if (!userStore.isLoggedIn) { router.push('/login'); return }
  adding.value = true
  try {
    await cartStore.addToCart(product.value.id, quantity.value)
  } finally {
    adding.value = false
  }
}

async function buyNow() {
  if (!userStore.isLoggedIn) { router.push('/login'); return }
  await cartStore.addToCart(product.value.id, quantity.value)
  router.push('/cart')
}
</script>

<style scoped>
.product-detail-page {
  padding: 20px 0;
}

.breadcrumb {
  margin-bottom: 16px;
}

.product-main {
  display: flex;
  gap: 40px;
  padding: 24px;
  margin-bottom: 20px;
}

.img-section {
  width: 400px;
  flex-shrink: 0;
}

.main-img-wrap {
  width: 400px;
  height: 400px;
  overflow: hidden;
  border-radius: 8px;
  border: 1px solid #f0f0f0;
  margin-bottom: 12px;
}

.main-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.main-img:hover {
  transform: scale(1.1);
}

.thumb-list {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.thumb {
  width: 60px;
  height: 60px;
  border: 2px solid transparent;
  border-radius: 4px;
  cursor: pointer;
  overflow: hidden;
}

.thumb.active {
  border-color: var(--jd-red);
}

.thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.info-section {
  flex: 1;
}

.product-name {
  font-size: 20px;
  color: #333;
  line-height: 1.4;
  margin-bottom: 20px;
  font-weight: normal;
}

.price-block {
  background: #f9f0f0;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.current-price {
  color: var(--jd-red);
  margin-bottom: 6px;
}

.sym {
  font-size: 16px;
}

.price {
  font-size: 36px;
  font-weight: bold;
}

.original {
  font-size: 14px;
  color: #999;
}

.meta-block {
  margin-bottom: 20px;
}

.meta-row {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
  font-size: 14px;
}

.meta-label {
  width: 50px;
  color: #999;
  flex-shrink: 0;
}

.review-count {
  color: #999;
  font-size: 13px;
}

.low-stock {
  color: var(--jd-orange);
  font-weight: bold;
}

.quantity-section {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
}

.stock-tip {
  font-size: 13px;
  color: #999;
}

.action-buttons {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.action-buttons .el-button {
  min-width: 160px;
}

.add-cart-btn {
  background: var(--jd-orange);
  border-color: var(--jd-orange);
  color: #fff;
}

.add-cart-btn:hover {
  background: #e65c00;
  border-color: #e65c00;
}

.guarantees {
  display: flex;
  gap: 20px;
  padding: 16px;
  background: #f9f9f9;
  border-radius: 8px;
}

.guarantee-item {
  font-size: 13px;
  color: #666;
}

.tabs-section {
  padding: 24px;
  margin-bottom: 20px;
}

.description-content {
  padding: 20px 0;
  font-size: 15px;
  color: #555;
  line-height: 2;
}

.reviews-placeholder {
  padding: 20px 0;
}

.mock-review {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.review-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 10px;
}

.reviewer-av {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: var(--jd-red);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
}

.reviewer-name {
  font-size: 14px;
  font-weight: bold;
}

.review-date {
  margin-left: auto;
  font-size: 12px;
  color: #999;
}

.review-text {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.recommend-section {
  margin-bottom: 30px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-top: 16px;
}
</style>
