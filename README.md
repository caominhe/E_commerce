# E-Commerce Backend

Basic E-Commerce Backend project built with Java and Spring Boot.

## Technologies

* Java 21
* Spring Boot
* Maven
* MySQL
* JPA / Hibernate
* REST API

## Architecture

The project uses MVC and Layered Architecture.

```text
Client
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
MySQL Database
```

## Project Structure

```text
src/main/java/com/example/ecommerce
├── controller
├── service
├── repository
├── entity
├── dto
├── exception
└── config
```

## Configuration

The project uses `application.yaml` for Spring Boot configuration.

Database credentials should be provided through environment variables:

```text
DB_USERNAME
DB_PASSWORD
```

Example:

```yaml
spring:
  datasource:
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

## How to Run

### 1. Clone the project

```bash
git clone https://github.com/caominhe/E_commerce.git
cd E_commerce
```

### 2. Configure database

Create a MySQL database for the project and configure the required environment variables.

### 3. Run the application

Using Maven:

```bash
mvn spring-boot:run
```

Or run the main class:

```text
EcommerceApplication
```

## Current Status

Basic Spring Boot E-Commerce Backend project.

The project is being developed step by step as part of Java Backend learning.
