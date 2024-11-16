# Task Management API

![Java](https://img.shields.io/badge/java-21-blue)
![Spring Boot](https://img.shields.io/badge/spring--boot-3.0-brightgreen)
![Maven](https://img.shields.io/badge/maven-3-orange)

Task Management API is a simple, yet powerful RESTful API designed to manage tasks in a to-do list. It offers CRUD operations for task management and is secured using Basic Authentication.

---

## About the Project

Task Management API provides endpoints for creating, reading, updating, and deleting tasks. Built with Spring Boot, it leverages modern Java features and tools such as JPA for database operations, H2 for in-memory data storage, and Spring Security for securing endpoints.

Key features include:

- CRUD operations for tasks.
- Basic Authentication for secured access.
- Comprehensive API documentation with OpenAPI.
- Frontend support with CORS configuration.
- CSRF disabled for local testing (configurable for production).
- Automated testing with Rest Assured.
- Code coverage reporting using JaCoCo.

---

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot
- **Build Tool:** Maven 3
- **Database:** H2 (In-Memory Database)
- **ORM:** JPA
- **API Documentation:** OpenAPI 3.0.3 (Swagger UI)
- **Testing:** Rest Assured
- **Code Coverage:** JaCoCo
- **Utilities:** Lombok

---

## Setup

### Prerequisites

To run this project, ensure you have the following installed:

- **Java 21 (corretto-21.0.5)** or higher.
- **Maven 3** or higher.

### Local Setup

#### 1. Install Maven  
   ```bash
   brew install maven
   ```
#### 2. Install a Java version manager  
   Follow the instructions at: https://sdkman.io/install
#### 3. Install the JDK (corretto-21.0.5):
   ```bash
   sdk install java 21.0.5-amzn
   ```
#### 4. Clone the repository  
   ```bash
   git clone https://github.com/fredrikkumlin/task-management-api.git
   ```
   ```bash
   cd task-management-api
   ```
#### 5. Start the app  
   ```bash
   mvn clean install
   ```
   ```bash
   mvn spring-boot:run
   ```
#### 6. Swagger/OpenAPI
   Swagger ui: http://localhost:8080/swagger-ui/index.html  
   Docs: http://localhost:8080/api-docs

   Use the following credentials:  
   username: test  
   password: test

#### 7. Database  
   H2 Console: http://localhost:8080/h2-console

   JDBC URL: jdbc:h2:mem:tasksdb  
   username: sa  
   password: password  


#### 8. Connect the frontend  
   From the url: http://localhost:5173


#### 9. If using IntelliJ  
   Right click on "target/generated-sources" Mark directory as "Generated Sources Root".

