<template>
  <div class="admin-page page-container">
    <div class="admin-header">
      <h2>管理后台</h2>
      <el-tag type="danger">ADMIN</el-tag>
    </div>

    <el-tabs v-model="activeTab" type="border-card" class="admin-tabs">
      <!-- Products Tab -->
      <el-tab-pane label="商品管理" name="products">
        <div class="tab-toolbar">
          <el-input v-model="productKeyword" placeholder="搜索商品名称" style="width: 240px" clearable @clear="loadProducts" @keyup.enter="loadProducts" />
          <el-button type="primary" @click="loadProducts">搜索</el-button>
          <el-button type="success" @click="openProductDialog(null)">+ 新增商品</el-button>
          <el-button @click="rebuildEmbeddings" :loading="rebuildingEmbed">重建向量索引</el-button>
        </div>

        <el-table :data="products" v-loading="productsLoading" stripe>
          <el-table-column prop="id" label="ID" width="70" />
          <el-table-column label="图片" width="80">
            <template #default="{ row }">
              <img :src="row.coverImage" style="width:50px;height:50px;object-fit:cover;border-radius:4px" />
            </template>
          </el-table-column>
          <el-table-column prop="name" label="商品名称" min-width="160" show-overflow-tooltip />
          <el-table-column prop="price" label="售价" width="90">
            <template #default="{ row }">¥{{ row.price }}</template>
          </el-table-column>
          <el-table-column prop="stock" label="库存" width="80" />
          <el-table-column prop="sales" label="销量" width="80" />
          <el-table-column prop="status" label="状态" width="80">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
                {{ row.status === 1 ? '上架' : '下架' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="160" fixed="right">
            <template #default="{ row }">
              <el-button link type="primary" @click="openProductDialog(row)">编辑</el-button>
              <el-button link type="danger" @click="deleteProduct(row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-wrap">
          <el-pagination v-model:current-page="productPage" :page-size="20" :total="productTotal"
            layout="total, prev, pager, next" @current-change="loadProducts" />
        </div>
      </el-tab-pane>

      <!-- Orders Tab -->
      <el-tab-pane label="订单管理" name="orders">
        <div class="tab-toolbar">
          <el-select v-model="orderStatusFilter" placeholder="全部状态" clearable style="width:140px" @change="loadOrders">
            <el-option label="待付款" :value="0" />
            <el-option label="待发货" :value="1" />
            <el-option label="待收货" :value="2" />
            <el-option label="已完成" :value="3" />
            <el-option label="已取消" :value="4" />
            <el-option label="退款中" :value="5" />
          </el-select>
        </div>

        <el-table :data="orders" v-loading="ordersLoading" stripe>
          <el-table-column prop="orderNo" label="订单号" width="220" />
          <el-table-column prop="userId" label="用户ID" width="80" />
          <el-table-column prop="actualAmount" label="实付金额" width="100">
            <template #default="{ row }">¥{{ row.actualAmount }}</template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="orderStatusType(row.status)" size="small">{{ orderStatusLabel(row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createdAt" label="下单时间" width="170">
            <template #default="{ row }">{{ formatDate(row.createdAt) }}</template>
          </el-table-column>
          <el-table-column label="操作" width="100" fixed="right">
            <template #default="{ row }">
              <el-button v-if="row.status === 1" link type="primary" @click="shipOrder(row.id)">发货</el-button>
              <span v-else class="no-action">—</span>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-wrap">
          <el-pagination v-model:current-page="orderPage" :page-size="20" :total="orderTotal"
            layout="total, prev, pager, next" @current-change="loadOrders" />
        </div>
      </el-tab-pane>

      <!-- Coupons Tab -->
      <el-tab-pane label="优惠券管理" name="coupons">
        <div class="tab-toolbar">
          <el-button type="success" @click="openCouponDialog">+ 新增优惠券</el-button>
        </div>

        <el-table :data="coupons" v-loading="couponsLoading" stripe>
          <el-table-column prop="code" label="优惠码" width="140" />
          <el-table-column prop="type" label="类型" width="90">
            <template #default="{ row }">
              <el-tag size="small">{{ row.type === 'FIXED' ? '满减' : '折扣' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="优惠" width="110">
            <template #default="{ row }">
              {{ row.type === 'FIXED' ? `减¥${row.value}` : `${row.value}%折扣` }}
            </template>
          </el-table-column>
          <el-table-column label="门槛" width="110">
            <template #default="{ row }">满¥{{ row.minOrder }}</template>
          </el-table-column>
          <el-table-column label="使用情况" width="110">
            <template #default="{ row }">{{ row.usedCount }} / {{ row.maxUses }}</template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template #default="{ row }">
              <el-button link type="danger" @click="deleteCoupon(row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- Users Tab -->
      <el-tab-pane label="用户管理" name="users">
        <el-table :data="users" v-loading="usersLoading" stripe>
          <el-table-column prop="id" label="ID" width="70" />
          <el-table-column prop="username" label="用户名" width="140" />
          <el-table-column prop="email" label="邮箱" min-width="160" />
          <el-table-column prop="phone" label="手机" width="130" />
          <el-table-column prop="role" label="角色" width="90">
            <template #default="{ row }">
              <el-tag :type="row.role === 'ADMIN' ? 'danger' : 'info'" size="small">{{ row.role }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createdAt" label="注册时间" width="170">
            <template #default="{ row }">{{ formatDate(row.createdAt) }}</template>
          </el-table-column>
        </el-table>
        <div class="pagination-wrap">
          <el-pagination v-model:current-page="userPage" :page-size="20" :total="userTotal"
            layout="total, prev, pager, next" @current-change="loadUsers" />
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- Product Dialog -->
    <el-dialog v-model="productDialogVisible" :title="editingProduct?.id ? '编辑商品' : '新增商品'" width="640px">
      <el-form :model="productForm" label-width="90px">
        <el-form-item label="商品名称" required>
          <el-input v-model="productForm.name" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="productForm.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="售价" required>
              <el-input-number v-model="productForm.price" :precision="2" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="原价">
              <el-input-number v-model="productForm.originalPrice" :precision="2" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="库存" required>
              <el-input-number v-model="productForm.stock" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类ID">
              <el-input-number v-model="productForm.categoryId" :min="1" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="封面图URL">
          <el-input v-model="productForm.coverImage" />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="闪购">
              <el-switch v-model="productForm.isFlashSale" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上架状态">
              <el-switch v-model="productForm.status" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="productDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveProduct" :loading="savingProduct">保存</el-button>
      </template>
    </el-dialog>

    <!-- Coupon Dialog -->
    <el-dialog v-model="couponDialogVisible" title="新增优惠券" width="480px">
      <el-form :model="couponForm" label-width="90px">
        <el-form-item label="优惠码" required>
          <el-input v-model="couponForm.code" placeholder="如 SAVE20" />
        </el-form-item>
        <el-form-item label="类型" required>
          <el-select v-model="couponForm.type" style="width:100%">
            <el-option label="满减 (FIXED)" value="FIXED" />
            <el-option label="折扣 (PERCENT)" value="PERCENT" />
          </el-select>
        </el-form-item>
        <el-form-item label="优惠值" required>
          <el-input-number v-model="couponForm.value" :precision="2" :min="0" style="width:100%" />
          <div class="form-tip">{{ couponForm.type === 'FIXED' ? '减去的金额（元）' : '折扣百分比（如10表示减10%）' }}</div>
        </el-form-item>
        <el-form-item label="最低消费">
          <el-input-number v-model="couponForm.minOrder" :precision="2" :min="0" style="width:100%" />
        </el-form-item>
        <el-form-item label="最大使用量">
          <el-input-number v-model="couponForm.maxUses" :min="1" style="width:100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="couponDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveCoupon" :loading="savingCoupon">创建</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { adminApi } from '@/api/admin'
import { ElMessage, ElMessageBox } from 'element-plus'

const activeTab = ref('products')

// Products
const products = ref([])
const productsLoading = ref(false)
const productKeyword = ref('')
const productPage = ref(1)
const productTotal = ref(0)
const productDialogVisible = ref(false)
const editingProduct = ref(null)
const savingProduct = ref(false)
const rebuildingEmbed = ref(false)
const productForm = ref({})

// Orders
const orders = ref([])
const ordersLoading = ref(false)
const orderPage = ref(1)
const orderTotal = ref(0)
const orderStatusFilter = ref(null)

// Coupons
const coupons = ref([])
const couponsLoading = ref(false)
const couponDialogVisible = ref(false)
const savingCoupon = ref(false)
const couponForm = ref({ code: '', type: 'FIXED', value: 10, minOrder: 0, maxUses: 100 })

// Users
const users = ref([])
const usersLoading = ref(false)
const userPage = ref(1)
const userTotal = ref(0)

onMounted(() => loadProducts())

watch(activeTab, (tab) => {
  if (tab === 'orders' && orders.value.length === 0) loadOrders()
  if (tab === 'coupons' && coupons.value.length === 0) loadCoupons()
  if (tab === 'users' && users.value.length === 0) loadUsers()
})

async function loadProducts() {
  productsLoading.value = true
  try {
    const res = await adminApi.listProducts({ page: productPage.value, size: 20, keyword: productKeyword.value || undefined })
    products.value = res.data.records
    productTotal.value = Number(res.data.total)
  } finally { productsLoading.value = false }
}

async function loadOrders() {
  ordersLoading.value = true
  try {
    const params = { page: orderPage.value, size: 20 }
    if (orderStatusFilter.value !== null && orderStatusFilter.value !== undefined) params.status = orderStatusFilter.value
    const res = await adminApi.listOrders(params)
    orders.value = res.data.records
    orderTotal.value = Number(res.data.total)
  } finally { ordersLoading.value = false }
}

async function loadCoupons() {
  couponsLoading.value = true
  try {
    const res = await adminApi.listCoupons()
    coupons.value = res.data
  } finally { couponsLoading.value = false }
}

async function loadUsers() {
  usersLoading.value = true
  try {
    const res = await adminApi.listUsers({ page: userPage.value, size: 20 })
    users.value = res.data.records
    userTotal.value = Number(res.data.total)
  } finally { usersLoading.value = false }
}

function openProductDialog(product) {
  editingProduct.value = product
  productForm.value = product
    ? { ...product }
    : { name: '', description: '', price: 0, originalPrice: null, stock: 100, categoryId: 1, coverImage: '', isFlashSale: false, status: 1 }
  productDialogVisible.value = true
}

async function saveProduct() {
  if (!productForm.value.name || !productForm.value.price) {
    ElMessage.warning('请填写商品名称和售价')
    return
  }
  savingProduct.value = true
  try {
    if (editingProduct.value?.id) {
      await adminApi.updateProduct(editingProduct.value.id, productForm.value)
      ElMessage.success('商品已更新')
    } else {
      await adminApi.createProduct(productForm.value)
      ElMessage.success('商品已创建')
    }
    productDialogVisible.value = false
    loadProducts()
  } finally { savingProduct.value = false }
}

async function deleteProduct(id) {
  await ElMessageBox.confirm('确定要删除该商品吗？', '确认删除', { type: 'warning' })
  await adminApi.deleteProduct(id)
  ElMessage.success('已删除')
  loadProducts()
}

async function shipOrder(id) {
  await adminApi.shipOrder(id)
  ElMessage.success('已标记发货')
  loadOrders()
}

async function rebuildEmbeddings() {
  rebuildingEmbed.value = true
  try {
    await adminApi.rebuildEmbeddings()
    ElMessage.success('向量索引重建完成')
  } catch (e) {
    ElMessage.warning(e.message || '向量模型未配置')
  } finally { rebuildingEmbed.value = false }
}

function openCouponDialog() {
  couponForm.value = { code: '', type: 'FIXED', value: 10, minOrder: 0, maxUses: 100 }
  couponDialogVisible.value = true
}

async function saveCoupon() {
  if (!couponForm.value.code) { ElMessage.warning('请填写优惠码'); return }
  savingCoupon.value = true
  try {
    await adminApi.createCoupon(couponForm.value)
    ElMessage.success('优惠券已创建')
    couponDialogVisible.value = false
    loadCoupons()
  } finally { savingCoupon.value = false }
}

async function deleteCoupon(id) {
  await ElMessageBox.confirm('确定要删除该优惠券吗？', '确认', { type: 'warning' })
  await adminApi.deleteCoupon(id)
  ElMessage.success('已删除')
  loadCoupons()
}

function orderStatusLabel(s) {
  return ['待付款', '待发货', '待收货', '已完成', '已取消', '退款中'][s] ?? '未知'
}

function orderStatusType(s) {
  return [null, 'warning', 'primary', 'success', 'info', 'danger'][s] ?? 'info'
}

function formatDate(d) {
  if (!d) return '-'
  return new Date(d).toLocaleString('zh-CN')
}
</script>

<style scoped>
.admin-page { padding: 20px 15px; }

.admin-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
}

.admin-tabs { border-radius: 8px; }

.tab-toolbar {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.pagination-wrap {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

.no-action { color: #ccc; font-size: 13px; }

.form-tip { font-size: 12px; color: #999; margin-top: 4px; }
</style>
