# 🧠 Task Management API

A Spring Boot RESTful API for managing user tasks with robust authentication, custom API responses, pagination, filtering, search functionality, and comprehensive Swagger documentation.

---

## 🚀 Features

### 🔐 1. Authentication & Authorization
- ✅ **JWT-based authentication** for secure access with access and refresh tokens
- 📝 **User Registration, Login, Logout, Token Refresh**
- 🔒 **Role-based access control** (Only users with `ROLE_USER` can manage tasks)
- 🔄 **Automatic token refresh** mechanism for seamless user experience
- 🛡️ **Secure token storage** and validation

### 🧾 2. Task Management (CRUD)
| Operation | Method & Endpoint | Auth Required | Description |
|----------|--------------------|---------------|-------------|
| 🔨 Create Task | `POST /api/task-management/v1/new-task` | ✅ Yes | Create new task with title, description, due date, priority |
| 📋 Get Tasks (Paginated) | `GET /api/task-management/v1/tasks` | ✅ Yes | Get paginated list of user's tasks with sorting |
| 🔍 Search Tasks | `GET /api/task-management/v1/tasks/search` | ✅ Yes | Search tasks by title with pagination and sorting |
| 🛠 Update Task | `PUT /api/task-management/v1/tasks/{id}` | ✅ Yes | Update existing task including status changes |
| ❌ Delete Task | `DELETE /api/task-management/v1/tasks/{id}` | ✅ Yes | Delete task by ID |

### 🔍 3. Search & Filtering
- ✅ **Title-based search**: Case-insensitive search by task title
- ✅ **Pagination support**: Configurable page size and page number
- ✅ **Sorting options**: Sort by id, title, dueDate, priority, status
- ✅ **User-scoped results**: Users can only search their own tasks

---

## 🔐 Authentication Endpoints

| Operation | Method & Endpoint | Public | Description |
|----------|--------------------|--------|-------------|
| 🧑‍💻 Register | `POST /api/auth/register` | ✅ Yes | Register new user with first name, last name, email, password |
| 🔓 Login | `POST /api/auth/login` | ✅ Yes | Authenticate user and return access/refresh tokens |
| 🔁 Refresh Token | `POST /api/auth/refresh-token` | ✅ Yes | Get new access token using refresh token |
| 🚪 Logout | `POST /api/auth/logout` | ✅ Yes | Invalidate refresh token (requires refresh token) |

> 🛡️ **Note**: All `task-management` endpoints require a **valid `access token`** in the `Authorization` header with `Bearer <token>`, and the user must have the role `ROLE_USER`.

---

## 📦 Pagination & Sorting

All list endpoints support:

- `page` (default: `0`) - Page number (zero-based)
- `size` (default: `10`) - Number of items per page
- `sortBy` (default: `dueDate`) - Sort field (id, title, dueDate, priority, status)
- `sortDirection` (default: `ASC`) - Sort direction (ASC, DESC)

📌 Example:
```http
GET /api/task-management/v1/tasks?page=1&size=5&sortBy=title&sortDirection=DESC
Authorization: Bearer <token>
```

---

## 🔍 Search Endpoint

### Search Tasks by Title
```http
GET /api/task-management/v1/tasks/search?title=report&page=0&size=10&sortBy=dueDate
Authorization: Bearer <token>
```

**Parameters:**
- `title` (required) - Search term for task title (case-insensitive)
- `page` (optional) - Page number (default: 0)
- `size` (optional) - Items per page (default: 10)
- `sortBy` (optional) - Sort field (default: dueDate)
- `sortDirection` (optional) - Sort direction (default: ASC)

**Response:**
```json
{
  "code": 200,
  "timestamp": "2024-07-19T14:57:14.380564Z",
  "message": "Successfully retrieved 5 tasks matching 'report'",
  "response": {
    "content": [...],
    "totalElements": 5,
    "totalPages": 1,
    "currentPage": 0,
    "pageSize": 10
  }
}
```

---

## 📡 Custom API Response Structure

All endpoints return JSON responses in this format:
```json
{
  "code": 200,
  "timestamp": "2024-07-19T14:57:14.380564Z",
  "message": "Successfully retrieved 10 tasks (sorted by: dueDate)",
  "response": { ... }
}
```

---

## ❌ Error Handling

- Centralized exception handling with:
  - Informative error messages
  - HTTP status codes
  - Custom exception types:
    - `ResourceNotFoundException` (404)
    - `ResourceBadRequestException` (400)
    - `ResourceNotAuthorizedException` (401)
    - `ResourceForbiddenException` (403)
    - `ResourceNotProcessableException` (422)
    - `DuplicateResourceException` (409)
    - `InternalServerErrorException` (500)

---

## 🧪 Example Requests

### 🔨 Create Task
```http
POST /api/task-management/v1/new-task
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Finish report",
  "description": "Complete the quarterly financial report",
  "dueDate": "2024-07-31",
  "priority": "HIGH"
}
```

### 📋 Get Paginated Tasks
```http
GET /api/task-management/v1/tasks?page=0&size=5&sortBy=title&sortDirection=DESC
Authorization: Bearer <token>
```

### 🔍 Search Tasks
```http
GET /api/task-management/v1/tasks/search?title=report&page=0&size=10&sortBy=dueDate
Authorization: Bearer <token>
```

### 🛠 Update Task
```http
PUT /api/task-management/v1/tasks/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Finish report (updated)",
  "description": "Update the quarterly financial report",
  "dueDate": "2024-08-01",
  "priority": "MEDIUM",
  "status": "IN_PROGRESS"
}
```

### ❌ Delete Task
```http
DELETE /api/task-management/v1/tasks/{id}
Authorization: Bearer <token>
```

---

## 🆕 **Recent Updates & New Features**

### **New Endpoints**
- ✅ **Search Tasks**: `GET /api/task-management/v1/tasks/search` - Search tasks by title
- ✅ **Enhanced Pagination**: Better pagination response with total counts
- ✅ **Improved Sorting**: Support for multiple sort fields and directions

### **Authentication Enhancements**
- ✅ **Token Refresh**: Automatic refresh token mechanism
- ✅ **User Details**: Enhanced user information in authentication responses
- ✅ **Security Improvements**: Better token validation and error handling

### **Task Management Improvements**
- ✅ **Status Management**: Full CRUD support for task status
- ✅ **Priority Support**: LOW, MEDIUM, HIGH priority levels
- ✅ **Date Validation**: Proper date format validation (yyyy-MM-dd)
- ✅ **User Scoping**: Users can only access their own tasks

### **API Response Improvements**
- ✅ **Consistent Format**: Standardized API response structure
- ✅ **Better Error Messages**: More descriptive error responses
- ✅ **Pagination Metadata**: Enhanced pagination information

### **Database & Performance**
- ✅ **Flyway Migrations**: Automated database schema management
- ✅ **Indexing**: Optimized database queries for search and sorting
- ✅ **Connection Pooling**: Improved database connection management

---

## 🧰 Setup & Running

### ⚙️ Prerequisites
- Java 17+
- Maven (or use `./mvnw`)
- PostgreSQL running
- Git

### 📥 1. Clone the repository
```bash
git clone <repo-url>
cd task-management
```

### 🧾 2. Configure the Database
Edit `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/taskdb
    username: your_db_user
    password: your_db_password
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
  flyway:
    enabled: true
    baseline-on-migrate: true
```

🧪 Uses Flyway for auto schema migration with versioned migrations.

---

### 🌐 3. Set Environment Variables (Optional)
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/taskdb
export SPRING_DATASOURCE_USERNAME=your_db_user
export SPRING_DATASOURCE_PASSWORD=your_db_password
```

---

### ▶️ 4. Build & Run the App

Using Maven Wrapper:
```bash
./mvnw clean spring-boot:run
```

Or:
```bash
./mvnw clean package
java -jar target/Task-Management.jar
```

---

## 🧭 API Access

- **Swagger UI**:  
  [http://localhost:8181/swagger-ui.html](http://localhost:8181/swagger-ui.html)  
  or  
  [http://localhost:8181/swagger-ui/index.html](http://localhost:8181/swagger-ui/index.html)

- **API Base URL**:  
  `http://localhost:8181/api/task-management/`

- **Health Check**:  
  `http://localhost:8181/actuator/health`

---

## 🧪 Testing the API

- 📘 Use Swagger UI for interactive testing
- 🛠 Use Postman/curl with JWT tokens for manual testing
- 🧪 Unit tests available in `src/test/`

---

## 🛠 Troubleshooting

| Issue | Solution |
|-------|----------|
| Port conflict | Change `server.port` in `application.yml` |
| DB errors | Check DB is running and credentials are correct |
| Flyway errors | Ensure DB user has migration permissions |
| Swagger not loading | Ensure app started without exceptions |
| Search not working | Verify database indexes are created |
| Token refresh issues | Check refresh token validity and expiration |

---

## 🗂 Project Structure

```
src/main/java/com/trading/task_management/
├── config/              # Configuration classes (Cache, OpenAPI, Security)
├── exceptions/          # Custom exception handlers
├── security/           # JWT, auth filters, roles, user management
│   ├── config/         # Security configuration
│   ├── controller/     # Auth endpoints
│   ├── dto/           # Auth DTOs
│   ├── entity/        # User and token entities
│   ├── repository/    # User repositories
│   └── service/       # Auth services
├── tasks/             # Task management
│   ├── controller/    # Task REST endpoints
│   ├── dto/          # Task DTOs
│   ├── entity/       # Task entity
│   ├── repository/   # Task repositories
│   └── service/      # Task business logic
├── util/             # Utility classes (pagination, sorting, etc.)
└── TaskManagementApplication.java
```

---

## 📊 Database Schema

### Users Table
- `id` (Primary Key)
- `first_name`, `last_name`, `email`, `password`
- `role` (ROLE_USER)
- `created_at`, `updated_at`

### Tasks Table
- `id` (Primary Key)
- `title`, `description`, `due_date`
- `priority` (LOW, MEDIUM, HIGH)
- `status` (PENDING, IN_PROGRESS, COMPLETED)
- `user_id` (Foreign Key to Users)
- `created_at`, `updated_at`

### Refresh Tokens Table
- `id` (Primary Key)
- `token`, `expiry_date`
- `user_id` (Foreign Key to Users)

---

## ⚖️ License

MIT (or your preferred license)
