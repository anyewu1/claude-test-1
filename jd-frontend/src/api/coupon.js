import http from './index'

export const couponApi = {
  validate: (code, orderAmount) =>
    http.get('/coupons/validate', { params: { code, orderAmount } })
}
