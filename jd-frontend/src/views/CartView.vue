<template>
  <div class="cart-page page-container">
    <div class="page-title">
      <el-icon><ShoppingCart /></el-icon> 我的购物车
      <span class="item-count">（共 {{ cartStore.totalCount }} 件商品）</span>
    </div>

    <div v-if="cartStore.items.length" class="cart-layout">
      <!-- Cart items -->
      <div class="cart-items-wrap">
        <div class="cart-header card">
          <el-checkbox v-model="allSelected" @change="handleSelectAll">全选</el-checkbox>
          <span>商品信息</span>
          <span class="col-price">单价</span>
          <span class="col-qty">数量</span>
          <span class="col-total">小计</span>
          <span class="col-action">操作</span>
        </div>

        <div class="cart-item card" v-for="item in cartStore.items" :key="item.id">
          <el-checkbox
            :model-value="item.selected"
            @change="(val) => cartStore.updateItem(item.id, { selected: val })"
          />
          <img :src="item.productImage" :alt="item.productName" class="item-img" @click="goProduct(item.productId)" />
          <div class="item-name" @click="goProduct(item.productId)">{{ item.productName }}</div>
          <div class="col-price price-tag">¥{{ item.price }}</div>
          <div class="col-qty">
            <el-input-number
              :model-value="item.quantity"
              :min="1"
              :max="item.stock"
              size="small"
              @change="(val) => cartStore.updateItem(item.id, { quantity: val })"
            />
          </div>
          <div class="col-total price-tag">¥{{ (item.price * item.quantity).toFixed(2) }}</div>
          <div class="col-action">
            <el-popconfirm title="确认删除该商品？" @confirm="cartStore.removeItem(item.id)">
              <template #reference>
                <el-button link type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </div>
        </div>
      </div>

      <!-- Summary -->
      <div class="cart-summary card">
        <h3>订单摘要</h3>
        <div class="summary-row">
          <span>已选商品</span>
          <span>{{ cartStore.selectedCount }} 件</span>
        </div>
        <div class="summary-row">
          <span>商品总价</span>
          <span class="price-tag">¥{{ cartStore.totalAmount.toFixed(2) }}</span>
        </div>
        <div class="summary-row discount">
          <span>优惠</span>
          <span>-¥0.00</span>
        </div>
        <el-divider />
        <div class="summary-total">
          <span>合计</span>
          <span class="total-price">¥{{ cartStore.totalAmount.toFixed(2) }}</span>
        </div>
        <el-button
          type="primary"
          size="large"
          class="checkout-btn"
          :disabled="cartStore.selectedCount === 0"
          @click="goCheckout"
        >
          去结算（{{ cartStore.selectedCount }}）
        </el-button>

        <div class="clear-btn-wrap">
          <el-popconfirm title="确认清空购物车？" @confirm="cartStore.clearCart()">
            <template #reference>
              <el-button link type="danger" size="small">清空购物车</el-button>
            </template>
          </el-popconfirm>
        </div>
      </div>
    </div>

    <div v-else class="empty-cart">
      <el-empty description="购物车空空如也">
        <el-button type="primary" @click="$router.push('/')">去逛逛</el-button>
      </el-empty>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cartStore'

const router = useRouter()
const cartStore = useCartStore()

onMounted(() => cartStore.fetchCart())

const allSelected = computed({
  get: () => cartStore.items.length > 0 && cartStore.items.every(i => i.selected),
  set: (val) => cartStore.selectAll(val)
})

function handleSelectAll(val) {
  cartStore.selectAll(val)
}

function goProduct(id) {
  router.push(`/products/${id}`)
}

function goCheckout() {
  if (cartStore.selectedCount === 0) return
  router.push('/checkout')
}
</script>

<style scoped>
.cart-page {
  padding: 20px 15px;
}

.page-title {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.item-count {
  font-size: 15px;
  color: #999;
  font-weight: normal;
}

.cart-layout {
  display: flex;
  gap: 20px;
  align-items: flex-start;
}

.cart-items-wrap {
  flex: 1;
}

.cart-header {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  font-size: 14px;
  color: #666;
  gap: 16px;
  margin-bottom: 8px;
}

.cart-header span {
  flex: 1;
}

.cart-item {
  display: flex;
  align-items: center;
  padding: 16px 20px;
  gap: 16px;
  margin-bottom: 8px;
}

.item-img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
  cursor: pointer;
  flex-shrink: 0;
}

.item-name {
  flex: 1;
  font-size: 14px;
  color: #333;
  cursor: pointer;
  line-height: 1.4;
}

.item-name:hover {
  color: var(--jd-red);
}

.col-price, .col-qty, .col-total, .col-action {
  width: 100px;
  text-align: center;
  font-size: 14px;
}

.price-tag {
  color: var(--jd-red);
  font-weight: bold;
}

.cart-summary {
  width: 280px;
  flex-shrink: 0;
  padding: 20px;
  position: sticky;
  top: 90px;
}

.cart-summary h3 {
  margin-bottom: 16px;
  font-size: 16px;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  margin-bottom: 12px;
  color: #666;
}

.discount {
  color: var(--jd-red);
}

.summary-total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 16px;
}

.total-price {
  font-size: 24px;
  color: var(--jd-red);
}

.checkout-btn {
  width: 100%;
  background: var(--jd-red);
  border-color: var(--jd-red);
  font-size: 16px;
  height: 44px;
}

.clear-btn-wrap {
  text-align: center;
  margin-top: 10px;
}

.empty-cart {
  padding: 60px 0;
}
</style>
