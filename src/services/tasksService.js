import api from './axiosInstance';

const fetchTasks = async (params) => {
  const res = await api.get('/v1/tasks', { params });
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

const tasksService = { fetchTasks, createTask, updateTask, deleteTask };
export default tasksService; 