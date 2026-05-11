<template>
  <div class="product-list-page page-container">
    <div class="page-layout">
      <!-- Sidebar -->
      <aside class="sidebar card">
        <div class="sidebar-title">商品分类</div>
        <div class="cat-list">
          <div
            v-for="cat in categories"
            :key="cat.id"
            class="cat-group"
          >
            <div
              class="cat-parent"
              :class="{ active: filters.categoryId === cat.id }"
              @click="setCategory(cat.id)"
            >
              {{ cat.icon }} {{ cat.name }}
            </div>
            <div
              v-for="child in cat.children"
              :key="child.id"
              class="cat-child"
              :class="{ active: filters.categoryId === child.id }"
              @click="setCategory(child.id)"
            >
              {{ child.name }}
            </div>
          </div>
        </div>

        <div class="sidebar-title" style="margin-top: 20px">价格区间</div>
        <div class="price-range">
          <el-input v-model="priceMin" placeholder="最低价" size="small" />
          <span>-</span>
          <el-input v-model="priceMax" placeholder="最高价" size="small" />
          <el-button size="small" @click="applyPriceFilter">确定</el-button>
        </div>
      </aside>

      <!-- Main content -->
      <main class="main-area">
        <!-- Filter bar -->
        <div class="filter-bar card">
          <div class="breadcrumb" v-if="currentCategoryName">
            <span>全部商品</span> > <span class="current">{{ currentCategoryName }}</span>
          </div>
          <div v-if="filters.keyword" class="keyword-tag">
            搜索 "{{ filters.keyword }}"
            <el-icon @click="clearKeyword" style="cursor:pointer"><Close /></el-icon>
          </div>
          <div class="sort-bar">
            <span class="sort-label">排序：</span>
            <el-radio-group v-model="filters.sortBy" @change="loadProducts(1)">
              <el-radio-button value="createdAt">综合</el-radio-button>
              <el-radio-button value="sales">销量</el-radio-button>
              <el-radio-button value="price">价格</el-radio-button>
              <el-radio-button value="rating">好评</el-radio-button>
            </el-radio-group>
            <el-button
              v-if="filters.sortBy === 'price'"
              link
              @click="toggleSortOrder"
              style="margin-left: 8px"
            >
              <el-icon>
                <component :is="filters.sortOrder === 'asc' ? 'SortUp' : 'SortDown'" />
              </el-icon>
              {{ filters.sortOrder === 'asc' ? '价格从低到高' : '价格从高到低' }}
            </el-button>
          </div>
        </div>

        <!-- Products grid -->
        <div v-loading="loading" class="products-area">
          <div v-if="products.length" class="product-grid">
            <ProductCard v-for="p in products" :key="p.id" :product="p" />
          </div>
          <el-empty v-else-if="!loading" description="暂无商品" />
        </div>

        <!-- Pagination -->
        <div class="pagination-wrap" v-if="total > 0">
          <el-pagination
            v-model:current-page="filters.page"
            :page-size="filters.size"
            :total="total"
            layout="prev, pager, next, total"
            @current-change="loadProducts"
            background
          />
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import ProductCard from '@/components/ProductCard.vue'
import { productApi } from '@/api/product'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const products = ref([])
const total = ref(0)
const categories = ref([])
const priceMin = ref('')
const priceMax = ref('')

const filters = reactive({
  page: 1,
  size: 20,
  categoryId: null,
  keyword: '',
  minPrice: null,
  maxPrice: null,
  sortBy: 'createdAt',
  sortOrder: 'desc'
})

const currentCategoryName = computed(() => {
  if (!filters.categoryId) return ''
  for (const cat of categories.value) {
    if (cat.id === filters.categoryId) return cat.name
    if (cat.children) {
      const child = cat.children.find(c => c.id === filters.categoryId)
      if (child) return child.name
    }
  }
  return ''
})

onMounted(async () => {
  const res = await productApi.categories()
  categories.value = res.data

  syncFromRoute()
  loadProducts(1)
})

watch(() => route.query, () => {
  syncFromRoute()
  loadProducts(1)
})

function syncFromRoute() {
  filters.categoryId = route.query.categoryId ? Number(route.query.categoryId) : null
  filters.keyword = route.query.keyword || ''
  if (route.query.keyword) filters.sortBy = 'createdAt'
}

async function loadProducts(page) {
  if (page) filters.page = page
  loading.value = true
  try {
    const res = await productApi.list({
      page: filters.page,
      size: filters.size,
      categoryId: filters.categoryId,
      keyword: filters.keyword || undefined,
      minPrice: filters.minPrice || undefined,
      maxPrice: filters.maxPrice || undefined,
      sortBy: filters.sortBy,
      sortOrder: filters.sortOrder
    })
    products.value = res.data.records
    total.value = Number(res.data.total)
  } finally {
    loading.value = false
  }
}

function setCategory(id) {
  filters.categoryId = id === filters.categoryId ? null : id
  loadProducts(1)
}

function applyPriceFilter() {
  filters.minPrice = priceMin.value ? Number(priceMin.value) : null
  filters.maxPrice = priceMax.value ? Number(priceMax.value) : null
  loadProducts(1)
}

function toggleSortOrder() {
  filters.sortOrder = filters.sortOrder === 'asc' ? 'desc' : 'asc'
  loadProducts(1)
}

function clearKeyword() {
  filters.keyword = ''
  router.push({ path: '/products', query: { categoryId: filters.categoryId } })
}
</script>

<style scoped>
.product-list-page {
  padding: 20px 15px;
}

.page-layout {
  display: flex;
  gap: 20px;
}

.sidebar {
  width: 180px;
  flex-shrink: 0;
  padding: 16px;
  align-self: flex-start;
  position: sticky;
  top: 90px;
}

.sidebar-title {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  padding-bottom: 8px;
  border-bottom: 2px solid var(--jd-red);
  margin-bottom: 12px;
}

.cat-parent {
  padding: 8px 10px;
  cursor: pointer;
  border-radius: 4px;
  font-size: 14px;
  color: #333;
  transition: all 0.15s;
}

.cat-parent:hover, .cat-parent.active {
  background: #fff0f0;
  color: var(--jd-red);
}

.cat-child {
  padding: 6px 10px 6px 22px;
  cursor: pointer;
  font-size: 13px;
  color: #666;
  border-radius: 4px;
}

.cat-child:hover, .cat-child.active {
  color: var(--jd-red);
}

.price-range {
  display: flex;
  align-items: center;
  gap: 4px;
  flex-wrap: wrap;
}

.price-range .el-input {
  width: 65px;
}

.main-area {
  flex: 1;
  min-width: 0;
}

.filter-bar {
  padding: 14px 16px;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.breadcrumb {
  font-size: 13px;
  color: #999;
}

.breadcrumb .current {
  color: var(--jd-red);
}

.keyword-tag {
  display: flex;
  align-items: center;
  gap: 4px;
  background: #fff0f0;
  color: var(--jd-red);
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 13px;
}

.sort-bar {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-left: auto;
}

.sort-label {
  font-size: 13px;
  color: #666;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.pagination-wrap {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}
</style>
