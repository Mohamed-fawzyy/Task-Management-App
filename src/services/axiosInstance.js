import axios from 'axios';

const api = axios.create({
  baseURL: '/api/task-management',
});

api.interceptors.request.use(
  (config) => {
    // Do not attach token for public auth endpoints
    if (
      config.url.includes('/auth/v1/register') ||
      config.url.includes('/auth/v1/authenticate') ||
      config.url.includes('/auth/v1/refresh-token')
    ) {
      delete config.headers['Authorization'];
    } else {
      const token = localStorage.getItem('accessToken');
      if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
      }
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Axios response interceptor for token refresh
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    if (
      error.response &&
      error.response.status === 401 &&
      !originalRequest._retry &&
      !originalRequest.url.includes('/auth/v1/authenticate') &&
      !originalRequest.url.includes('/auth/v1/register') &&
      !originalRequest.url.includes('/auth/v1/refresh-token')
    ) {
      originalRequest._retry = true;
      try {
        const refreshToken = localStorage.getItem('refreshToken');
        if (!refreshToken) throw new Error('No refresh token');
        // Call refresh token endpoint
        const res = await api.post('/auth/v1/refresh-token', { refreshToken });
        const newAccessToken = res.data.accessToken;
        if (newAccessToken) {
          localStorage.setItem('accessToken', newAccessToken);
          api.defaults.headers.common['Authorization'] = `Bearer ${newAccessToken}`;
          originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
          return api(originalRequest);
        } else {
          // No new token, clear storage and redirect
          localStorage.removeItem('accessToken');
          localStorage.removeItem('refreshToken');
          window.location.href = '/login';
          return Promise.reject(error);
        }
      } catch (refreshError) {
        // Refresh failed, clear storage and redirect
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }
    return Promise.reject(error);
  }
);

export const setAuthToken = (token) => {
  if (token) {
    api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  } else {
    delete api.defaults.headers.common['Authorization'];
  }
};

export default api; 