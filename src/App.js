import React, { useEffect } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import RegisterPage from './pages/RegisterPage';
import LoginPage from './pages/LoginPage';
import DashboardPage from './pages/DashboardPage';
import CreateTaskPage from './pages/CreateTaskPage';
import EditTaskPage from './pages/EditTaskPage';
import TaskDetailsModal from './pages/TaskDetailsModal';
import ProtectedRoute from './components/ProtectedRoute';

function App() {
  // Initialize dark mode on app load
  useEffect(() => {
    const theme = localStorage.getItem('theme');
    if (theme === 'dark') {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }, []);

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 text-gray-900 dark:text-gray-100">
      <Routes>
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route
          path="/"
          element={<Navigate to="/login" replace />} />
        <Route
          path="/dashboard"
          element={<DashboardPage />}
        />
        <Route
          path="/tasks/new"
          element={<CreateTaskPage />}
        />
        <Route
          path="/tasks/:id/edit"
          element={<EditTaskPage />}
        />
        <Route
          path="/tasks/:id"
          element={<TaskDetailsModal />}
        />
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </div>
  );
}

export default App;
