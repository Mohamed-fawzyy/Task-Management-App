import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { fetchTasks, deleteTask } from '../store/slices/tasksSlice';
import { useParams, useNavigate } from 'react-router-dom';

const TaskDetailsModal = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { tasks } = useSelector((state) => state.tasks);
  const [task, setTask] = useState(null);
  const [deleting, setDeleting] = useState(false);
  const [deleteError, setDeleteError] = useState(null);

  useEffect(() => {
    if (!tasks.length) {
      dispatch(fetchTasks({}));
    }
  }, [dispatch, tasks.length]);

  useEffect(() => {
    const found = tasks.find((t) => String(t.id) === String(id));
    setTask(found || null);
  }, [tasks, id]);

  const handleDelete = async () => {
    setDeleting(true);
    setDeleteError(null);
    const result = await dispatch(deleteTask(id));
    setDeleting(false);
    if (deleteTask.fulfilled.match(result)) {
      navigate('/dashboard');
    } else {
      setDeleteError(result.payload || 'Failed to delete task');
    }
  };

  if (!task) {
    return (
      <div className="flex items-center justify-center min-h-screen bg-gray-50 dark:bg-gray-900">
        <div className="bg-white dark:bg-gray-800 shadow-md rounded px-8 pt-6 pb-8 mb-4 w-full max-w-lg text-center">
          <h2 className="text-2xl font-bold mb-6">Task Not Found</h2>
          <button onClick={() => navigate('/dashboard')} className="text-blue-600 hover:underline">Back to Dashboard</button>
        </div>
      </div>
    );
  }

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-50 dark:bg-gray-900">
      <div className="bg-white dark:bg-gray-800 shadow-md rounded px-8 pt-6 pb-8 mb-4 w-full max-w-lg">
        <h2 className="text-2xl font-bold mb-6 text-center">Task Details</h2>
        <div className="mb-4">
          <span className="font-bold">Title:</span> {task.title}
        </div>
        <div className="mb-4">
          <span className="font-bold">Description:</span> {task.description || '-'}
        </div>
        <div className="mb-4">
          <span className="font-bold">Due Date:</span> {task.dueDate ? new Date(task.dueDate).toLocaleDateString() : '-'}
        </div>
        <div className="mb-4">
          <span className="font-bold">Priority:</span> {task.priority}
        </div>
        <div className="mb-4">
          <span className="font-bold">Status:</span> {task.status}
        </div>
        {deleteError && <div className="text-red-600 text-center mb-4">{deleteError}</div>}
        <div className="flex justify-between mt-6 gap-4">
          <button onClick={() => navigate('/dashboard')} className="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">Back to Dashboard</button>
          <button
            onClick={handleDelete}
            className="bg-red-600 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
            disabled={deleting}
          >
            {deleting ? 'Deleting...' : 'Delete Task'}
          </button>
        </div>
      </div>
    </div>
  );
};

export default TaskDetailsModal; 