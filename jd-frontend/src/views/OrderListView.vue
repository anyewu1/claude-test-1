<template>
  <div class="order-list-page page-container">
    <div class="page-title">我的订单</div>

    <div class="card order-tabs">
      <el-tabs v-model="activeStatus" @tab-change="handleTabChange">
        <el-tab-pane label="全部" name="" />
        <el-tab-pane label="待付款" name="0" />
        <el-tab-pane label="待发货" name="1" />
        <el-tab-pane label="待收货" name="2" />
        <el-tab-pane label="已完成" name="3" />
        <el-tab-pane label="已取消" name="4" />
      </el-tabs>
    </div>

    <div v-loading="loading" class="order-list">
      <template v-if="orders.length">
        <div class="order-card card" v-for="order in orders" :key="order.id">
          <div class="order-header">
            <span class="order-no">订单号：{{ order.orderNo }}</span>
            <span class="order-time">{{ formatDate(order.createdAt) }}</span>
            <span class="order-status" :class="statusClass(order.status)">
              {{ statusText(order.status) }}
            </span>
          </div>

          <div class="order-items">
            <div
              class="order-item"
              v-for="item in order.items"
              :key="item.id"
              @click="$router.push(`/products/${item.productId}`)"
            >
              <img :src="item.productImage" :alt="item.productName" />
              <div class="item-info">
                <div class="item-name">{{ item.productName }}</div>
                <div class="item-qty">× {{ item.quantity }}</div>
              </div>
              <div class="item-price">¥{{ item.price }}</div>
            </div>
          </div>

          <div class="order-footer">
            <div class="order-amount">
              实付：<span class="amount">¥{{ order.actualAmount }}</span>
            </div>
            <div class="order-actions">
              <el-button
                v-if="order.status === 0"
                type="primary"
                size="small"
                @click="payOrder(order)"
                :loading="actionLoading === order.id"
              >立即付款</el-button>
              <el-button
                v-if="order.status === 0"
                size="small"
                @click="cancelOrder(order)"
                :loading="actionLoading === order.id"
              >取消订单</el-button>
              <el-button
                v-if="order.status === 2"
                type="success"
                size="small"
                @click="confirmOrder(order)"
                :loading="actionLoading === order.id"
              >确认收货</el-button>
              <el-button
                size="small"
                @click="$router.push(`/orders/${order.id}`)"
              >查看详情</el-button>
            </div>
          </div>
        </div>
      </template>

      <el-empty v-else-if="!loading" description="暂无订单" />
    </div>

    <div class="pagination-wrap" v-if="total > 12">
      <el-pagination
        v-model:current-page="page"
        :page-size="10"
        :total="total"
        layout="prev, pager, next"
        @current-change="loadOrders"
        background
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { orderApi } from '@/api/order'
import { ElMessage, ElMessageBox } from 'element-plus'

const activeStatus = ref('')
const loading = ref(false)
const orders = ref([])
const total = ref(0)
const page = ref(1)
const actionLoading = ref(null)

onMounted(() => loadOrders())

function handleTabChange() {
  page.value = 1
  loadOrders()
}

async function loadOrders() {
  loading.value = true
  try {
    const params = { page: page.value, size: 10 }
    if (activeStatus.value !== '') params.status = Number(activeStatus.value)
    const res = await orderApi.list(params)
    orders.value = res.data.records
    total.value = Number(res.data.total)
  } finally {
    loading.value = false
  }
}

async function payOrder(order) {
  actionLoading.value = order.id
  try {
    await orderApi.pay(order.id)
    ElMessage.success('支付成功！')
    loadOrders()
  } finally {
    actionLoading.value = null
  }
}

async function cancelOrder(order) {
  await ElMessageBox.confirm('确定要取消该订单吗？', '提示', { type: 'warning' })
  actionLoading.value = order.id
  try {
    await orderApi.cancel(order.id)
    ElMessage.success('订单已取消')
    loadOrders()
  } finally {
    actionLoading.value = null
  }
}

async function confirmOrder(order) {
  await ElMessageBox.confirm('确认已收到商品？', '确认收货', { type: 'info' })
  actionLoading.value = order.id
  try {
    await orderApi.confirm(order.id)
    ElMessage.success('确认收货成功！')
    loadOrders()
  } finally {
    actionLoading.value = null
  }
}

function statusText(s) {
  return ['待付款', '待发货', '待收货', '已完成', '已取消'][s] || '未知'
}

function statusClass(s) {
  return ['pending', 'paid', 'shipped', 'completed', 'cancelled'][s] || ''
}

function formatDate(d) {
  if (!d) return ''
  return new Date(d).toLocaleString('zh-CN')
}
</script>

<style scoped>
.order-list-page {
  padding: 20px 15px;
}

.page-title {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 20px;
}

.order-tabs {
  margin-bottom: 16px;
  padding: 0 16px;
}

.order-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.order-card {
  padding: 0;
  overflow: hidden;
}

.order-header {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px 20px;
  background: #f9f9f9;
  border-bottom: 1px solid #f0f0f0;
  font-size: 13px;
}

.order-no {
  color: #666;
}

.order-time {
  color: #999;
}

.order-status {
  margin-left: auto;
  font-weight: bold;
  font-size: 14px;
}

.order-status.pending { color: var(--jd-red); }
.order-status.paid { color: var(--jd-orange); }
.order-status.shipped { color: #1890ff; }
.order-status.completed { color: #52c41a; }
.order-status.cancelled { color: #999; }

.order-items {
  padding: 16px 20px;
}

.order-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 8px 0;
  border-bottom: 1px solid #f5f5f5;
  cursor: pointer;
}

.order-item:last-child {
  border-bottom: none;
}

.order-item:hover .item-name {
  color: var(--jd-red);
}

.order-item img {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}

.item-info {
  flex: 1;
}

.item-name {
  font-size: 14px;
  color: #333;
}

.item-qty {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.item-price {
  color: var(--jd-red);
  font-weight: bold;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px;
  border-top: 1px solid #f0f0f0;
  background: #fafafa;
}

.order-amount {
  font-size: 14px;
  color: #666;
}

.amount {
  font-size: 18px;
  color: var(--jd-red);
  font-weight: bold;
}

.order-actions {
  display: flex;
  gap: 8px;
}

.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
</style>
