import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { createTask } from '../store/slices/tasksSlice';
import { useNavigate } from 'react-router-dom';

const priorities = [
  { value: 'LOW', label: 'Low' },
  { value: 'MEDIUM', label: 'Medium' },
  { value: 'HIGH', label: 'High' },
];

// Helper to format date as yyyy-MM-dd
function formatDateToYMD(dateStr) {
  if (!dateStr) return '';
  const d = new Date(dateStr);
  if (isNaN(d)) return '';
  const yyyy = d.getFullYear();
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const dd = String(d.getDate()).padStart(2, '0');
  return `${yyyy}-${mm}-${dd}`;
}

const CreateTaskPage = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { loading, error } = useSelector((state) => state.tasks);
  const [form, setForm] = useState({
    title: '',
    description: '',
    dueDate: '',
    priority: 'LOW',
  });
  const [touched, setTouched] = useState({});

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
    else if (!/^\d{4}-\d{2}-\d{2}$/.test(form.dueDate)) errors.dueDate = 'Date must be in YYYY-MM-DD format';
    if (!form.priority) errors.priority = 'Priority is required';
    return errors;
  };
  const errors = validate();
  const isValid = Object.keys(errors).length === 0;

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!isValid) return;
    // Always send dueDate as yyyy-MM-dd
    const formattedDueDate = formatDateToYMD(form.dueDate);
    const payload = { ...form, dueDate: formattedDueDate };
    const result = await dispatch(createTask(payload));
    console.log('result:', result);
    if (createTask.fulfilled.match(result)) {
      navigate('/dashboard');
    }
    // error is already shown below the form
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-50 dark:bg-gray-900">
      <form
        className="bg-white dark:bg-gray-800 shadow-md rounded px-8 pt-6 pb-8 mb-4 w-full max-w-lg"
        onSubmit={handleSubmit}
        noValidate
      >
        <h2 className="text-2xl font-bold mb-6 text-center">Create Task</h2>
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
            min={new Date().toISOString().split('T')[0]}
            required
          />
          {errors.dueDate && touched.dueDate && (
            <p className="text-red-500 text-xs mt-1">{errors.dueDate}</p>
          )}
        </div>
        <div className="mb-4">
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
        {error && <p className="text-red-600 text-sm mb-4 text-center">{error}</p>}
        <button
          type="submit"
          className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline disabled:opacity-50"
          disabled={loading || !isValid}
        >
          {loading ? 'Creating...' : 'Create Task'}
        </button>
        <div className="mt-4 text-center">
          <button type="button" onClick={() => navigate('/dashboard')} className="text-blue-600 hover:underline">Cancel</button>
        </div>
      </form>
    </div>
  );
};

export default CreateTaskPage; 