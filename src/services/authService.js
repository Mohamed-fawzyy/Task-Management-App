import api from './axiosInstance';

const register = async (userData) => {
  const res = await api.post('/auth/v1/register', userData);
  return res.data;
};

const login = async (credentials) => {
  const res = await api.post('/auth/v1/authenticate', credentials);
  return res.data;
};

const refreshToken = async (refreshToken) => {
  const res = await api.post('/auth/v1/refresh-token', { refreshToken });
  return res.data;
};

const logout = async (refreshToken) => {
  await api.post('/auth/v1/logout', { refreshToken });
};

const authService = { register, login, refreshToken, logout };
export default authService; 