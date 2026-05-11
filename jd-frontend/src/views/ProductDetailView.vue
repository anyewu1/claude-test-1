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
            <div class="reviews-section" v-loading="reviewLoading">
              <!-- Summary -->
              <div class="review-summary">
                <el-rate :model-value="product.rating" disabled show-score size="large" />
                <span class="review-summary-text">
                  共 {{ product.ratingCount?.toLocaleString() }} 条评价，
                  好评率 {{ Math.floor(product.rating / 5 * 100) }}%
                </span>
              </div>
              <el-divider />

              <!-- Write review -->
              <div v-if="userStore.isLoggedIn" class="write-review">
                <div class="write-review-title">写评价</div>
                <div class="write-review-rating">
                  <span>评分：</span>
                  <el-rate v-model="newReview.rating" />
                </div>
                <el-input
                  v-model="newReview.content"
                  type="textarea"
                  :rows="3"
                  placeholder="分享您的使用体验..."
                  maxlength="500"
                  show-word-limit
                />
                <el-button
                  type="primary"
                  @click="submitReview"
                  :loading="submittingReview"
                  style="margin-top: 12px"
                >提交评价</el-button>
              </div>
              <div v-else class="login-to-review">
                <el-button link type="primary" @click="router.push('/login')">登录</el-button>
                后可发表评价
              </div>
              <el-divider />

              <!-- Review list -->
              <el-empty v-if="reviews.length === 0 && !reviewLoading" description="暂无评价" />
              <div class="review-item" v-for="r in reviews" :key="r.id">
                <div class="review-header">
                  <div class="reviewer-av">{{ r.username?.charAt(0)?.toUpperCase() }}</div>
                  <div>
                    <div class="reviewer-name">{{ r.username }}</div>
                    <el-rate :model-value="r.rating" disabled size="small" />
                  </div>
                  <span class="review-date">{{ new Date(r.createdAt).toLocaleDateString('zh-CN') }}</span>
                </div>
                <p class="review-text">{{ r.content }}</p>
              </div>

              <!-- Pagination -->
              <div v-if="reviewTotal > 10" class="review-pagination">
                <el-pagination
                  v-model:current-page="reviewPage"
                  :page-size="10"
                  :total="reviewTotal"
                  layout="prev, pager, next"
                  @current-change="loadReviews"
                />
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
import { ElMessage } from 'element-plus'
import ProductCard from '@/components/ProductCard.vue'
import { productApi } from '@/api/product'
import { reviewApi } from '@/api/review'
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

const reviews = ref([])
const reviewTotal = ref(0)
const reviewPage = ref(1)
const reviewLoading = ref(false)
const submittingReview = ref(false)
const newReview = ref({ rating: 5, content: '' })

const currentImage = computed(() => {
  const imgs = product.value?.imageList || []
  return imgs[currentImgIdx.value] || product.value?.coverImage || ''
})

const discount = computed(() => {
  if (!product.value?.originalPrice) return null
  return (product.value.price / product.value.originalPrice * 10).toFixed(1)
})

onMounted(() => loadProduct())
watch(() => route.params.id, loadProduct)

async function loadProduct() {
  loading.value = true
  try {
    const res = await productApi.detail(route.params.id)
    product.value = res.data
    currentImgIdx.value = 0
    reviewPage.value = 1

    const [recRes] = await Promise.all([
      productApi.recommend({ productId: res.data.id, limit: 4 }),
      loadReviews()
    ])
    recommendations.value = recRes.data
  } finally {
    loading.value = false
  }
}

async function loadReviews() {
  reviewLoading.value = true
  try {
    const res = await reviewApi.listByProduct(route.params.id, { page: reviewPage.value, size: 10 })
    reviews.value = res.data.records
    reviewTotal.value = res.data.total
  } finally {
    reviewLoading.value = false
  }
}

async function submitReview() {
  if (!userStore.isLoggedIn) { router.push('/login'); return }
  if (!newReview.value.content.trim()) { ElMessage.warning('请填写评价内容'); return }
  submittingReview.value = true
  try {
    await reviewApi.addReview(product.value.id, newReview.value)
    ElMessage.success('评价提交成功')
    newReview.value = { rating: 5, content: '' }
    reviewPage.value = 1
    await loadReviews()
  } finally {
    submittingReview.value = false
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

.reviews-section { padding: 20px 0; }

.review-summary {
  display: flex;
  align-items: center;
  gap: 16px;
}

.review-summary-text { color: #999; font-size: 14px; }

.write-review {
  margin-bottom: 20px;
  padding: 16px;
  background: #f9f9f9;
  border-radius: 8px;
}

.write-review-title {
  font-size: 15px;
  font-weight: bold;
  margin-bottom: 12px;
}

.write-review-rating {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  font-size: 14px;
  color: #666;
}

.login-to-review {
  font-size: 14px;
  color: #999;
  margin-bottom: 16px;
}

.review-item {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.review-pagination { display: flex; justify-content: center; margin-top: 20px; }

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
