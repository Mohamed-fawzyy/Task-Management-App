import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchTasks, searchTasks, setFilters } from '../store/slices/tasksSlice';
import { logout } from '../store/slices/authSlice';
import { useNavigate } from 'react-router-dom';
// Remove Heroicons import
// import { MoonIcon, SunIcon, ArrowLeftOnRectangleIcon } from '@heroicons/react/24/outline';

const statusOptions = [
  { value: '', label: 'All' },
  { value: 'PENDING', label: 'Pending' },
  { value: 'IN_PROGRESS', label: 'In Progress' },
  { value: 'COMPLETED', label: 'Completed' },
];
const priorityOptions = [
  { value: '', label: 'All' },
  { value: 'LOW', label: 'Low' },
  { value: 'MEDIUM', label: 'Medium' },
  { value: 'HIGH', label: 'High' },
];

function classNames(...classes) {
  return classes.filter(Boolean).join(' ');
}

const DashboardPage = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { tasks = [], loading, filters, error, total = 0 } = useSelector((state) => state.tasks);
  const { user } = useSelector((state) => state.auth);
  const [darkMode, setDarkMode] = useState(() => localStorage.getItem('theme') === 'dark');
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    document.documentElement.classList.toggle('dark', darkMode);
    localStorage.setItem('theme', darkMode ? 'dark' : 'light');
  }, [darkMode]);

  // Debounced search effect
  useEffect(() => {
    const timeoutId = setTimeout(() => {
      // Use search endpoint if there's a search term, otherwise use regular fetch
      if (searchTerm.trim()) {
        dispatch(searchTasks({ title: searchTerm, params: filters }));
      } else {
        dispatch(fetchTasks(filters));
      }
    }, 500); // 500ms delay

    return () => clearTimeout(timeoutId);
  }, [dispatch, filters, searchTerm]);

  useEffect(() => {
    // Initial load - only fetch tasks if no search term
    if (!searchTerm.trim()) {
      dispatch(fetchTasks(filters));
    }
  }, [dispatch, filters]);

  const handleFilterChange = (e) => {
    dispatch(setFilters({ [e.target.name]: e.target.value, page: 1 }));
  };

  const handleSearch = (e) => {
    setSearchTerm(e.target.value);
  };

  const handlePageChange = (newPage) => {
    dispatch(setFilters({ page: newPage }));
  };

  const handleSort = (sortBy) => {
    dispatch(setFilters({ sortBy }));
  };

  const handleLogout = async () => {
    await dispatch(logout(localStorage.getItem('refreshToken')));
    navigate('/register');
  };

  let content;
  if (error && (error.toString().includes('403') || error.toString().toLowerCase().includes('forbidden') || error.toString().toLowerCase().includes('unauthorized'))) {
    content = (
      <div className="text-center text-red-600 py-8">
        Access denied or session expired. Please <button className="text-blue-600 underline" onClick={() => navigate('/login')}>log in again</button>.
      </div>
    );
  } else {
    content = (
      <div className="overflow-x-auto rounded shadow bg-white dark:bg-gray-800">
        <table className="min-w-full divide-y divide-gray-200 dark:divide-gray-700">
          <thead>
            <tr>
              <th className="px-4 py-2 cursor-pointer" onClick={() => handleSort('title')}>Title</th>
              <th className="px-4 py-2 cursor-pointer" onClick={() => handleSort('dueDate')}>Due Date</th>
              <th className="px-4 py-2 cursor-pointer" onClick={() => handleSort('priority')}>Priority</th>
              <th className="px-4 py-2 cursor-pointer" onClick={() => handleSort('status')}>Status</th>
              <th className="px-4 py-2">Actions</th>
            </tr>
          </thead>
          <tbody>
            {loading ? (
              <tr><td colSpan={5} className="text-center py-8">Loading...</td></tr>
            ) : (!Array.isArray(tasks) || tasks.length === 0) ? (
              <tr><td colSpan={5} className="text-center py-8">No tasks yet. Click + New Task to create your first task.</td></tr>
            ) : (
              tasks.map((task) => (
                <tr key={task.id} className="hover:bg-gray-100 dark:hover:bg-gray-700 transition">
                  <td className="px-4 py-2 font-medium">{task.title}</td>
                  <td className="px-4 py-2">{task.dueDate ? new Date(task.dueDate).toLocaleDateString() : '-'}</td>
                  <td className="px-4 py-2">
                    <span className={classNames(
                      'px-2 py-1 rounded text-xs font-semibold',
                      task.priority === 'HIGH' ? 'bg-red-100 text-red-700 dark:bg-red-900 dark:text-red-200' :
                      task.priority === 'MEDIUM' ? 'bg-yellow-100 text-yellow-700 dark:bg-yellow-900 dark:text-yellow-200' :
                      'bg-green-100 text-green-700 dark:bg-green-900 dark:text-green-200'
                    )}>{task.priority}</span>
                  </td>
                  <td className="px-4 py-2">
                    <span className={classNames(
                      'px-2 py-1 rounded text-xs font-semibold',
                      task.status === 'COMPLETED' ? 'bg-green-200 text-green-900 dark:bg-green-800 dark:text-green-100' :
                      task.status === 'IN_PROGRESS' ? 'bg-blue-200 text-blue-900 dark:bg-blue-800 dark:text-blue-100' :
                      'bg-gray-200 text-gray-900 dark:bg-gray-700 dark:text-gray-100'
                    )}>{task.status}</span>
                  </td>
                  <td className="px-4 py-2 flex gap-2">
                    <button
                      onClick={() => navigate(`/tasks/${task.id}`)}
                      className="text-blue-600 dark:text-blue-400 hover:underline"
                    >View</button>
                    <button
                      onClick={() => navigate(`/tasks/${task.id}/edit`)}
                      className="text-yellow-600 dark:text-yellow-400 hover:underline"
                    >Edit</button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 text-gray-900 dark:text-gray-100">
      <header className="flex items-center justify-between px-6 py-4 bg-white dark:bg-gray-800 shadow">
        <div className="flex items-center gap-2">
          <span className="text-xl font-bold tracking-tight">Task Dashboard</span>
        </div>
        <div className="flex items-center gap-4">
          <span className="font-medium">
            {user?.firstName ? 
              `Hi, ${user.firstName}${user.lastName ? ' ' + user.lastName : ''}!` : 
              user?.email ? 
                `Hi, ${user.email.split('@')[0]}!` : 
                'Hi, User!'
            }
          </span>
          <button
            onClick={() => setDarkMode((d) => !d)}
            className="p-2 rounded hover:bg-gray-200 dark:hover:bg-gray-700"
            title="Toggle dark mode"
          >
            {/* {darkMode ? <SunIcon className="h-5 w-5" /> : <MoonIcon className="h-5 w-5" />} */}
            {darkMode ? '🌞' : '🌙'}
          </button>
          <button
            onClick={handleLogout}
            className="p-2 rounded hover:bg-red-100 dark:hover:bg-red-900"
            title="Logout"
          >
            {/* <ArrowLeftOnRectangleIcon className="h-5 w-5 text-red-600" /> */}
            <span role="img" aria-label="logout">🚪</span>
          </button>
        </div>
      </header>
      <main className="max-w-5xl mx-auto py-8 px-4">
        <div className="flex flex-col md:flex-row md:items-end gap-4 mb-6">
          {/* <div>
            <label className="block text-sm font-medium mb-1">Status</label>
            <select
              name="status"
              value={filters.status}
              onChange={handleFilterChange}
              className="rounded border-gray-300 dark:bg-gray-700 dark:border-gray-600 px-2 py-1"
            >
              {statusOptions.map((opt) => (
                <option key={opt.value} value={opt.value}>{opt.label}</option>
              ))}
            </select>
          </div> */}
          {/* <div>
            <label className="block text-sm font-medium mb-1">Priority</label>
            <select
              name="priority"
              value={filters.priority}
              onChange={handleFilterChange}
              className="rounded border-gray-300 dark:bg-gray-700 dark:border-gray-600 px-2 py-1"
            >
              {priorityOptions.map((opt) => (
                <option key={opt.value} value={opt.value}>{opt.label}</option>
              ))}
            </select>
          </div> */}
          <div className="flex-1">
            <label className="block text-sm font-medium mb-1">Search</label>
            <input
              type="text"
              placeholder="Search by title..."
              value={searchTerm}
              onChange={handleSearch}
              className="w-full rounded border-gray-300 dark:bg-gray-700 dark:border-gray-600 px-2 py-1 bg-white dark:bg-gray-700 text-gray-900 dark:text-gray-100"
            />
          </div>
          <button
            onClick={() => navigate('/tasks/new')}
            className="bg-blue-600 hover:bg-blue-700 text-white dark:text-white font-bold py-2 px-4 rounded shadow"
          >
            + New Task
          </button>
        </div>
        {content}
        {/* Pagination */}
        <div className="flex justify-between items-center mt-6">
          <button
            onClick={() => handlePageChange((filters.page || 0) - 1)}
            disabled={(filters.page || 0) <= 0}
            className="px-3 py-1 rounded bg-gray-200 dark:bg-gray-700 text-gray-900 dark:text-gray-100 disabled:opacity-50"
          >
            Previous
          </button>
          <span>Page {(filters.page || 0) + 1} of {Math.ceil(total / 10)}</span>
          <button
            onClick={() => handlePageChange((filters.page || 0) + 1)}
            disabled={(filters.page || 0) >= Math.ceil(total / 10) - 1}
            className="px-3 py-1 rounded bg-gray-200 dark:bg-gray-700 text-gray-900 dark:text-gray-100 disabled:opacity-50"
          >
            Next
          </button>
        </div>
        {error && <div className="text-red-600 text-center mt-4">{error}</div>}
      </main>
    </div>
  );
};

export default DashboardPage; 