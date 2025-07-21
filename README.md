# Tasks Management Dashboard

---

## 🚀 Features & Usage

### ✨ Modern, Secure Task Management App

This project is a **production-ready ReactJS dashboard** for managing tasks, built with:
- **React 18** + **Redux Toolkit** for state management
- **TailwindCSS** for beautiful, responsive UI with dark mode support
- **Axios** for secure API calls with automatic token refresh
- **React Router v6** for navigation
- **Spring Boot** backend (see `/api/task-management` endpoints)

---

### 🔐 **Authentication Pages**
- **Register**: Create a new user (first name, last name, email, password)
  - Form validation, error handling, and success redirect to login
  - User details stored for personalized greeting
- **Login**: Secure login with email and password
  - Form validation, error handling, and redirect to dashboard
  - User details retrieved for personalized greeting
- **JWT-based Auth**: Access/refresh tokens are securely managed with automatic refresh
- **Logout**: Invalidate session and clear tokens
- **Token Management**: Automatic token refresh on expiration, silent retry of failed requests

---

### 🗂️ **Dashboard & Task Management**
- **Dashboard**: View your tasks in a clean, responsive layout
  - **Search functionality**: Real-time search by task title with debounced input
  - **Pagination**: Navigate through large task lists (10 items per page)
  - **Sorting**: Sort by due date, title, priority, or status
  - **User greeting**: Personalized welcome message with user's name or email
  - **Dark mode toggle**: Switch between light and dark themes
  - **Responsive design**: Works perfectly on desktop, tablet, and mobile
- **Create Task**: Add new tasks with title, description, due date, and priority
  - Form validation with real-time feedback
  - Date picker with minimum date validation
  - Error handling with user-friendly messages
- **Edit Task**: Update existing tasks with all fields including status
  - Pre-populated form with current task data
  - Status management (Pending, In Progress, Completed)
- **Task Details**: View complete task information
  - **Delete functionality**: Red delete button to remove tasks
  - Confirmation and error handling for deletions
- **Task Actions**: View and Edit buttons for each task

---

### 🌙 **UI/UX Enhancements**
- **Dark Mode**: Complete dark theme support with proper contrast
  - Toggle between light and dark modes
  - Persistent theme preference
  - Proper text contrast in all input fields
- **TailwindCSS**: Modern, mobile-first design
- **Responsive Design**: Optimized for all screen sizes
- **Form Styling**: Consistent input styling with proper dark mode support
- **Button States**: Loading states, disabled states, and hover effects
- **Error Handling**: User-friendly error messages and validation feedback

---

### 🔍 **Search & Filtering**
- **Real-time Search**: Debounced search by task title
  - Backend-powered search endpoint integration
  - Instant results as you type
- **Pagination Controls**: Previous/Next buttons with page information
- **Sort Options**: Sort by due date, title, priority, or status
- **Status Indicators**: Color-coded priority and status badges

---

## 🛠️ **Getting Started**

1. **Install dependencies:**
   ```sh
   npm install
   ```

2. **Start the development server:**
   ```sh
   npm start
   ```
   The app runs at [http://localhost:3000](http://localhost:3000)

3. **Build for production:**
   ```sh
   npm run build
   ```

---

## 🔗 **Backend API Integration**
- **Base URL**: Configured to connect to Spring Boot backend
- **Authentication**: JWT token management with automatic refresh
- **API Endpoints**: 
  - Task CRUD operations
  - Search functionality (`/v1/tasks/search`)
  - Pagination and sorting support
- **Error Handling**: Comprehensive error handling for network issues and API errors

---

## 📄 **Project Structure**
- `src/pages/` — Main app pages (Login, Register, Dashboard, CreateTask, EditTask, TaskDetails)
- `src/components/` — Reusable UI components
- `src/store/` — Redux Toolkit store and slices (auth, tasks)
- `src/services/` — API service modules (authService, tasksService)
- `src/hooks/` — Custom React hooks
- `src/utils/` — Utility functions and helpers

---

## 🆕 **Recent Updates & Features**

### **Authentication Enhancements**
- ✅ User details storage and retrieval for personalized experience
- ✅ Automatic token refresh on expiration
- ✅ Silent retry of failed requests due to token expiration
- ✅ Improved error handling for authentication failures

### **Task Management Improvements**
- ✅ **Search functionality**: Backend-powered search by task title
- ✅ **Delete task**: Red delete button in task details view
- ✅ **Enhanced pagination**: Better page navigation with total count
- ✅ **Improved sorting**: Multiple sort options with visual feedback

### **UI/UX Improvements**
- ✅ **Dark mode**: Complete dark theme with proper contrast
- ✅ **Form styling**: Consistent input styling across all forms
- ✅ **Responsive design**: Mobile-optimized layout
- ✅ **Loading states**: Better user feedback during operations
- ✅ **Error handling**: Comprehensive error messages and validation

### **Technical Improvements**
- ✅ **Debounced search**: Performance optimization for search input
- ✅ **Token management**: Robust JWT token handling
- ✅ **State management**: Improved Redux store structure
- ✅ **API integration**: Better error handling and response processing

---

## 💡 **Contributing & Customization**
- Fork, clone, and PRs welcome!
- Easily extend with new features, themes, or integrations
- Follow existing patterns for consistency

---

**Enjoy your modern, secure, and beautiful task management dashboard!**
