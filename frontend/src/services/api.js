import axios from 'axios';
import { useAuthStore } from '../store/authStore';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

console.log(API_URL);

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 15000,
});

api.interceptors.request.use(
  (config) => {

    const token = useAuthStore.getState().token;

    // Public routes
    const publicRoutes = ["/register", "/login"];

    const isPublicRoute = publicRoutes.some((route) =>
      config.url.includes(route)
    );

    if (token && !isPublicRoute) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);
// Response interceptor to handle errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      useAuthStore.getState().logout();
      if (window.location.pathname !== '/login') {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export default api;

// Auth API
export const authAPI = {
  login: (data) => api.post('/auth/login', data),
  register: (data) => api.post('/auth/register', data),
  refreshToken: () => api.post('/auth/refresh'),
  forgotPassword: (email) => api.post('/auth/forgot-password"', { email }),
  resetPassword: (data) => api.post('/auth/reset-password', data),
};

// Products API
export const productsAPI = {
  getAll: (params) => api.get('/products', { params }),
  getById: (id) => api.get(`/products/${id}`),
  getByCategory: (categoryId, params) => api.get(`/products/category/${categoryId}`, { params }),
  getFeatured: (params) => api.get('/products/featured', { params }),
  getNewArrivals: (params) => api.get('/products/new-arrivals', { params }),
  getBestSellers: (params) => api.get('/products/best-sellers', { params }),
  search: (query, params) => api.get('/products/search', { params: { q: query, ...params } }),
  getRelated: (productId, params) => api.get(`/products/${productId}/related`, { params }),
  // Admin product operations
  create: (data) => api.post('/admin/products', data),
  update: (id, data) => api.put(`/admin/products/${id}`, data),
  delete: (id) => api.delete(`/admin/products/${id}`),
  toggleFeatured: (id) => api.put(`/admin/products/${id}/toggle-featured`),
  toggleActive: (id) => api.put(`/admin/products/${id}/toggle-active`),
  bulkDelete: (ids) => api.delete('/admin/products/bulk', { data: { ids } }),
  bulkUpdateStock: (updates) => api.put('/admin/products/bulk-stock', { updates }),
};

// Categories API
export const categoriesAPI = {
  getAll: () => api.get('/categories'),
  getRoot: () => api.get('/categories/root'),
  getById: (id) => api.get(`/categories/${id}`),
  getSubcategories: (id) => api.get(`/categories/${id}/subcategories`),
  // Admin category operations
  create: (data) => api.post('/admin/categories', data),
  update: (id, data) => api.put(`/admin/categories/${id}`, data),
  delete: (id) => api.delete(`/admin/categories/${id}`),
};



// User API
export const userAPI = {

  // ================= PROFILE =================
  getProfile: (id) =>
    api.get('/user/profile', { params: { id } }),

  updateProfile: (data) =>
    api.put('/api/user/profile', data),

  // ================= ORDER =================
  trackOrder: (orderId) =>
    api.get(`/api/user/orders/${orderId}`),

  // ================= FEEDBACK =================
  addFeedback: (data) =>
    api.post('/api/user/feedback', data),

  getFeedbackByProduct: (productId) =>
    api.get(`/api/user/feedback/product/${productId}`),

  // ================= CATEGORY =================
  getAllCategories: () =>
    api.get('/api/category/categories'),

  getCategoryById: (categoryId) =>
    api.get(`/api/category/categories/${categoryId}`),

  getCategoryBySlug: (slug) =>
    api.get(`/api/category/categories/slug/${slug}`),

  getActiveCategories: () =>
    api.get('/api/category/categories/active'),

  searchCategories: (params) =>
    api.get('/api/category/categories/search', { params }),

  // ================= PRODUCTS =================
  getProductById: (productId) =>
    api.get(`/api/user/products/${productId}`),

  searchProducts: (params) =>
    api.get('/api/user/products/search', { params }),

  getProductsByCategory: (categoryId, params) =>
    api.get(`/api/user/products/category/${categoryId}`, { params }),

  getAllProducts: (params) =>
    api.get('/api/user/products', { params }),
};


// Wishlist API
export const wishlistAPI = {

  createWishlist: () =>
    api.post('/api/wishlist/create'),

  getWishlist: () =>
    api.get('/api/wishlist'),

  addProductToWishlist: (productId) =>
    api.post(`/api/wishlist/add-product/${productId}`),
};


// Order API
export const orderAPI = {

  // =========================
  // PLACE ORDER
  // =========================
  placeOrder: (data) =>
    api.post('/api/orders/', data),

  // =========================
  // CUSTOMER ORDER HISTORY
  // =========================
  getOrderHistory: (params) =>
    api.get('/api/orders/history', { params }),

  // =========================
  // ARTISAN DASHBOARD ORDERS
  // =========================
  getArtisanOrders: (params) =>
    api.get('/api/orders/artisans/orders', { params }),

  // =========================
  // CANCEL ORDER (your mapping name says pay but method name says cancel)
  // =========================
  cancelOrder: (orderId) =>
    api.get(`/api/orders/${orderId}/pay`, {
      params: { orderId }
    }),

  // =========================
  // GET ORDER BY ID
  // =========================
  getOrderById: (orderId) =>
    api.get('/api/orders/users/orders', {
      params: { orderId }
    }),

  // =========================
  // UPDATE ORDER STATUS (ARTISAN)
  // =========================
  updateOrderStatus: (orderId, status) =>
    api.put(`/api/orders/${orderId}/status`, null, {
      params: { status }
    }),
};

// Reviews API
export const reviewsAPI = {
  getByProduct: (productId, params) => api.get(`/reviews/product/${productId}`, { params }),
  create: (data) => api.post('/reviews', data),
  update: (id, data) => api.put(`/reviews/${id}`, data),
  delete: (id) => api.delete(`/reviews/${id}`),
  markHelpful: (id) => api.post(`/reviews/${id}/helpful`),
  reportReview: (id, reason) => api.post(`/reviews/${id}/report`, { reason }),
};





// Coupons API
export const couponsAPI = {
  validate: (code) => api.post('/coupons/validate', { code }),
  getAvailable: () => api.get('/coupons/available'),
};

// Admin API
export const adminAPI = {

  // =========================
  // ARTISAN MANAGEMENT
  // =========================
  getArtisans: () => api.get('/api/admin/artisans'),

  approveArtisan: (id) =>
    api.patch(`/api/admin/artisans/${id}/approve`),

  rejectArtisan: (id) =>
    api.patch(`/api/admin/artisans/${id}/reject`),


  // =========================
  // USER MANAGEMENT
  // =========================
  getAllUsers: () =>
    api.get('/api/admin/all'),

  blockUser: (id) =>
    api.patch(`/api/admin/users/${id}/block`),


  // =========================
  // ORDER MANAGEMENT
  // =========================
  getAllOrders: () =>
    api.get('/api/admin/users/orders'),

  updateArtisanOrderStatus: (id, status) =>
    api.get(`/api/admin/artisan/orders/${id}/status`, {
      params: { id, status }
    }),

  updateAllOrderStatus: (id, status) =>
    api.get(`/api/admin/all/orders/${id}/status`, {
      params: { id, status }
    }),


  // =========================
  // DASHBOARD
  // =========================
  getDailyMetrics: () =>
    api.get('/api/admin/daily-metrics'),


  // =========================
  // CATEGORY MANAGEMENT
  // =========================
  getCategories: (params) =>
    api.get('/api/admin/allCategory', { params }),

  createCategory: (formData) =>
    api.post('/api/admin/categories', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    }),

  updateCategory: (id, formData) =>
    api.put(`/api/admin/categories/${id}`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    }),

  deleteCategory: (id) =>
    api.delete(`/api/admin/categories/${id}`),


  // =========================
  // CATEGORY REQUESTS
  // =========================
  getPendingCategoryRequests: () =>
    api.get('/api/admin/pending'),

  approveCategoryRequest: (id) =>
    api.put(`/api/admin/${id}/approve`),

  rejectCategoryRequest: (id, reason) =>
    api.put(`/api/admin/${id}/reject`, null, {
      params: { reason }
    }),


  // =========================
  // PRODUCT MANAGEMENT
  // =========================
  getProducts: (params) =>
    api.get('/api/admin/products', { params }),

  getProductById: (id) =>
    api.get(`/api/admin/${id}`),

  updateProductStatus: (id, active) =>
    api.put(`/api/admin/${id}/status`, null, {
      params: { active }
    }),

  hardDeleteProduct: (id) =>
    api.delete(`/api/admin/${id}/hard-delete`),
};


// Artisan API
export const artisanAPI = {

  // =========================
  // ARTISAN PROFILE
  // =========================

  applyAsArtisan: (data) =>
  api.post('/artisans/apply', data),

  getArtisanDetails: () =>
    api.get('/artisans/getArtisan'),

  updateArtisanDetails: (data) =>
    api.post('/artisans/updateArtisan', data),


  // =========================
  // CATEGORY REQUEST
  // =========================

  createCategoryRequest: (data) =>
    api.post('/artisans/create', data),

  getMyCategoryRequests: () =>
    api.get('/artisans/my'),


  getProfileExists : () =>
    api.get('/artisans/details'),


  getKYCStatus : () =>
   api.get('/artisans/kycStatus'),
  // =========================
  // PRODUCT MANAGEMENT
  // =========================

  createProduct: (formData) =>
    api.post('/artisans/saveProduct', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    }),

  updateProduct: (formData) =>
    api.post('/artisans/UpdateProduct', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    }),

  deleteProduct: (productId) =>
    api.delete(`/artisans/delete/${productId}`),

  getProductsByArtisan: (artisanId, params) =>
    api.get(`/artisans/${artisanId}/product`, { params }),
};


// Address API
export const addressAPI = {

  // =========================
  // CREATE
  // =========================
  addAddress: (data) =>
    api.post('/api/address/addAddress', data),


  // =========================
  // READ
  // =========================
  getAddresses: () =>
    api.get('/api/address/getAddress'),

  getDefaultAddress: (userId) =>
    api.get('/api/address/getDefaultAddress', {
      params: { userId }
    }),


  // =========================
  // UPDATE
  // =========================
  updateAddress: (addressId, data) =>
    api.put('/api/address/updateAddress', data, {
      params: { addressId }
    }),

  setDefaultAddress: (userId, addressId) =>
    api.put('/api/address/setDefaultAddress', null, {
      params: { userId, addressId }
    }),


  // =========================
  // DELETE
  // =========================
  deleteAddress: () =>
    api.delete('/api/address/delAddress'),
};


// Cart API
export const cartAPI = {

  // =========================
  // ADD TO CART
  // =========================
  addToCart: (data) =>
    api.post('/api/cart/addCart', data),

  // =========================
  // GET USER CART
  // =========================
  getCart: () =>
    api.get('/api/cart/getCart'),

  // =========================
  // DELETE ITEM FROM CART
  // =========================
  deleteCartItem: (productId) =>
    api.delete(`/api/cart/deleteCart/${productId}`),

  // =========================
  // UPDATE CART ITEM
  // =========================
  updateCartItem: (data) =>
    api.put(`/api/cart/update/${data.productId}`, data),
};