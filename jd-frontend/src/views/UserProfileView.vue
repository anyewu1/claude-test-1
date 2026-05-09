<template>
  <div class="profile-page page-container">
    <div class="profile-layout">
      <!-- Sidebar -->
      <aside class="profile-sidebar card">
        <div class="user-info">
          <div class="avatar-wrap">
            <img :src="userStore.avatar" :alt="userStore.username" class="avatar" />
          </div>
          <div class="username">{{ userStore.username }}</div>
          <el-tag>普通会员</el-tag>
        </div>
        <el-menu :default-active="activeMenu" @select="activeMenu = $event">
          <el-menu-item index="profile">
            <el-icon><User /></el-icon> 个人信息
          </el-menu-item>
          <el-menu-item index="orders" @click="$router.push('/orders')">
            <el-icon><List /></el-icon> 我的订单
          </el-menu-item>
          <el-menu-item index="addresses">
            <el-icon><Location /></el-icon> 收货地址
          </el-menu-item>
        </el-menu>
      </aside>

      <!-- Content -->
      <main class="profile-content">
        <!-- Profile info -->
        <div v-if="activeMenu === 'profile'" class="card section-card">
          <div class="section-title">个人信息</div>
          <el-form :model="profileForm" label-width="100px" style="max-width: 500px">
            <el-form-item label="用户名">
              <el-input :value="userStore.username" disabled />
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="profileForm.phone" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item label="头像URL">
              <el-input v-model="profileForm.avatar" placeholder="头像图片地址" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveProfile" :loading="saving">保存修改</el-button>
            </el-form-item>
          </el-form>
        </div>

        <!-- Addresses -->
        <div v-if="activeMenu === 'addresses'" class="card section-card">
          <div class="section-header">
            <div class="section-title">收货地址</div>
            <el-button type="primary" size="small" @click="showDialog = true">+ 新增地址</el-button>
          </div>
          <div class="address-list">
            <div class="address-item" v-for="addr in addresses" :key="addr.id">
              <div class="addr-top">
                <span class="addr-name">{{ addr.name }}</span>
                <span class="addr-phone">{{ addr.phone }}</span>
                <el-tag v-if="addr.isDefault" type="danger" size="small">默认</el-tag>
              </div>
              <div class="addr-detail">{{ addr.province }}{{ addr.city }}{{ addr.district }}{{ addr.detail }}</div>
              <div class="addr-actions">
                <el-button link @click="setDefault(addr.id)" v-if="!addr.isDefault">设为默认</el-button>
                <el-button link type="danger" @click="deleteAddr(addr.id)">删除</el-button>
              </div>
            </div>
            <el-empty v-if="!addresses.length" description="暂无地址" />
          </div>
        </div>
      </main>
    </div>

    <!-- Add Address Dialog -->
    <el-dialog v-model="showDialog" title="新增地址" width="480">
      <el-form :model="newAddr" label-width="80px">
        <el-form-item label="收货人"><el-input v-model="newAddr.name" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="newAddr.phone" /></el-form-item>
        <el-form-item label="省份"><el-input v-model="newAddr.province" /></el-form-item>
        <el-form-item label="城市"><el-input v-model="newAddr.city" /></el-form-item>
        <el-form-item label="区县"><el-input v-model="newAddr.district" /></el-form-item>
        <el-form-item label="详细地址"><el-input v-model="newAddr.detail" /></el-form-item>
        <el-form-item><el-checkbox v-model="newAddr.isDefault">设为默认</el-checkbox></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="addAddr">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/userStore'
import { addressApi } from '@/api/address'
import { authApi } from '@/api/auth'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const activeMenu = ref('profile')
const saving = ref(false)
const addresses = ref([])
const showDialog = ref(false)
const newAddr = ref({ name: '', phone: '', province: '', city: '', district: '', detail: '', isDefault: false })
const profileForm = ref({
  phone: userStore.userInfo?.phone || '',
  avatar: userStore.userInfo?.avatar || ''
})

onMounted(() => loadAddresses())

async function loadAddresses() {
  const res = await addressApi.list()
  addresses.value = res.data
}

async function saveProfile() {
  saving.value = true
  try {
    await authApi.updateProfile(profileForm.value)
    await userStore.fetchProfile()
    ElMessage.success('保存成功')
  } finally {
    saving.value = false
  }
}

async function addAddr() {
  if (!newAddr.value.name || !newAddr.value.phone) { ElMessage.warning('请填写必填项'); return }
  const res = await addressApi.add(newAddr.value)
  addresses.value.push(res.data)
  showDialog.value = false
  newAddr.value = { name: '', phone: '', province: '', city: '', district: '', detail: '', isDefault: false }
  ElMessage.success('添加成功')
}

async function setDefault(id) {
  await addressApi.setDefault(id)
  loadAddresses()
}

async function deleteAddr(id) {
  await addressApi.delete(id)
  addresses.value = addresses.value.filter(a => a.id !== id)
  ElMessage.success('已删除')
}
</script>

<style scoped>
.profile-page {
  padding: 20px 15px;
}

.profile-layout {
  display: flex;
  gap: 20px;
}

.profile-sidebar {
  width: 200px;
  flex-shrink: 0;
  padding: 20px 0;
}

.user-info {
  text-align: center;
  padding: 0 16px 20px;
  border-bottom: 1px solid #f0f0f0;
}

.avatar-wrap {
  margin-bottom: 12px;
}

.avatar {
  width: 70px;
  height: 70px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #f0f0f0;
}

.username {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 8px;
}

.profile-content {
  flex: 1;
}

.section-card {
  padding: 24px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
}

.address-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.address-item {
  border: 1px solid #e5e5e5;
  border-radius: 8px;
  padding: 14px;
}

.addr-top {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 6px;
}

.addr-name { font-weight: bold; }
.addr-phone { color: #666; }
.addr-detail { font-size: 13px; color: #666; margin-bottom: 8px; }
.addr-actions { display: flex; gap: 8px; }
</style>
