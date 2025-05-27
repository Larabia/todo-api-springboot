# ✅ Task Management API – Spring Boot

A simple task management REST API built with Spring Boot.  
Supports CRUD operations, filtering, validation, PostgreSQL database integration and Docker deployment.

---

## 🧰 Technologies Used

- Java 17
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- H2 (in-memory DB)
- Jakarta Bean Validation
- Lombok
- RESTful API principles
- Global error handling (`@RestControllerAdvice`)
- Git + GitHub
- Docker

---

## 📦 Requirements

- Java 17+
- Maven 3.9+
- Docker & Docker Compose (optional, for containerized setup)

---

## ▶️ Run Locally

### Option 1 – Using Maven and local PostgreSQL

1. Create a PostgreSQL database:
   - DB name: `todo_api`
   - User: `sa`
   - Password: `nuevaClaveSegura123`

2. Configure your `application-dev.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5434/todo_api
spring.datasource.username=sa
spring.datasource.password=nuevaClaveSegura123```

2. Run the app:

```bash
./mvnw spring-boot:run
```

### Option 2 – Run everything with Docker

1. Build the app:

```bash
./mvnw clean package
```

1. Start the containers:

```bash
docker-compose up --build
```

### The app will be available at: http://localhost:8080/api/tasks

---

## 🧪 Run Tests

```bash
./mvnw test
```
### Integration tests use H2 in-memory database with test profile.

---

## 🐳 Docker Details

- PostgreSQL runs on port 5434 (host)

- Spring Boot app runs on port 8080

- DB credentials are configured via environment variables in docker-compose.yml

---

## 📦 Project Structure
com.larabia.todo_api
├── controller // REST endpoints
├── dto // Request DTOs with validation
├── entity // JPA entities
├── exception // Custom exception handling
├── repository // Spring Data interfaces
├── service // Business logic
└── TodoApiApplication // Spring Boot main class

---

## 📌 Endpoints

### 🔹 `GET /api/tasks`
Returns all tasks.

### 🔹 `GET /api/tasks/{id}`
Returns a task by its ID.

### 🔹 `POST /api/tasks`
Creates a new task.

**Request body example:**
```json
{
  "title": "Start Spring Boot project",
  "description": "Build task CRUD with validation",
  "dueDate": "2025-06-01"
}
```

### 🔹 `PUT /api/tasks/{id}`
Updates an existing task entirely.
Returns 404 if not found, 422 if the task is completed.

### 🔹 `PATCH /api/tasks/{id}/complete`
Marks a task as completed.

### 🔹 `PATCH /api/tasks/{id}`
Partially updates title and/or dueDate.

**Partial update body example:**
```json
{
  "title": "Refactor DTO usage",
  "dueDate": "2025-06-10"
}
```

### 🔹 `DELETE /api/tasks/{id}`
Deletes a task by its ID.

---

## 🛑 **Error Handling**

All validation and business rule violations return structured JSON error responses:

### **400 – Validation error:**
```json
{
  "errors": [
    { "field": "title", "message": "Title is required" },
    { "field": "dueDate", "message": "Due date must be today or in the future" }
  ]
}
```
### **404 – Not Found:**
```json
{
 "error": "Task with id 99 not found"
}
```
### **422 – Business rule violation:**
```json
{
  "error": "Completed tasks cannot be edited."
}
```


---

## ✍️ Author

**Lorena Arabia**  
Java Backend Developer  
📌 Argentina
