<template>
  <div class="order-detail-page page-container" v-loading="loading">
    <div class="back-link" @click="$router.push('/orders')">
      <el-icon><ArrowLeft /></el-icon> 返回我的订单
    </div>

    <div v-if="order" class="order-content">
      <div class="status-card card">
        <div class="status-icon">{{ statusIcon }}</div>
        <div>
          <div class="status-text">{{ statusText }}</div>
          <div class="status-desc">{{ statusDesc }}</div>
        </div>
        <div class="status-actions">
          <el-button v-if="order.status === 0" type="primary" @click="payOrder" :loading="actionLoading">
            立即付款
          </el-button>
          <el-button v-if="order.status === 0" @click="cancelOrder" :loading="actionLoading">
            取消订单
          </el-button>
          <el-button v-if="order.status === 1 || order.status === 2" type="success" @click="confirmOrder" :loading="actionLoading">
            确认收货
          </el-button>
          <el-button v-if="order.status === 3" type="warning" @click="requestRefund" :loading="actionLoading">
            申请退款
          </el-button>
        </div>
      </div>

      <!-- Progress steps -->
      <div class="card progress-card">
        <el-steps :active="stepActive" finish-status="success" align-center>
          <el-step title="提交订单" :description="formatDate(order.createdAt)" />
          <el-step title="付款成功" :description="formatDate(order.paidAt)" />
          <el-step title="商品出库" :description="formatDate(order.shippedAt)" />
          <el-step title="确认收货" :description="formatDate(order.completedAt)" />
        </el-steps>
      </div>

      <!-- Address -->
      <div class="card section-card">
        <div class="section-title">收货信息</div>
        <div class="address-info">{{ order.addressSnapshot }}</div>
      </div>

      <!-- Items -->
      <div class="card section-card">
        <div class="section-title">商品清单</div>
        <div class="order-item" v-for="item in order.items" :key="item.id">
          <img :src="item.productImage" :alt="item.productName" />
          <div class="item-main">
            <div class="item-name">{{ item.productName }}</div>
          </div>
          <div class="item-meta">
            <div class="item-price">¥{{ item.price }}</div>
            <div class="item-qty">× {{ item.quantity }}</div>
          </div>
          <div class="item-total">¥{{ (item.price * item.quantity).toFixed(2) }}</div>
        </div>
      </div>

      <!-- Summary -->
      <div class="card section-card amount-card">
        <div class="amount-row">
          <span>商品总价</span>
          <span>¥{{ order.totalAmount }}</span>
        </div>
        <div class="amount-row">
          <span>运费</span>
          <span class="free">免运费</span>
        </div>
        <div class="amount-row total">
          <span>实付金额</span>
          <span class="final">¥{{ order.actualAmount }}</span>
        </div>
      </div>

      <!-- Order info -->
      <div class="card section-card order-info">
        <div class="info-row"><span>订单编号</span><span>{{ order.orderNo }}</span></div>
        <div class="info-row"><span>下单时间</span><span>{{ formatDate(order.createdAt) }}</span></div>
        <div v-if="order.remark" class="info-row"><span>备注</span><span>{{ order.remark }}</span></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { orderApi } from '@/api/order'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const actionLoading = ref(false)
const order = ref(null)

const stepActive = computed(() => {
  const s = order.value?.status
  if (s === 0) return 1
  if (s === 1) return 2
  if (s === 2) return 3
  if (s >= 3) return 4
  return 0
})

const statusText = computed(() => ['待付款', '待发货', '待收货', '已完成', '已取消', '退款中'][order.value?.status] || '')
const statusIcon = computed(() => ['⏳', '📦', '🚚', '✅', '❌', '🔄'][order.value?.status] || '')
const statusDesc = computed(() => {
  const s = order.value?.status
  if (s === 0) return '请在24小时内完成付款，超时订单将自动取消'
  if (s === 1) return '商家正在备货，即将发出'
  if (s === 2) return '商品已发出，请耐心等待'
  if (s === 3) return '感谢您的购买，欢迎再次光临！'
  if (s === 4) return '订单已取消'
  if (s === 5) return '退款申请已提交，预计1-3个工作日处理'
  return ''
})

onMounted(loadOrder)

async function loadOrder() {
  loading.value = true
  try {
    const res = await orderApi.detail(route.params.id)
    order.value = res.data
  } finally {
    loading.value = false
  }
}

async function payOrder() {
  actionLoading.value = true
  try {
    await orderApi.pay(order.value.id)
    ElMessage.success('支付成功！')
    loadOrder()
  } finally {
    actionLoading.value = false
  }
}

async function cancelOrder() {
  await ElMessageBox.confirm('确定要取消该订单吗？', '提示', { type: 'warning' })
  actionLoading.value = true
  try {
    await orderApi.cancel(order.value.id)
    ElMessage.success('订单已取消')
    loadOrder()
  } finally {
    actionLoading.value = false
  }
}

async function confirmOrder() {
  await ElMessageBox.confirm('确认已收到商品？', '确认收货', { type: 'info' })
  actionLoading.value = true
  try {
    await orderApi.confirm(order.value.id)
    ElMessage.success('确认收货成功！')
    loadOrder()
  } finally {
    actionLoading.value = false
  }
}

async function requestRefund() {
  await ElMessageBox.confirm('确认申请退款？退款将在1-3个工作日内处理。', '申请退款', { type: 'warning' })
  actionLoading.value = true
  try {
    await orderApi.refund(order.value.id)
    ElMessage.success('退款申请已提交')
    loadOrder()
  } finally {
    actionLoading.value = false
  }
}

function formatDate(d) {
  if (!d) return '-'
  return new Date(d).toLocaleString('zh-CN')
}
</script>

<style scoped>
.order-detail-page {
  padding: 20px 15px;
  max-width: 900px;
}

.back-link {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #666;
  cursor: pointer;
  margin-bottom: 20px;
  font-size: 14px;
}

.back-link:hover { color: var(--jd-red); }

.order-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.status-card {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 24px;
}

.status-icon {
  font-size: 40px;
}

.status-text {
  font-size: 20px;
  font-weight: bold;
  color: var(--jd-red);
  margin-bottom: 6px;
}

.status-desc {
  font-size: 13px;
  color: #999;
}

.status-actions {
  margin-left: auto;
  display: flex;
  gap: 8px;
}

.progress-card {
  padding: 24px 40px;
}

.section-card {
  padding: 20px 24px;
}

.section-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid #f0f0f0;
}

.address-info {
  font-size: 15px;
  color: #555;
  line-height: 1.6;
}

.order-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f5;
}

.order-item:last-child { border-bottom: none; }

.order-item img {
  width: 70px;
  height: 70px;
  object-fit: cover;
  border-radius: 4px;
}

.item-main { flex: 1; }
.item-name { font-size: 14px; color: #333; }

.item-meta {
  text-align: right;
}

.item-price {
  color: var(--jd-red);
  font-weight: bold;
}

.item-qty {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.item-total {
  width: 100px;
  text-align: right;
  font-size: 16px;
  font-weight: bold;
  color: var(--jd-red);
}

.amount-card {}

.amount-row {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  font-size: 14px;
  color: #666;
}

.free { color: var(--jd-red); }

.amount-row.total {
  padding-top: 12px;
  border-top: 1px dashed #e5e5e5;
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.final {
  font-size: 22px;
  color: var(--jd-red);
}

.order-info .info-row {
  display: flex;
  gap: 20px;
  padding: 8px 0;
  font-size: 14px;
  border-bottom: 1px solid #f5f5f5;
}

.order-info .info-row:last-child { border-bottom: none; }

.order-info .info-row span:first-child {
  width: 80px;
  color: #999;
}
</style>
