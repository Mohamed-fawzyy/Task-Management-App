# 🧠 Task Management API

A Spring Boot RESTful API for managing user tasks with robust authentication, custom API responses, pagination, filtering, and comprehensive Swagger documentation.

---

## 🚀 Features

### 🔐 1. Authentication & Authorization
- ✅ **JWT-based authentication** for secure access.
- 📝 **User Registration, Login, Logout, Token Refresh**
- 🔒 **Role-based access control** (Only users with `ROLE_USER` can manage tasks).

### 🧾 2. Task Management (CRUD)
| Operation | Method & Endpoint | Auth Required |
|----------|--------------------|---------------|
| 🔨 Create Task | `POST /api/task-management/v1/new-task` | ✅ Yes |
| 📋 Get Tasks (Paginated) | `GET /api/task-management/v1/tasks` | ✅ Yes |
| 🛠 Update Task | `PUT /api/task-management/v1/tasks/{id}` | ✅ Yes |
| ❌ Delete Task | `DELETE /api/task-management/v1/tasks/{id}` | ✅ Yes |

---

## 🔐 Authentication Endpoints

| Operation | Method & Endpoint | Public |
|----------|--------------------|--------|
| 🧑‍💻 Register | `POST /api/auth/register` | ✅ Yes |
| 🔓 Login | `POST /api/auth/login` | ✅ Yes |
| 🔁 Refresh Token | `POST /api/auth/refresh-token` | ✅ Yes |
| 🚪 Logout | `POST /api/auth/logout` | ✅ Yes (requires refresh token) |

> 🛡️ **Note**: All `task-management` endpoints require a **valid `access token`** in the `Authorization` header with `Bearer <token>`, and the user must have the role `ROLE_USER`.

---

## 📦 Pagination & Sorting

All list endpoints support:

- `page` (default: `0`)
- `size` (default: `10`)
- `sortBy` (default: `dueDate`, can be `id`, `title`, `priority`, `status`, etc.)

📌 Example:
```http
GET /api/task-management/v1/tasks?page=1&size=5&sortBy=title
Authorization: Bearer <token>
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
  - Custom exception types (`BadRequestException`, `UnauthorizedException`, etc.)

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
GET /api/task-management/v1/tasks?page=0&size=5&sortBy=title
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
```

🧪 Uses Flyway for auto schema migration.

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
java -jar target/task-management-*.jar
```

---

## 🧭 API Access

- **Swagger UI**:  
  [http://localhost:8181/swagger-ui.html](http://localhost:8181/swagger-ui.html)  
  or  
  [http://localhost:8181/swagger-ui/index.html](http://localhost:8181/swagger-ui/index.html)

- **API Base URL**:  
  `http://localhost:8181/api/task-management/`

---

## 🧪 Testing the API

- 📘 Use Swagger UI for interactive testing
- 🛠 Use Postman/curl with JWT tokens for manual testing

---

## 🛠 Troubleshooting

| Issue | Solution |
|-------|----------|
| Port conflict | Change `server.port` in `application.yml` |
| DB errors | Check DB is running and credentials are correct |
| Flyway errors | Ensure DB user has migration permissions |
| Swagger not loading | Ensure app started without exceptions |

---

## 🗂 Project Structure

```
src
├── controller/       # REST endpoints
├── service/          # Business logic
├── repository/       # JPA repositories
├── dto/              # Data Transfer Objects
├── exceptions/       # Custom error handlers
├── security/         # JWT, auth filters, roles
└── util/             # Utility classes (pagination, sorting, etc.)
```

---

## ⚖️ License

MIT (or your preferred license)
