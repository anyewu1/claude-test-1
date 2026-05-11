import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/userStore'

const routes = [
  { path: '/', component: () => import('@/views/HomeView.vue') },
  { path: '/products', component: () => import('@/views/ProductListView.vue') },
  { path: '/products/:id', component: () => import('@/views/ProductDetailView.vue') },
  { path: '/search', component: () => import('@/views/ProductListView.vue') },
  { path: '/cart', component: () => import('@/views/CartView.vue'), meta: { requiresAuth: true } },
  { path: '/checkout', component: () => import('@/views/CheckoutView.vue'), meta: { requiresAuth: true } },
  { path: '/orders', component: () => import('@/views/OrderListView.vue'), meta: { requiresAuth: true } },
  { path: '/orders/:id', component: () => import('@/views/OrderDetailView.vue'), meta: { requiresAuth: true } },
  { path: '/login', component: () => import('@/views/LoginView.vue') },
  { path: '/register', component: () => import('@/views/RegisterView.vue') },
  { path: '/profile', component: () => import('@/views/UserProfileView.vue'), meta: { requiresAuth: true } },
  { path: '/favorites', component: () => import('@/views/FavoritesView.vue'), meta: { requiresAuth: true } },
  { path: '/admin', component: () => import('@/views/AdminView.vue'), meta: { requiresAuth: true, requiresAdmin: true } },
  { path: '/:pathMatch(.*)*', name: 'NotFound', component: () => import('@/views/NotFoundView.vue') }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior: () => ({ top: 0 })
})

router.beforeEach((to) => {
  const userStore = useUserStore()
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }
  if (to.meta.requiresAdmin && !userStore.isAdmin) {
    return { path: '/' }
  }
})

export default router
