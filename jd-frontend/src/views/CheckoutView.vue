<template>
  <div class="checkout-page page-container">
    <div class="page-title">确认订单</div>

    <div class="checkout-layout">
      <div class="checkout-main">
        <!-- Address section -->
        <div class="section-card card">
          <div class="section-header">
            <el-icon><Location /></el-icon>
            <span>收货地址</span>
            <el-button link @click="showAddressDialog = true" style="margin-left: auto">
              + 新增地址
            </el-button>
          </div>
          <div v-if="addresses.length" class="address-list">
            <div
              v-for="addr in addresses"
              :key="addr.id"
              class="address-item"
              :class="{ selected: selectedAddressId === addr.id }"
              @click="selectedAddressId = addr.id"
            >
              <div class="addr-main">
                <span class="addr-name">{{ addr.name }}</span>
                <span class="addr-phone">{{ addr.phone }}</span>
                <el-tag v-if="addr.isDefault" type="danger" size="small">默认</el-tag>
              </div>
              <div class="addr-detail">{{ addr.province }}{{ addr.city }}{{ addr.district }}{{ addr.detail }}</div>
            </div>
          </div>
          <el-empty v-else description="暂无收货地址，请先添加" />
        </div>

        <!-- Order items -->
        <div class="section-card card">
          <div class="section-header">
            <el-icon><ShoppingBag /></el-icon>
            <span>商品清单</span>
          </div>
          <div class="order-item" v-for="item in selectedItems" :key="item.id">
            <img :src="item.productImage" :alt="item.productName" class="order-img" />
            <div class="order-item-info">
              <div class="order-item-name">{{ item.productName }}</div>
              <div class="order-item-price">¥{{ item.price }} × {{ item.quantity }}</div>
            </div>
            <div class="order-item-total">¥{{ (item.price * item.quantity).toFixed(2) }}</div>
          </div>
        </div>

        <!-- Remark -->
        <div class="section-card card">
          <div class="section-header">
            <el-icon><ChatDotRound /></el-icon>
            <span>订单备注</span>
          </div>
          <el-input
            v-model="remark"
            type="textarea"
            placeholder="可在此填写特殊要求（选填）"
            :rows="3"
            maxlength="200"
            show-word-limit
          />
        </div>
      </div>

      <!-- Summary -->
      <div class="checkout-summary card">
        <h3>订单汇总</h3>
        <div class="summary-row">
          <span>商品总价</span>
          <span>¥{{ totalAmount.toFixed(2) }}</span>
        </div>
        <div class="summary-row">
          <span>运费</span>
          <span class="free-ship">免运费</span>
        </div>
        <div class="summary-row discount-row">
          <span>优惠</span>
          <span>-¥0.00</span>
        </div>
        <el-divider />
        <div class="total-row">
          <span>应付合计</span>
          <span class="final-price">¥{{ totalAmount.toFixed(2) }}</span>
        </div>
        <div class="address-tip" v-if="selectedAddress">
          配送至：{{ selectedAddress.province }}{{ selectedAddress.city }}{{ selectedAddress.district }}
        </div>
        <el-button
          type="primary"
          size="large"
          class="submit-btn"
          :loading="submitting"
          :disabled="!selectedAddressId || selectedItems.length === 0"
          @click="submitOrder"
        >
          提交订单
        </el-button>
      </div>
    </div>

    <!-- Add Address Dialog -->
    <el-dialog v-model="showAddressDialog" title="新增收货地址" width="500">
      <el-form :model="newAddress" label-width="80px">
        <el-form-item label="收货人">
          <el-input v-model="newAddress.name" placeholder="请输入收货人姓名" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="newAddress.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="省份">
          <el-input v-model="newAddress.province" placeholder="如：广东省" />
        </el-form-item>
        <el-form-item label="城市">
          <el-input v-model="newAddress.city" placeholder="如：深圳市" />
        </el-form-item>
        <el-form-item label="区县">
          <el-input v-model="newAddress.district" placeholder="如：南山区" />
        </el-form-item>
        <el-form-item label="详细地址">
          <el-input v-model="newAddress.detail" placeholder="街道、小区、门牌号" />
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="newAddress.isDefault">设为默认地址</el-checkbox>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddressDialog = false">取消</el-button>
        <el-button type="primary" @click="addAddress">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cartStore'
import { addressApi } from '@/api/address'
import { orderApi } from '@/api/order'
import { ElMessage } from 'element-plus'

const router = useRouter()
const cartStore = useCartStore()

const addresses = ref([])
const selectedAddressId = ref(null)
const remark = ref('')
const submitting = ref(false)
const showAddressDialog = ref(false)
const newAddress = ref({ name: '', phone: '', province: '', city: '', district: '', detail: '', isDefault: false })

const selectedItems = computed(() => cartStore.items.filter(i => i.selected))
const totalAmount = computed(() => cartStore.totalAmount)
const selectedAddress = computed(() => addresses.value.find(a => a.id === selectedAddressId.value))

onMounted(async () => {
  await cartStore.fetchCart()
  if (selectedItems.value.length === 0) {
    ElMessage.warning('请先选择商品')
    router.push('/cart')
    return
  }
  const res = await addressApi.list()
  addresses.value = res.data
  const def = addresses.value.find(a => a.isDefault)
  if (def) selectedAddressId.value = def.id
  else if (addresses.value.length) selectedAddressId.value = addresses.value[0].id
})

async function addAddress() {
  if (!newAddress.value.name || !newAddress.value.phone) {
    ElMessage.warning('请填写收货人和手机号')
    return
  }
  const res = await addressApi.add(newAddress.value)
  addresses.value.push(res.data)
  selectedAddressId.value = res.data.id
  showAddressDialog.value = false
  newAddress.value = { name: '', phone: '', province: '', city: '', district: '', detail: '', isDefault: false }
  ElMessage.success('地址添加成功')
}

async function submitOrder() {
  if (!selectedAddressId.value) {
    ElMessage.warning('请选择收货地址')
    return
  }
  submitting.value = true
  try {
    const cartItemIds = selectedItems.value.map(i => i.id)
    const res = await orderApi.create({ addressId: selectedAddressId.value, cartItemIds, remark: remark.value })
    ElMessage.success('下单成功！')
    await cartStore.fetchCart()
    router.push(`/orders/${res.data.id}`)
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.checkout-page {
  padding: 20px 15px;
}

.page-title {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 20px;
}

.checkout-layout {
  display: flex;
  gap: 20px;
  align-items: flex-start;
}

.checkout-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.section-card {
  padding: 20px;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 16px;
}

.address-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.address-item {
  border: 2px solid #e5e5e5;
  border-radius: 8px;
  padding: 14px;
  cursor: pointer;
  transition: border-color 0.2s;
}

.address-item:hover {
  border-color: var(--jd-red);
}

.address-item.selected {
  border-color: var(--jd-red);
  background: #fff5f5;
}

.addr-main {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 6px;
}

.addr-name {
  font-weight: bold;
}

.addr-phone {
  color: #666;
}

.addr-detail {
  font-size: 13px;
  color: #666;
}

.order-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.order-item:last-child {
  border-bottom: none;
}

.order-img {
  width: 70px;
  height: 70px;
  object-fit: cover;
  border-radius: 4px;
}

.order-item-info {
  flex: 1;
}

.order-item-name {
  font-size: 14px;
  color: #333;
  margin-bottom: 6px;
}

.order-item-price {
  font-size: 13px;
  color: #999;
}

.order-item-total {
  color: var(--jd-red);
  font-weight: bold;
  font-size: 16px;
}

.checkout-summary {
  width: 280px;
  flex-shrink: 0;
  padding: 20px;
  position: sticky;
  top: 90px;
}

.checkout-summary h3 {
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

.free-ship {
  color: var(--jd-red);
}

.discount-row {
  color: #999;
}

.total-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.final-price {
  font-size: 24px;
  color: var(--jd-red);
  font-weight: bold;
}

.address-tip {
  font-size: 12px;
  color: #999;
  margin-bottom: 12px;
}

.submit-btn {
  width: 100%;
  background: var(--jd-red);
  border-color: var(--jd-red);
  font-size: 16px;
  height: 44px;
}
</style>
