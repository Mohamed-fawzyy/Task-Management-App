import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import tasksService from '../../services/tasksService';

const initialState = {
  tasks: [],
  total: 0,
  loading: false,
  error: null,
  filters: {
    status: '',
    priority: '',
    search: '',
  },
};

export const fetchTasks = createAsyncThunk('tasks/fetchTasks', async (params, thunkAPI) => {
  try {
    const data = await tasksService.fetchTasks(params);
    return data;
  } catch (error) {
    return thunkAPI.rejectWithValue(error.response?.data?.message || 'Failed to fetch tasks');
  }
});

export const searchTasks = createAsyncThunk('tasks/searchTasks', async ({ title, params }, thunkAPI) => {
  try {
    const data = await tasksService.searchTasks(title, params);
    return data;
  } catch (error) {
    return thunkAPI.rejectWithValue(error.response?.data?.message || 'Failed to search tasks');
  }
});

export const createTask = createAsyncThunk('tasks/createTask', async (task, thunkAPI) => {
  try {
    const data = await tasksService.createTask(task);
    return data;
  } catch (error) {
    let errMsg = error.response?.data?.message;
    if (!errMsg && error.response?.data) {
      if (typeof error.response.data === 'string') {
        errMsg = error.response.data;
      } else if (typeof error.response.data === 'object') {
        errMsg = Object.values(error.response.data).join(' ');
      }
    }
    
    // Extract only the relevant part of the error message
    if (errMsg && typeof errMsg === 'string') {
      if (errMsg.includes('Invalid request:')) {
        // Extract just the "Invalid request:" part and the field name
        const match = errMsg.match(/Invalid request: ([^(]+)/);
        if (match) {
          errMsg = `Invalid request: ${match[1].trim()}`;
        }
      } else if (errMsg.includes('Unrecognized field')) {
        // Extract just the "Unrecognized field" part
        const match = errMsg.match(/(Unrecognized field [^(]+)/);
        if (match) {
          errMsg = match[1];
        }
      }
    }
    
    return thunkAPI.rejectWithValue(errMsg || 'Failed to create task');
  }
});

export const updateTask = createAsyncThunk('tasks/updateTask', async ({ id, task }, thunkAPI) => {
  try {
    const data = await tasksService.updateTask(id, task);
    return data;
  } catch (error) {
    let errMsg = error.response?.data?.message;
    if (!errMsg && error.response?.data) {
      if (typeof error.response.data === 'string') {
        errMsg = error.response.data;
      } else if (typeof error.response.data === 'object') {
        errMsg = Object.values(error.response.data).join(' ');
      }
    }
    return thunkAPI.rejectWithValue(errMsg || 'Failed to update task');
  }
});

export const deleteTask = createAsyncThunk('tasks/deleteTask', async (id, thunkAPI) => {
  try {
    await tasksService.deleteTask(id);
    return id;
  } catch (error) {
    return thunkAPI.rejectWithValue(error.response?.data?.message || 'Failed to delete task');
  }
});

const tasksSlice = createSlice({
  name: 'tasks',
  initialState,
  reducers: {
    setFilters(state, action) {
      state.filters = { ...state.filters, ...action.payload };
    },
    resetError(state) {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchTasks.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchTasks.fulfilled, (state, action) => {
        state.loading = false;
        state.tasks = action.payload?.response?.data || [];
        state.total = action.payload?.response?.totalElements || 0;
      })
      .addCase(fetchTasks.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      .addCase(searchTasks.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(searchTasks.fulfilled, (state, action) => {
        state.loading = false;
        state.tasks = action.payload?.response?.data || [];
        state.total = action.payload?.response?.totalElements || 0;
      })
      .addCase(searchTasks.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      .addCase(createTask.fulfilled, (state, action) => {
        state.tasks.unshift(action.payload);
      })
      .addCase(updateTask.fulfilled, (state, action) => {
        const idx = state.tasks.findIndex(t => t.id === action.payload.id);
        if (idx !== -1) state.tasks[idx] = action.payload;
      })
      .addCase(deleteTask.fulfilled, (state, action) => {
        state.tasks = state.tasks.filter(t => t.id !== action.payload);
      });
  },
});

export const { setFilters, resetError } = tasksSlice.actions;
export default tasksSlice.reducer; 