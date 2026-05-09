<template>
  <div class="ai-chat-widget">
    <!-- Float button -->
    <div class="chat-fab" @click="toggleChat" :class="{ active: isOpen }">
      <span v-if="!isOpen">🤖</span>
      <el-icon v-else><Close /></el-icon>
      <span class="fab-label">AI客服</span>
    </div>

    <!-- Chat window -->
    <Transition name="chat-slide">
      <div v-if="isOpen" class="chat-window">
        <div class="chat-header">
          <div class="ai-avatar">🤖</div>
          <div>
            <div class="ai-name">京小智</div>
            <div class="ai-status">AI智能客服 · 在线</div>
          </div>
          <el-button link @click="isOpen = false" class="close-btn">
            <el-icon><Close /></el-icon>
          </el-button>
        </div>

        <div class="chat-messages" ref="messagesRef">
          <div
            v-for="(msg, i) in messages"
            :key="i"
            class="message-row"
            :class="msg.role"
          >
            <div v-if="msg.role === 'assistant'" class="avatar">🤖</div>
            <div class="bubble" :class="msg.role">
              <div class="msg-content">{{ msg.content }}</div>
              <div v-if="msg.products?.length" class="product-suggestions">
                <div
                  v-for="p in msg.products"
                  :key="p.id"
                  class="suggest-product"
                  @click="goProduct(p.id)"
                >
                  <img :src="p.coverImage" :alt="p.name" />
                  <div>
                    <div class="sp-name">{{ p.name }}</div>
                    <div class="sp-price">¥{{ p.price }}</div>
                  </div>
                </div>
              </div>
            </div>
            <div v-if="msg.role === 'user'" class="avatar user-av">
              {{ userStore.username?.charAt(0) || '我' }}
            </div>
          </div>
          <div v-if="loading" class="message-row assistant">
            <div class="avatar">🤖</div>
            <div class="bubble assistant typing">
              <span class="dot"></span><span class="dot"></span><span class="dot"></span>
            </div>
          </div>
        </div>

        <div class="quick-questions">
          <span
            v-for="q in quickQuestions"
            :key="q"
            class="quick-q"
            @click="sendMessage(q)"
          >{{ q }}</span>
        </div>

        <div class="chat-input">
          <el-input
            v-model="inputText"
            placeholder="请输入您的问题..."
            :disabled="loading"
            @keyup.enter="sendMessage()"
            maxlength="200"
          />
          <el-button type="primary" @click="sendMessage()" :disabled="loading || !inputText.trim()">
            <el-icon><Promotion /></el-icon>
          </el-button>
        </div>
      </div>
    </Transition>
  </div>
</template>

<script setup>
import { ref, nextTick, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { aiApi } from '@/api/ai'

const router = useRouter()
const userStore = useUserStore()

const isOpen = ref(false)
const inputText = ref('')
const loading = ref(false)
const messagesRef = ref(null)
const sessionId = ref(Date.now().toString())

const messages = ref([
  {
    role: 'assistant',
    content: '您好！我是京东AI智能客服京小智，很高兴为您服务！\n\n我可以帮您：\n• 推荐适合的商品\n• 解答购物问题\n• 咨询退换货政策\n• 查询物流状态',
    products: []
  }
])

const quickQuestions = [
  '推荐手机',
  '退货流程',
  '配送时间',
  '有什么优惠'
]

function toggleChat() {
  isOpen.value = !isOpen.value
}

async function sendMessage(text) {
  const msg = text || inputText.value.trim()
  if (!msg) return
  inputText.value = ''

  messages.value.push({ role: 'user', content: msg })
  loading.value = true
  scrollToBottom()

  try {
    const history = messages.value.slice(0, -1).map(m => ({
      role: m.role,
      content: m.content
    }))
    const res = await aiApi.chat({ message: msg, sessionId: sessionId.value, history })
    messages.value.push({
      role: 'assistant',
      content: res.data.reply,
      products: res.data.products || []
    })
  } catch {
    messages.value.push({
      role: 'assistant',
      content: '抱歉，我暂时无法回复，请稍后再试。',
      products: []
    })
  } finally {
    loading.value = false
    scrollToBottom()
  }
}

function goProduct(id) {
  router.push(`/products/${id}`)
  isOpen.value = false
}

async function scrollToBottom() {
  await nextTick()
  if (messagesRef.value) {
    messagesRef.value.scrollTop = messagesRef.value.scrollHeight
  }
}

watch(isOpen, (val) => {
  if (val) scrollToBottom()
})
</script>

<style scoped>
.ai-chat-widget {
  position: fixed;
  bottom: 30px;
  right: 30px;
  z-index: 1000;
}

.chat-fab {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 60px;
  height: 60px;
  background: var(--jd-red);
  border-radius: 50%;
  cursor: pointer;
  box-shadow: 0 4px 16px rgba(225,37,27,0.4);
  color: #fff;
  font-size: 24px;
  justify-content: center;
  transition: transform 0.2s, box-shadow 0.2s;
  user-select: none;
}

.chat-fab:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 20px rgba(225,37,27,0.5);
}

.fab-label {
  font-size: 10px;
  line-height: 1;
}

.chat-window {
  position: absolute;
  bottom: 70px;
  right: 0;
  width: 360px;
  height: 520px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0,0,0,0.15);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.chat-slide-enter-active, .chat-slide-leave-active {
  transition: all 0.3s ease;
}
.chat-slide-enter-from, .chat-slide-leave-to {
  opacity: 0;
  transform: translateY(20px) scale(0.95);
}

.chat-header {
  background: linear-gradient(135deg, var(--jd-red), #ff6b6b);
  color: #fff;
  padding: 14px 16px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.ai-avatar {
  font-size: 28px;
  width: 40px;
  height: 40px;
  background: rgba(255,255,255,0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.ai-name {
  font-size: 15px;
  font-weight: bold;
}

.ai-status {
  font-size: 11px;
  opacity: 0.85;
}

.close-btn {
  margin-left: auto;
  color: #fff !important;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.message-row {
  display: flex;
  align-items: flex-end;
  gap: 8px;
}

.message-row.user {
  flex-direction: row-reverse;
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  flex-shrink: 0;
}

.user-av {
  background: var(--jd-red);
  color: #fff;
  font-size: 13px;
  font-weight: bold;
}

.bubble {
  max-width: 75%;
  padding: 10px 14px;
  border-radius: 12px;
  font-size: 14px;
  line-height: 1.5;
  white-space: pre-wrap;
}

.bubble.assistant {
  background: #f5f5f5;
  border-radius: 2px 12px 12px 12px;
  color: #333;
}

.bubble.user {
  background: var(--jd-red);
  border-radius: 12px 2px 12px 12px;
  color: #fff;
}

.typing {
  display: flex;
  gap: 4px;
  align-items: center;
  padding: 12px 16px;
}

.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #999;
  animation: bounce 1.2s infinite;
}

.dot:nth-child(2) { animation-delay: 0.2s; }
.dot:nth-child(3) { animation-delay: 0.4s; }

@keyframes bounce {
  0%, 80%, 100% { transform: translateY(0); }
  40% { transform: translateY(-6px); }
}

.product-suggestions {
  margin-top: 8px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.suggest-product {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px;
  background: #fff;
  border-radius: 6px;
  cursor: pointer;
  border: 1px solid #e5e5e5;
}

.suggest-product:hover {
  border-color: var(--jd-red);
}

.suggest-product img {
  width: 40px;
  height: 40px;
  object-fit: cover;
  border-radius: 4px;
}

.sp-name {
  font-size: 12px;
  color: #333;
  line-height: 1.3;
}

.sp-price {
  font-size: 13px;
  color: var(--jd-red);
  font-weight: bold;
}

.quick-questions {
  padding: 8px 12px;
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  border-top: 1px solid #f0f0f0;
}

.quick-q {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 12px;
  border: 1px solid var(--jd-red);
  color: var(--jd-red);
  cursor: pointer;
  transition: all 0.15s;
}

.quick-q:hover {
  background: var(--jd-red);
  color: #fff;
}

.chat-input {
  padding: 12px;
  display: flex;
  gap: 8px;
  border-top: 1px solid #e5e5e5;
}

.chat-input .el-input {
  flex: 1;
}
</style>
