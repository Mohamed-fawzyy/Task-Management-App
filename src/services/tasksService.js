import api from './axiosInstance';

const fetchTasks = async (params) => {
  // Only send parameters that are explicitly set (not empty/default values)
  const backendParams = {};
  
  if (params.page !== undefined && params.page !== null) {
    backendParams.page = params.page;
  }
  
  if (params.size !== undefined && params.size !== null) {
    backendParams.size = params.size;
  }
  
  if (params.sortBy && params.sortBy.trim() !== '') {
    backendParams.sortBy = params.sortBy;
  }
  
  const res = await api.get('/v1/tasks', { params: backendParams });
  return res.data;
};

const searchTasks = async (title, params) => {
  // Only send parameters that are explicitly set (not empty/default values)
  const backendParams = { title };
  
  if (params.page !== undefined && params.page !== null) {
    backendParams.page = params.page;
  }
  
  if (params.size !== undefined && params.size !== null) {
    backendParams.size = params.size;
  }
  
  if (params.sortBy && params.sortBy.trim() !== '') {
    backendParams.sortBy = params.sortBy;
  }
  
  const res = await api.get('/v1/tasks/search', { params: backendParams });
  return res.data;
};

const createTask = async (task) => {
  const res = await api.post('/v1/new-task', task);
  return res.data;
};

const updateTask = async (id, task) => {
  const res = await api.put(`/v1/tasks/${id}`, task);
  return res.data;
};

const deleteTask = async (id) => {
  await api.delete(`/v1/tasks/${id}`);
};

const tasksService = { fetchTasks, searchTasks, createTask, updateTask, deleteTask };
export default tasksService; 