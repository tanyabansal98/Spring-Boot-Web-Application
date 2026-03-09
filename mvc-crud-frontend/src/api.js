import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8085/api',
  headers: { 'Content-Type': 'application/json' },
});

// ── Request Interceptor: attach JWT token to every API call ──
// Before each request, read the token from localStorage
// and add it as "Authorization: Bearer <token>"
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// ── Response Interceptor: handle 401 (Unauthorized) ──
// If the backend rejects the token (expired/invalid),
// clear local storage and redirect to login page
api.interceptors.response.use(
  (response) => response, // pass through successful responses
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      localStorage.removeItem('role');
      window.location.href = '/login'; // force redirect to login
    }
    return Promise.reject(error);
  }
);

export default api;
