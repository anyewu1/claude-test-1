<template>
  <div class="not-found-page page-container">
    <div class="not-found-content">
      <div class="error-code">404</div>
      <div class="error-title">页面不存在</div>
      <p class="error-desc">您访问的页面已删除或地址有误，请检查后重试</p>
      <div class="action-group">
        <el-button type="primary" size="large" @click="$router.push('/')">返回首页</el-button>
        <el-button size="large" @click="$router.back()">返回上一页</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.not-found-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
  padding: 40px 15px;
}

.not-found-content {
  text-align: center;
}

.error-code {
  font-size: 120px;
  font-weight: bold;
  color: var(--jd-red);
  line-height: 1;
  margin-bottom: 16px;
}

.error-title {
  font-size: 28px;
  color: #333;
  margin-bottom: 12px;
}

.error-desc {
  font-size: 15px;
  color: #999;
  margin-bottom: 32px;
}

.action-group {
  display: flex;
  gap: 16px;
  justify-content: center;
}
</style>
