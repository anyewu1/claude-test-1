# JD电商平台 API 接口文档

**Base URL**: `http://localhost:8080/api`

**认证方式**: JWT Bearer Token（请求头 `Authorization: Bearer <token>`）

**统一响应格式**:
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

**分页响应格式**:
```json
{
  "code": 200,
  "data": {
    "records": [],
    "total": 100,
    "size": 10,
    "current": 1,
    "pages": 10
  }
}
```

**错误码说明**:
| code | 说明 |
|------|------|
| 200  | 成功 |
| 400  | 请求参数错误 |
| 401  | 未登录或Token失效 |
| 403  | 无权限 |
| 404  | 资源不存在 |
| 500  | 服务器内部错误 |

---

## 1. 用户认证模块 `/api/auth`

### 1.1 用户注册
- **POST** `/api/auth/register`
- **权限**: 无需认证

**请求体**:
```json
{
  "username": "zhangsan",
  "email": "zhangsan@example.com",
  "password": "password123",
  "phone": "13800138000"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "userId": 1,
    "username": "zhangsan"
  }
}
```

---

### 1.2 用户登录
- **POST** `/api/auth/login`
- **权限**: 无需认证

**请求体**:
```json
{
  "username": "zhangsan",
  "password": "password123"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "userId": 1,
    "username": "zhangsan",
    "avatar": "https://...",
    "role": "USER"
  }
}
```

---

### 1.3 获取当前用户信息
- **GET** `/api/auth/profile`
- **权限**: 需要登录

**响应**:
```json
{
  "code": 200,
  "data": {
    "id": 1,
    "username": "zhangsan",
    "email": "zhangsan@example.com",
    "phone": "13800138000",
    "avatar": "https://...",
    "role": "USER",
    "createdAt": "2024-01-01T00:00:00"
  }
}
```

---

### 1.4 更新用户信息
- **PUT** `/api/auth/profile`
- **权限**: 需要登录

**请求体**:
```json
{
  "phone": "13900139000",
  "avatar": "https://..."
}
```

---

## 2. 商品分类模块 `/api/categories`

### 2.1 获取所有分类（树形）
- **GET** `/api/categories`
- **权限**: 无需认证

**响应**:
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "name": "手机数码",
      "icon": "📱",
      "children": [
        { "id": 11, "name": "手机", "icon": "📱" },
        { "id": 12, "name": "平板电脑", "icon": "💻" }
      ]
    }
  ]
}
```

---

### 2.2 获取顶级分类
- **GET** `/api/categories/top`
- **权限**: 无需认证

---

## 3. 商品模块 `/api/products`

### 3.1 获取商品列表（分页）
- **GET** `/api/products`
- **权限**: 无需认证

**Query 参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码，默认1 |
| size | int | 否 | 每页数量，默认12 |
| categoryId | long | 否 | 分类ID |
| keyword | string | 否 | 搜索关键词 |
| minPrice | decimal | 否 | 最低价格 |
| maxPrice | decimal | 否 | 最高价格 |
| sortBy | string | 否 | 排序字段：price/sales/rating/createdAt |
| sortOrder | string | 否 | 排序方向：asc/desc |

**响应**:
```json
{
  "code": 200,
  "data": {
    "records": [
      {
        "id": 1,
        "name": "iPhone 15 Pro",
        "price": 7999.00,
        "originalPrice": 8999.00,
        "stock": 100,
        "sales": 5000,
        "categoryId": 11,
        "categoryName": "手机",
        "coverImage": "https://...",
        "rating": 4.8,
        "ratingCount": 12345
      }
    ],
    "total": 100,
    "size": 12,
    "current": 1,
    "pages": 9
  }
}
```

---

### 3.2 获取商品详情
- **GET** `/api/products/{id}`
- **权限**: 无需认证

**响应**:
```json
{
  "code": 200,
  "data": {
    "id": 1,
    "name": "iPhone 15 Pro",
    "description": "商品详细描述...",
    "price": 7999.00,
    "originalPrice": 8999.00,
    "stock": 100,
    "sales": 5000,
    "categoryId": 11,
    "categoryName": "手机",
    "images": ["https://...", "https://..."],
    "rating": 4.8,
    "ratingCount": 12345,
    "specifications": [
      { "key": "颜色", "value": "深空黑" },
      { "key": "存储", "value": "256GB" }
    ]
  }
}
```

---

### 3.3 搜索商品
- **GET** `/api/products/search?keyword=iPhone&page=1&size=12`
- **权限**: 无需认证

---

### 3.4 获取推荐商品
- **GET** `/api/products/recommend?productId={id}&limit=8`
- **权限**: 无需认证

---

### 3.5 获取闪购商品
- **GET** `/api/products/flash-sale`
- **权限**: 无需认证

---

## 4. 购物车模块 `/api/cart`

### 4.1 获取购物车
- **GET** `/api/cart`
- **权限**: 需要登录

**响应**:
```json
{
  "code": 200,
  "data": {
    "items": [
      {
        "id": 1,
        "productId": 1,
        "productName": "iPhone 15 Pro",
        "productImage": "https://...",
        "price": 7999.00,
        "quantity": 2,
        "selected": true,
        "stock": 100
      }
    ],
    "totalCount": 2,
    "selectedCount": 2,
    "totalAmount": 15998.00
  }
}
```

---

### 4.2 添加商品到购物车
- **POST** `/api/cart/items`
- **权限**: 需要登录

**请求体**:
```json
{
  "productId": 1,
  "quantity": 1
}
```

---

### 4.3 更新购物车商品数量
- **PUT** `/api/cart/items/{id}`
- **权限**: 需要登录

**请求体**:
```json
{
  "quantity": 3,
  "selected": true
}
```

---

### 4.4 删除购物车商品
- **DELETE** `/api/cart/items/{id}`
- **权限**: 需要登录

---

### 4.5 全选/取消全选
- **PUT** `/api/cart/select-all`
- **权限**: 需要登录

**请求体**:
```json
{
  "selected": true
}
```

---

### 4.6 清空购物车
- **DELETE** `/api/cart`
- **权限**: 需要登录

---

## 5. 收货地址模块 `/api/addresses`

### 5.1 获取地址列表
- **GET** `/api/addresses`
- **权限**: 需要登录

**响应**:
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "name": "张三",
      "phone": "13800138000",
      "province": "广东省",
      "city": "深圳市",
      "district": "南山区",
      "detail": "科技园南区8栋",
      "isDefault": true
    }
  ]
}
```

---

### 5.2 添加地址
- **POST** `/api/addresses`
- **权限**: 需要登录

**请求体**:
```json
{
  "name": "张三",
  "phone": "13800138000",
  "province": "广东省",
  "city": "深圳市",
  "district": "南山区",
  "detail": "科技园南区8栋",
  "isDefault": false
}
```

---

### 5.3 更新地址
- **PUT** `/api/addresses/{id}`

---

### 5.4 删除地址
- **DELETE** `/api/addresses/{id}`

---

### 5.5 设置默认地址
- **PUT** `/api/addresses/{id}/default`

---

## 6. 订单模块 `/api/orders`

### 6.1 创建订单
- **POST** `/api/orders`
- **权限**: 需要登录

**请求体**:
```json
{
  "addressId": 1,
  "cartItemIds": [1, 2, 3],
  "remark": "请尽快发货"
}
```

**响应**:
```json
{
  "code": 200,
  "data": {
    "orderId": 100,
    "orderNo": "JD20240101123456",
    "totalAmount": 15998.00,
    "actualAmount": 15998.00,
    "status": 0
  }
}
```

---

### 6.2 获取订单列表
- **GET** `/api/orders?page=1&size=10&status=`
- **权限**: 需要登录

**Query 参数**:
| 参数 | 说明 |
|------|------|
| status | 订单状态：0-待付款, 1-待发货, 2-待收货, 3-已完成, 4-已取消 |

**响应**:
```json
{
  "code": 200,
  "data": {
    "records": [
      {
        "id": 100,
        "orderNo": "JD20240101123456",
        "status": 0,
        "totalAmount": 15998.00,
        "actualAmount": 15998.00,
        "createdAt": "2024-01-01T12:00:00",
        "items": [
          {
            "productId": 1,
            "productName": "iPhone 15 Pro",
            "productImage": "https://...",
            "price": 7999.00,
            "quantity": 2
          }
        ]
      }
    ],
    "total": 10,
    "current": 1
  }
}
```

---

### 6.3 获取订单详情
- **GET** `/api/orders/{id}`
- **权限**: 需要登录

---

### 6.4 取消订单
- **PUT** `/api/orders/{id}/cancel`
- **权限**: 需要登录

---

### 6.5 模拟支付订单
- **PUT** `/api/orders/{id}/pay`
- **权限**: 需要登录

---

### 6.6 确认收货
- **PUT** `/api/orders/{id}/confirm`
- **权限**: 需要登录

---

## 7. AI 模块 `/api/ai`

### 7.1 AI 客服聊天
- **POST** `/api/ai/chat`
- **权限**: 无需认证（登录后可个性化）

**请求体**:
```json
{
  "message": "我想买一款拍照效果好的手机，预算5000元左右",
  "sessionId": "session-uuid-123",
  "history": [
    { "role": "user", "content": "你好" },
    { "role": "assistant", "content": "您好！我是JD智能客服，有什么可以帮您？" }
  ]
}
```

**响应**:
```json
{
  "code": 200,
  "data": {
    "reply": "根据您的预算5000元，拍照效果好的手机推荐...",
    "products": [
      {
        "id": 2,
        "name": "小米14",
        "price": 3999.00,
        "coverImage": "https://..."
      }
    ]
  }
}
```

---

### 7.2 AI 搜索建议
- **GET** `/api/ai/suggest?keyword=手机&limit=5`
- **权限**: 无需认证

**响应**:
```json
{
  "code": 200,
  "data": ["手机壳", "手机支架", "手机膜", "手机充电器", "手机耳机"]
}
```

---

### 7.3 AI 商品推荐
- **POST** `/api/ai/recommend`
- **权限**: 无需认证

**请求体**:
```json
{
  "query": "适合学生用的平板电脑",
  "budget": 3000,
  "limit": 6
}
```

**响应**:
```json
{
  "code": 200,
  "data": {
    "analysis": "根据您的需求，为您推荐以下适合学生使用的平板电脑...",
    "products": []
  }
}
```

---

## 订单状态说明

| 状态值 | 说明 |
|--------|------|
| 0 | 待付款 |
| 1 | 待发货（已付款）|
| 2 | 待收货（已发货）|
| 3 | 已完成 |
| 4 | 已取消 |
