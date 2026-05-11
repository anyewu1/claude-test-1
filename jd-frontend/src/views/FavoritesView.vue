<template>
  <div class="favorites-page page-container">
    <div class="page-header">
      <h2 class="section-title">我的收藏</h2>
      <span class="item-count">共 {{ favoriteStore.count }} 件商品</span>
    </div>

    <div v-loading="loading">
      <el-empty
        v-if="!loading && favoriteStore.items.length === 0"
        description="您还没有收藏任何商品"
        :image-size="120"
      >
        <el-button type="primary" @click="$router.push('/products')">去逛逛</el-button>
      </el-empty>

      <div v-else class="favorites-grid">
        <div
          class="fav-item card"
          v-for="fav in favoriteStore.items"
          :key="fav.id"
          @click="$router.push(`/products/${fav.productId}`)"
        >
          <div class="fav-img-wrap">
            <img :src="fav.coverImage" :alt="fav.productName" class="fav-img" />
          </div>
          <div class="fav-body">
            <div class="fav-name">{{ fav.productName }}</div>
            <div class="fav-price">
              <span class="sym">¥</span>
              <span class="amount">{{ fav.price }}</span>
              <span v-if="fav.originalPrice" class="original">¥{{ fav.originalPrice }}</span>
            </div>
          </div>
          <el-button
            class="remove-btn"
            size="small"
            type="danger"
            plain
            @click.stop="favoriteStore.remove(fav.productId)"
          >取消收藏</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useFavoriteStore } from '@/stores/favoriteStore'

const favoriteStore = useFavoriteStore()
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    await favoriteStore.fetchFavorites()
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.favorites-page { padding: 20px 15px; }

.page-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.item-count { font-size: 14px; color: #999; }

.favorites-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.fav-item {
  cursor: pointer;
  overflow: hidden;
  transition: box-shadow 0.2s, transform 0.2s;
  padding-bottom: 12px;
}

.fav-item:hover {
  box-shadow: 0 8px 24px rgba(0,0,0,0.12);
  transform: translateY(-2px);
}

.fav-img-wrap {
  width: 100%;
  padding-top: 100%;
  position: relative;
  overflow: hidden;
}

.fav-img {
  position: absolute;
  top: 0; left: 0;
  width: 100%; height: 100%;
  object-fit: cover;
}

.fav-body { padding: 12px 12px 8px; }

.fav-name {
  font-size: 13px;
  color: #333;
  line-height: 1.4;
  height: 2.8em;
  overflow: hidden;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  margin-bottom: 8px;
}

.fav-price { color: var(--jd-red); margin-bottom: 8px; }
.sym { font-size: 12px; }
.amount { font-size: 18px; font-weight: bold; }
.original { font-size: 12px; color: #999; text-decoration: line-through; margin-left: 6px; }

.remove-btn { margin: 0 12px; width: calc(100% - 24px); }

@media (max-width: 768px) {
  .favorites-grid { grid-template-columns: repeat(2, 1fr); }
}
</style>
