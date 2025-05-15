# ✅ Task Management API – Spring Boot

This is a RESTful API for managing tasks, built with Java and Spring Boot.  
It includes field validation, error handling, custom exception mapping, and partial updates.

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


## ▶️ Run Locally

1. Clone the repo:

```bash
git clone https://github.com/Larabia/todo-api-springboot.git
```

2. Navigate into the project and run:

```bash
./mvnw spring-boot:run
```

3. Access the app:

API: http://localhost:8080/api/tasks

H2 Console: http://localhost:8080/h2-console

---

## ✍️ Author

**Lorena Arabia**  
Java Backend Developer  
📌 Argentina
