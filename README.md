# 🔐 Secure Authentication System

A production-style authentication and authorization backend built using Spring Boot.

---

## 🚀 Features

- User Registration & Login
- JWT-based Stateless Authentication
- Refresh Token Mechanism
- Role-Based Access Control (USER / ADMIN)
- Soft Delete Support
- Pagination & Search
- Global Exception Handling
- Swagger API Documentation
- MySQL Database Integration
- Flyway Migration Support

---

## 🛠 Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- JWT
- MySQL
- JPA / Hibernate
- Flyway
- Swagger (OpenAPI)
- Maven

---

## 🔐 Authentication Flow

1. User registers
2. User logs in → Access Token + Refresh Token generated
3. Access token used for protected APIs
4. When expired → Refresh token generates new access token
5. Role-based access control restricts endpoints

---

## 📂 Project Structure

src/main/java/com/example/authsystem │
├── config 
├── controller
├── dto
├── entity 
├── exception 
├── repository
├── security 
├── service

---

## 🔑 API Endpoints

### Auth APIs
- POST /api/auth/register
- POST /api/auth/login
- POST /api/auth/refresh
- POST /api/auth/logout

### User APIs
- GET /api/users/me
- PUT /api/users/profile

### Admin APIs
- GET /api/admin/users
- GET /api/admin/users/{id}
- DELETE /api/admin/users/{id}

---

## ⚙️ How To Run

1. Clone repository
2. Configure MySQL
3. Update application.properties
4. Run: mvn clean install mvn spring-boot:run

## Swagger URL: http://localhost:8081/swagger-ui.html


## 🔗 Live Swagger URL Link: https://authsystem-ar4f.onrender.com/swagger-ui/index.html

---

## 📌 Author

Developed by [Utkarsh Kumar Dabgarwal]
linkdin -:https://www.linkedin.com/in/utkarshkumardabgarwal-9001571b1?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app
