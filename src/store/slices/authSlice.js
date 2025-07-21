import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import authService from '../../services/authService';
import { setAuthToken } from '../../services/axiosInstance';

const userRaw = localStorage.getItem('user');
const user = userRaw && userRaw !== 'undefined' ? JSON.parse(userRaw) : null;
const accessToken = localStorage.getItem('accessToken');
const refreshToken = localStorage.getItem('refreshToken');

const initialState = {
  user: user || null,
  accessToken: accessToken || null,
  refreshToken: refreshToken || null,
  loading: false,
  error: null,
};

export const login = createAsyncThunk('auth/login', async (credentials, thunkAPI) => {
  try {
    const data = await authService.login(credentials);
    // For login, we don't have user details from the form, so we'll create a basic user object
    // The user details will be loaded from localStorage if available
    return data;
  } catch (error) {
    let errMsg = error.response?.data?.message;
    if (!errMsg && error.response?.data) {
      if (typeof error.response.data === 'string') {
        // If it's an HTML error page, show a friendly message
        if (error.response.data.startsWith('<!DOCTYPE html>')) {
          errMsg = 'Server error: Endpoint not found or backend is not running at the expected URL.';
        } else {
          errMsg = error.response.data;
        }
      } else if (typeof error.response.data === 'object') {
        errMsg = Object.values(error.response.data)
          .filter(v => typeof v === 'string' || typeof v === 'number')
          .join(' ');
      }
    }
    // Remove any prefix like 'Unexpected server error:' if present
    if (errMsg && errMsg.startsWith('Unexpected server error:')) {
      errMsg = errMsg.replace('Unexpected server error:', '').trim();
    }
    return thunkAPI.rejectWithValue(errMsg || 'Login failed');
  }
});

export const register = createAsyncThunk('auth/register', async (userData, thunkAPI) => {
  try {
    const data = await authService.register(userData);
    // Return both the API response and the user data from the form
    return { ...data, userData };
  } catch (error) {
    let errMsg = error.response?.data?.message;
    if (!errMsg && error.response?.data) {
      if (typeof error.response.data === 'string') {
        // If it's an HTML error page, show a friendly message
        if (error.response.data.startsWith('<!DOCTYPE html>')) {
          errMsg = 'Server error: Endpoint not found or backend is not running at the expected URL.';
        } else {
          errMsg = error.response.data;
        }
      } else if (typeof error.response.data === 'object') {
        errMsg = Object.values(error.response.data)
          .filter(v => typeof v === 'string' || typeof v === 'number')
          .join(' ');
      }
    }
    return thunkAPI.rejectWithValue(errMsg || 'Registration failed');
  }
});

export const refresh = createAsyncThunk('auth/refresh', async (refreshToken, thunkAPI) => {
  try {
    const data = await authService.refreshToken(refreshToken);
    return data;
  } catch (error) {
    return thunkAPI.rejectWithValue(error.response?.data?.message || 'Token refresh failed');
  }
});

export const logout = createAsyncThunk('auth/logout', async (refreshToken, thunkAPI) => {
  try {
    await authService.logout(refreshToken);
    return true;
  } catch (error) {
    return thunkAPI.rejectWithValue(error.response?.data?.message || 'Logout failed');
  }
});

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    resetError(state) {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(login.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(login.fulfilled, (state, action) => {
        state.loading = false;
        // Try to load user details from localStorage, or create basic user object
        const storedUser = localStorage.getItem('user');
        let user;
        if (storedUser && storedUser !== 'undefined') {
          try {
            user = JSON.parse(storedUser);
          } catch (e) {
            // If parsing fails, create basic user object
            user = { email: action.payload.email || 'User' };
          }
        } else {
          // Create basic user object if no stored user data
          user = { email: action.payload.email || 'User' };
        }
        state.user = user;
        state.accessToken = action.payload.accessToken;
        state.refreshToken = action.payload.refreshToken;
        localStorage.setItem('user', JSON.stringify(user));
        localStorage.setItem('accessToken', action.payload.accessToken);
        localStorage.setItem('refreshToken', action.payload.refreshToken);
        setAuthToken(action.payload.accessToken);
      })
      .addCase(login.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      .addCase(register.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(register.fulfilled, (state, action) => {
        state.loading = false;
        // Create user object from registration form data
        const userFromForm = {
          firstName: action.payload.userData.firstName,
          lastName: action.payload.userData.lastName,
          email: action.payload.userData.email
        };
        state.user = userFromForm;
        state.accessToken = action.payload.accessToken;
        state.refreshToken = action.payload.refreshToken;
        localStorage.setItem('user', JSON.stringify(userFromForm));
        localStorage.setItem('accessToken', action.payload.accessToken);
        localStorage.setItem('refreshToken', action.payload.refreshToken);
        setAuthToken(action.payload.accessToken);
      })
      .addCase(register.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      .addCase(refresh.fulfilled, (state, action) => {
        state.accessToken = action.payload.accessToken;
        localStorage.setItem('accessToken', action.payload.accessToken);
        setAuthToken(action.payload.accessToken);
      })
      .addCase(refresh.rejected, (state, action) => {
        state.error = action.payload;
      })
      .addCase(logout.fulfilled, (state) => {
        state.user = null;
        state.accessToken = null;
        state.refreshToken = null;
        localStorage.removeItem('user');
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        setAuthToken(null);
        // No navigation here; handle redirect in the component after dispatch
      })
      .addCase(logout.rejected, (state, action) => {
        state.error = action.payload;
      });
  },
});

export const { resetError } = authSlice.actions;
export default authSlice.reducer; 