import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { updateTask, fetchTasks } from '../store/slices/tasksSlice';
import { useNavigate, useParams } from 'react-router-dom';

const priorities = [
  { value: 'LOW', label: 'Low' },
  { value: 'MEDIUM', label: 'Medium' },
  { value: 'HIGH', label: 'High' },
];
const statuses = [
  { value: 'PENDING', label: 'Pending' },
  { value: 'IN_PROGRESS', label: 'In Progress' },
  { value: 'COMPLETED', label: 'Completed' },
];

const EditTaskPage = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { id } = useParams();
  const { tasks, loading, error } = useSelector((state) => state.tasks);
  const [form, setForm] = useState({
    title: '',
    description: '',
    dueDate: '',
    priority: 'LOW',
    status: 'PENDING',
  });
  const [touched, setTouched] = useState({});

  useEffect(() => {
    if (!tasks.length) {
      dispatch(fetchTasks({}));
    }
  }, [dispatch, tasks.length]);

  useEffect(() => {
    const task = tasks.find((t) => String(t.id) === String(id));
    if (task) {
      setForm({
        title: task.title || '',
        description: task.description || '',
        dueDate: task.dueDate ? task.dueDate.slice(0, 10) : '',
        priority: task.priority || 'LOW',
        status: task.status || 'PENDING',
      });
    }
  }, [tasks, id]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleBlur = (e) => {
    setTouched({ ...touched, [e.target.name]: true });
  };

  const validate = () => {
    const errors = {};
    if (!form.title) errors.title = 'Title is required';
    if (!form.dueDate) errors.dueDate = 'Due date is required';
    if (!form.priority) errors.priority = 'Priority is required';
    if (!form.status) errors.status = 'Status is required';
    return errors;
  };
  const errors = validate();
  const isValid = Object.keys(errors).length === 0;

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!isValid) return;
    const result = await dispatch(updateTask({ id, task: form }));
    if (updateTask.fulfilled.match(result)) {
      navigate('/dashboard');
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-50 dark:bg-gray-900">
      <form
        className="bg-white dark:bg-gray-800 shadow-md rounded px-8 pt-6 pb-8 mb-4 w-full max-w-lg"
        onSubmit={handleSubmit}
        noValidate
      >
        <h2 className="text-2xl font-bold mb-6 text-center">Edit Task</h2>
        <div className="mb-4">
          <label className="block text-gray-700 dark:text-gray-200 text-sm font-bold mb-2">Title</label>
          <input
            type="text"
            name="title"
            value={form.title}
            onChange={handleChange}
            onBlur={handleBlur}
            className={`shadow appearance-none border rounded w-full py-2 px-3 leading-tight focus:outline-none focus:shadow-outline bg-white dark:bg-gray-700 text-gray-900 dark:text-gray-100 border-gray-300 dark:border-gray-600 ${errors.title && touched.title ? 'border-red-500' : ''}`}
            autoComplete="off"
          />
          {errors.title && touched.title && (
            <p className="text-red-500 text-xs mt-1">{errors.title}</p>
          )}
        </div>
        <div className="mb-4">
          <label className="block text-gray-700 dark:text-gray-200 text-sm font-bold mb-2">Description</label>
          <textarea
            name="description"
            value={form.description}
            onChange={handleChange}
            onBlur={handleBlur}
            className="shadow appearance-none border rounded w-full py-2 px-3 leading-tight focus:outline-none focus:shadow-outline min-h-[80px] bg-white dark:bg-gray-700 text-gray-900 dark:text-gray-100 border-gray-300 dark:border-gray-600"
          />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700 dark:text-gray-200 text-sm font-bold mb-2">Due Date</label>
          <input
            type="date"
            name="dueDate"
            value={form.dueDate}
            onChange={handleChange}
            onBlur={handleBlur}
            className={`shadow appearance-none border rounded w-full py-2 px-3 leading-tight focus:outline-none focus:shadow-outline bg-white dark:bg-gray-700 text-gray-900 dark:text-gray-100 border-gray-300 dark:border-gray-600 ${errors.dueDate && touched.dueDate ? 'border-red-500' : ''}`}
          />
          {errors.dueDate && touched.dueDate && (
            <p className="text-red-500 text-xs mt-1">{errors.dueDate}</p>
          )}
        </div>
        <div className="mb-4 flex gap-4">
          <div className="flex-1">
            <label className="block text-gray-700 dark:text-gray-200 text-sm font-bold mb-2">Priority</label>
            <select
              name="priority"
              value={form.priority}
              onChange={handleChange}
              onBlur={handleBlur}
              className={`shadow appearance-none border rounded w-full py-2 px-3 leading-tight focus:outline-none focus:shadow-outline bg-white dark:bg-gray-700 text-gray-900 dark:text-gray-100 border-gray-300 dark:border-gray-600 ${errors.priority && touched.priority ? 'border-red-500' : ''}`}
            >
              {priorities.map((opt) => (
                <option key={opt.value} value={opt.value}>{opt.label}</option>
              ))}
            </select>
            {errors.priority && touched.priority && (
              <p className="text-red-500 text-xs mt-1">{errors.priority}</p>
            )}
          </div>
          <div className="flex-1">
            <label className="block text-gray-700 dark:text-gray-200 text-sm font-bold mb-2">Status</label>
            <select
              name="status"
              value={form.status}
              onChange={handleChange}
              onBlur={handleBlur}
              className={`shadow appearance-none border rounded w-full py-2 px-3 leading-tight focus:outline-none focus:shadow-outline bg-white dark:bg-gray-700 text-gray-900 dark:text-gray-100 border-gray-300 dark:border-gray-600 ${errors.status && touched.status ? 'border-red-500' : ''}`}
            >
              {statuses.map((opt) => (
                <option key={opt.value} value={opt.value}>{opt.label}</option>
              ))}
            </select>
            {errors.status && touched.status && (
              <p className="text-red-500 text-xs mt-1">{errors.status}</p>
            )}
          </div>
        </div>
        {error && <p className="text-red-600 text-sm mb-4 text-center">{error}</p>}
        <button
          type="submit"
          className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline disabled:opacity-50"
          disabled={loading || !isValid}
        >
          {loading ? 'Updating...' : 'Update Task'}
        </button>
        <div className="mt-4 text-center">
          <button type="button" onClick={() => navigate('/dashboard')} className="text-blue-600 hover:underline">Cancel</button>
        </div>
      </form>
    </div>
  );
};

export default EditTaskPage; 