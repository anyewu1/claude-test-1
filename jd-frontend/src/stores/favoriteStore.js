import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { favoriteApi } from '@/api/favorite'
import { ElMessage } from 'element-plus'
import { useUserStore } from './userStore'

export const useFavoriteStore = defineStore('favorite', () => {
  const items = ref([])
  const productIds = ref(new Set())

  const count = computed(() => items.value.length)

  function isFavorited(productId) {
    return productIds.value.has(Number(productId))
  }

  async function fetchFavorites() {
    const userStore = useUserStore()
    if (!userStore.isLoggedIn) return
    const res = await favoriteApi.list()
    items.value = res.data
    productIds.value = new Set(res.data.map(f => f.productId))
  }

  async function toggle(productId) {
    const userStore = useUserStore()
    if (!userStore.isLoggedIn) return false

    if (isFavorited(productId)) {
      await favoriteApi.remove(productId)
      items.value = items.value.filter(f => f.productId !== Number(productId))
      productIds.value.delete(Number(productId))
      ElMessage.success('已取消收藏')
    } else {
      await favoriteApi.add(productId)
      productIds.value.add(Number(productId))
      ElMessage.success('已加入收藏')
    }
    return true
  }

  async function remove(productId) {
    await favoriteApi.remove(productId)
    items.value = items.value.filter(f => f.productId !== Number(productId))
    productIds.value.delete(Number(productId))
    ElMessage.success('已取消收藏')
  }

  return { items, count, isFavorited, fetchFavorites, toggle, remove }
})
