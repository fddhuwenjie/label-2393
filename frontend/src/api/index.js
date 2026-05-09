import axios from 'axios'
import store from '@/store'
import router from '@/router'

// 创建axios实例
const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器 - 添加Token
api.interceptors.request.use(
  config => {
    const token = store.state.token
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res
  },
  error => {
    if (error.response) {
      const { status, data } = error.response
      if (status === 401) {
        // Token过期或无效，清除登录状态
        store.commit('logout')
        router.push('/login')
        return Promise.reject(new Error('登录已过期，请重新登录'))
      }
      if (status === 403) {
        return Promise.reject(new Error(data?.message || '权限不足'))
      }
      return Promise.reject(new Error(data?.message || '请求失败'))
    }
    return Promise.reject(error)
  }
)

// 用户相关API
export const userApi = {
  login: (data) => api.post('/user/login', data),
  register: (data) => api.post('/user/register', data),
  getCurrentUser: () => api.get('/user/me'),
  getById: (id) => api.get(`/user/${id}`),
  update: (id, data) => api.put(`/user/${id}`, data)
}

// 电影相关API
export const movieApi = {
  getList: (params) => api.get('/movie/list', { params }),
  getById: (id) => api.get(`/movie/${id}`),
  getFilters: () => api.get('/movie/filters')
}

// 评论相关API
export const reviewApi = {
  getByMovieId: (movieId, params) => api.get(`/review/movie/${movieId}`, { params }),
  getByUserId: (userId, params) => api.get(`/review/user/${userId}`, { params }),
  checkUserReview: (userId, movieId) => api.get('/review/check', { params: { userId, movieId } }),
  save: (data) => api.post('/review', data),
  delete: (id) => api.delete(`/review/${id}`)
}

export default api
