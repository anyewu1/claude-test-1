<template>
  <div id="app">
    <AppHeader v-if="showHeader" />
    <main class="main-content">
      <RouterView />
    </main>
    <AppFooter v-if="showHeader" />
    <AIChat />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import AIChat from '@/components/AIChat.vue'

const route = useRoute()
const noHeaderRoutes = ['/login', '/register']
const showHeader = computed(() => !noHeaderRoutes.includes(route.path))
</script>

<style>
#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  padding-top: 0;
}
</style>
