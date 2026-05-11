<template>
  <div class="product-card" @click="goDetail">
    <div class="card-img-wrap">
      <img :src="product.coverImage" :alt="product.name" class="card-img" loading="lazy" />
      <span v-if="product.isFlashSale" class="badge flash-badge">闪购</span>
      <span v-if="discount" class="badge discount-badge">{{ discount }}折</span>
      <button
        class="fav-btn"
        :class="{ 'fav-active': isFavorited }"
        @click.stop="toggleFavorite"
        :title="isFavorited ? '取消收藏' : '加入收藏'"
      >
        <el-icon><StarFilled v-if="isFavorited" /><Star v-else /></el-icon>
      </button>
    </div>
    <div class="card-body">
      <div class="card-price">
        <span class="symbol">¥</span>
        <span class="amount">{{ product.price }}</span>
        <span v-if="product.originalPrice" class="original-price">¥{{ product.originalPrice }}</span>
      </div>
      <div class="card-name">{{ product.name }}</div>
      <div class="card-meta">
        <el-rate
          :model-value="product.rating"
          disabled
          size="small"
          :show-score="false"
        />
        <span class="sales-text">已售 {{ formatSales(product.sales) }}</span>
      </div>
      <el-button
        type="primary"
        size="small"
        class="add-cart-btn"
        @click.stop="addToCart"
        :loading="adding"
      >
        加入购物车
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cartStore'
import { useUserStore } from '@/stores/userStore'
import { useFavoriteStore } from '@/stores/favoriteStore'

const props = defineProps({
  product: { type: Object, required: true }
})

const router = useRouter()
const cartStore = useCartStore()
const userStore = useUserStore()
const favoriteStore = useFavoriteStore()
const adding = ref(false)

const isFavorited = computed(() => favoriteStore.isFavorited(props.product.id))

async function toggleFavorite() {
  if (!userStore.isLoggedIn) { router.push('/login'); return }
  await favoriteStore.toggle(props.product.id)
}

const discount = computed(() => {
  if (!props.product.originalPrice || props.product.originalPrice <= props.product.price) return null
  return (props.product.price / props.product.originalPrice * 10).toFixed(1)
})

function goDetail() {
  router.push(`/products/${props.product.id}`)
}

async function addToCart() {
  if (!userStore.isLoggedIn) {
    router.push('/login')
    return
  }
  adding.value = true
  try {
    await cartStore.addToCart(props.product.id, 1)
  } finally {
    adding.value = false
  }
}

function formatSales(n) {
  if (n >= 10000) return (n / 10000).toFixed(1) + '万'
  return n
}
</script>

<style scoped>
.product-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: box-shadow 0.2s, transform 0.2s;
  border: 1px solid #f0f0f0;
}

.product-card:hover {
  box-shadow: 0 8px 24px rgba(0,0,0,0.12);
  transform: translateY(-2px);
}

.card-img-wrap {
  position: relative;
  padding-top: 100%;
  overflow: hidden;
  background: #f9f9f9;
}

.card-img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.product-card:hover .card-img {
  transform: scale(1.05);
}

.badge {
  position: absolute;
  top: 8px;
  left: 8px;
  padding: 2px 6px;
  border-radius: 3px;
  font-size: 11px;
  font-weight: bold;
}

.flash-badge {
  background: var(--jd-red);
  color: #fff;
}

.discount-badge {
  background: var(--jd-orange);
  color: #fff;
  left: auto;
  right: 8px;
}

.fav-btn {
  position: absolute;
  bottom: 8px;
  right: 8px;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.9);
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 15px;
  color: #ccc;
  opacity: 0;
  transition: opacity 0.2s, color 0.2s;
}

.product-card:hover .fav-btn {
  opacity: 1;
}

.fav-btn.fav-active {
  opacity: 1;
  color: var(--jd-red);
}

.fav-btn:hover {
  color: var(--jd-red);
}

.card-body {
  padding: 12px;
}

.card-price {
  color: var(--jd-red);
  margin-bottom: 6px;
}

.symbol {
  font-size: 12px;
}

.amount {
  font-size: 20px;
  font-weight: bold;
}

.original-price {
  font-size: 12px;
  color: #999;
  text-decoration: line-through;
  margin-left: 6px;
}

.card-name {
  font-size: 14px;
  color: #333;
  line-height: 1.4;
  height: 2.8em;
  overflow: hidden;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  margin-bottom: 8px;
}

.card-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
}

.sales-text {
  font-size: 12px;
  color: #999;
}

.add-cart-btn {
  width: 100%;
  background: var(--jd-red);
  border-color: var(--jd-red);
}

.add-cart-btn:hover {
  background: var(--jd-red-dark);
  border-color: var(--jd-red-dark);
}
</style>
