import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { cartApi } from '@/api/cart'
import { ElMessage } from 'element-plus'
import { useUserStore } from './userStore'

export const useCartStore = defineStore('cart', () => {
  const items = ref([])
  const totalCount = ref(0)
  const selectedCount = ref(0)
  const totalAmount = ref(0)

  const cartCount = computed(() => totalCount.value)

  async function fetchCart() {
    const userStore = useUserStore()
    if (!userStore.isLoggedIn) return
    const res = await cartApi.getCart()
    items.value = res.data.items
    totalCount.value = res.data.totalCount
    selectedCount.value = res.data.selectedCount
    totalAmount.value = res.data.totalAmount
  }

  async function addToCart(productId, quantity = 1) {
    await cartApi.addItem({ productId, quantity })
    ElMessage.success('已加入购物车')
    await fetchCart()
  }

  async function updateItem(id, data) {
    await cartApi.updateItem(id, data)
    await fetchCart()
  }

  async function removeItem(id) {
    await cartApi.removeItem(id)
    await fetchCart()
  }

  async function selectAll(selected) {
    await cartApi.selectAll(selected)
    await fetchCart()
  }

  async function clearCart() {
    await cartApi.clearCart()
    items.value = []
    totalCount.value = 0
    selectedCount.value = 0
    totalAmount.value = 0
  }

  return {
    items, totalCount, selectedCount, totalAmount, cartCount,
    fetchCart, addToCart, updateItem, removeItem, selectAll, clearCart
  }
})
