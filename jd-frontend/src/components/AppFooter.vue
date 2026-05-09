<template>
  <footer class="app-footer">
    <div class="footer-services">
      <div class="page-container services-bar">
        <div class="service-item" v-for="s in services" :key="s.icon">
          <span class="s-icon">{{ s.icon }}</span>
          <div>
            <div class="s-title">{{ s.title }}</div>
            <div class="s-desc">{{ s.desc }}</div>
          </div>
        </div>
      </div>
    </div>
    <div class="footer-main">
      <div class="page-container footer-content">
        <div class="footer-col">
          <h4>购物指南</h4>
          <a href="#">购物流程</a>
          <a href="#">会员介绍</a>
          <a href="#">生活旅行</a>
          <a href="#">常见问题</a>
        </div>
        <div class="footer-col">
          <h4>配送方式</h4>
          <a href="#">上门自提</a>
          <a href="#">211限时达</a>
          <a href="#">配送服务查询</a>
          <a href="#">配送费收取标准</a>
        </div>
        <div class="footer-col">
          <h4>支付方式</h4>
          <a href="#">货到付款</a>
          <a href="#">在线支付</a>
          <a href="#">分期付款</a>
          <a href="#">邮局汇款</a>
        </div>
        <div class="footer-col">
          <h4>售后服务</h4>
          <a href="#">售后政策</a>
          <a href="#">价格保护</a>
          <a href="#">退款说明</a>
          <a href="#">返修/换货流程</a>
        </div>
        <div class="footer-col">
          <h4>特色服务</h4>
          <a href="#">夺宝岛</a>
          <a href="#">闪购</a>
          <a href="#">团购</a>
          <a href="#">企业采购</a>
        </div>
      </div>
    </div>
    <div class="footer-bottom">
      <div class="page-container">
        <p>© 2024 京东电商平台 All Rights Reserved</p>
        <p>京ICP备17264001号 | 增值电信业务经营许可证</p>
      </div>
    </div>
  </footer>
</template>

<script setup>
const services = [
  { icon: '🚚', title: '正品保障', desc: '正品行货，提供发票' },
  { icon: '⚡', title: '极速配送', desc: '211限时达，隔日达' },
  { icon: '💰', title: '轻松退换', desc: '7天退货，30天换货' },
  { icon: '🛡️', title: '安全支付', desc: '银行级别安全支付' },
  { icon: '🎁', title: '会员积分', desc: '购物得积分，积分抵现' }
]
</script>

<style scoped>
.app-footer {
  background: #fff;
  margin-top: 30px;
}

.footer-services {
  background: #f5f5f5;
  border-top: 1px solid #e5e5e5;
  padding: 20px 0;
}

.services-bar {
  display: flex;
  justify-content: space-around;
}

.service-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.s-icon {
  font-size: 28px;
}

.s-title {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.s-desc {
  font-size: 12px;
  color: #999;
}

.footer-main {
  padding: 30px 0;
  border-top: 1px solid #e5e5e5;
}

.footer-content {
  display: flex;
  gap: 40px;
}

.footer-col {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.footer-col h4 {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.footer-col a {
  font-size: 12px;
  color: #666;
}

.footer-col a:hover {
  color: var(--jd-red);
}

.footer-bottom {
  background: #e5e5e5;
  padding: 15px 0;
  text-align: center;
}

.footer-bottom p {
  font-size: 12px;
  color: #999;
  line-height: 1.8;
}
</style>
