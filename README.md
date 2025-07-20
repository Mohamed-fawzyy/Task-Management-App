# Task Management API

A Spring Boot RESTful API for managing user tasks with robust authentication, custom API responses, pagination, filtering, and comprehensive Swagger documentation.

## Features

### 1. **Authentication & Authorization**
- **JWT-based authentication** for secure access.
- **User registration, login, logout, and token refresh** endpoints.
- **Role-based access control** (User roles managed in the system).

### 2. **Task Management (CRUD)**
- **Create Task:**
  - Endpoint: `POST /api/task-management/v1/new-task`
  - Authenticated users can create new tasks.
- **Read Tasks (Paginated):**
  - Endpoint: `GET /api/task-management/v1/tasks`
  - Returns a paginated, sorted list of the user's tasks.
  - Supports custom pagination (`page`, `size`) and sorting (`sortBy`).
  - Default sort is by `dueDate`.
- **Update Task:**
  - Endpoint: `PUT /api/task-management/v1/tasks/{id}`
  - Authenticated users can update their own tasks.
- **Delete Task:**
  - Endpoint: `DELETE /api/task-management/v1/tasks/{id}`
  - Authenticated users can delete their own tasks.

### 3. **Pagination & Sorting**
- All list endpoints support:
  - `page` (default: 0)
  - `size` (default: 10)
  - `sortBy` (default: `dueDate`, can be any valid field: `id`, `title`, `dueDate`, `priority`, `status`)
- Example: `GET /api/task-management/v1/tasks?page=1&size=5&sortBy=title`

### 4. **Custom API Response Structure**
All endpoints return responses in a consistent format:
```json
{
  "code": 200,
  "timestamp": "2024-07-19T14:57:14.380564Z",
  "message": "Successfully retrieved 10 tasks (sorted by: dueDate)",
  "response": { ... }
}
```
- For paginated endpoints, `response` contains pagination metadata and data.

### 5. **Error Handling**
- Centralized exception handling with meaningful error messages and codes.
- Custom exceptions for common error cases (e.g., not found, forbidden, bad request).

### 6. **Swagger/OpenAPI Documentation**
- All endpoints and DTOs are documented using Swagger annotations.
- Interactive API docs available at `/swagger-ui.html` or `/swagger-ui/index.html` after running the app.

### 7. **Validation**
- Request DTOs use Java Bean Validation for required fields, formats, and constraints.
- Errors are returned in the custom API response format.

## Example Usage

### Create a Task
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

### Get Paginated Tasks
```http
GET /api/task-management/v1/tasks?page=0&size=5&sortBy=title
Authorization: Bearer <token>
```

### Update a Task
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

### Delete a Task
```http
DELETE /api/task-management/v1/tasks/{id}
Authorization: Bearer <token>
```

## Setup & Running

### Prerequisites
- **Java 17+** (required for Spring Boot 3+)
- **Maven** (or use the included Maven Wrapper `./mvnw`)
- **A running PostgreSQL (or your configured) database**
- **Git**

### 1. Clone the repository
```bash
git clone <repo-url>
cd task-management
```

### 2. Configure the database
- Edit `src/main/resources/application.yml` to set your database connection properties:
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
- Ensure your database is running and accessible.
- The app uses **Flyway** for automatic schema migrations. On first run, tables will be created automatically.

### 3. Set environment variables (optional)
You can override properties using environment variables, e.g.:
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/taskdb
export SPRING_DATASOURCE_USERNAME=your_db_user
export SPRING_DATASOURCE_PASSWORD=your_db_password
```

### 4. Build and run the app
- Using Maven Wrapper (recommended):
  ```bash
  ./mvnw clean spring-boot:run
  ```
- Or build a JAR and run:
  ```bash
  ./mvnw clean package
  java -jar target/task-management-*.jar
  ```

### 5. Access the API and Swagger UI
- **Swagger UI:**
  - [http://localhost:8181/swagger-ui.html](http://localhost:8181/swagger-ui.html)
  - or [http://localhost:8181/swagger-ui/index.html](http://localhost:8181/swagger-ui/index.html)
- **API Base URL:**
  - `http://localhost:8181/api/task-management/`

### 6. Testing the API
- Use Swagger UI to interactively test endpoints.
- Or use tools like **Postman** or **curl** with your JWT token.

### 7. Troubleshooting
- **Port already in use:** Change the port in `application.yml` (`server.port: 8181`).
- **Database connection errors:**
  - Ensure DB is running and credentials are correct.
  - Check firewall or network issues.
- **Migrations fail:**
  - Check Flyway logs for errors.
  - Ensure your DB user has permissions to create tables.
- **Swagger UI not loading:**
  - Ensure the app started without errors.
  - Check the port and context path.

## Project Structure
- `controller/` — REST API endpoints
- `service/` — Business logic
- `repository/` — Data access (Spring Data JPA)
- `dto/` — Data transfer objects (requests, responses, pagination)
- `exceptions/` — Custom exception classes and global handler
- `security/` — JWT, authentication, and user management
- `util/` — Common utilities (pagination, sorting, API response)

## License
MIT (or your license here)
