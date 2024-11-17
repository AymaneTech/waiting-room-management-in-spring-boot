# WRM: Waiting Room Management 📊

A centralized API for managing waiting rooms, designed to optimize visitor flow, scheduling, and performance statistics. Built with Spring Boot and Domain-Driven Design (DDD) principles.

![Project Status](https://img.shields.io/badge/Status-In%20Development-yellow)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)

## 📑 Table of Contents
- [Overview](#overview)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Key Features](#key-features)
- [Getting Started](#getting-started)
- [Development](#development)
- [API Documentation](#api-documentation)
- [Testing](#testing)

## Overview

WRM is a powerful API designed for managing waiting rooms across various organizations. Whether it's medical offices, public services, or businesses, WRM helps streamline visitor flow management, provides flexible scheduling algorithms, and delivers performance analytics such as visitor satisfaction rates and average wait times.

## Architecture

This project follows Domain-Driven Design (DDD) principles and is built using Spring Boot. The architecture is modular, focusing on clear separation of concerns for scalability and maintainability.

### Bounded Contexts:
- Waiting Room Management (Core Domain)
- Visitor Scheduling (Supporting Domain)
- Performance Analytics (Supporting Domain)

![Class Diagram](src/main/resources/assets/wrm-architecture.png)

## Tech Stack

### Core Technologies
- Java 21
- Spring Boot 3.2.0
- Spring Data JPA
- PostgreSQL 16
- Docker & Docker Compose

### Development Tools & Practices
- Test-Driven Development (TDD)
- Domain-Driven Design (DDD)
- Maven
- JUnit 5
- AssertJ

## Key Features

- 👥 Dynamic visitor management (track visitor status: "Waiting", "In Progress", "Completed", "Cancelled")
- 🛠 Configurable scheduling algorithms (FIFO, Priority, Shortest Job First)
- ⏰ Customizable service hours and capacity (weekly schedules, continuous, or segmented service times)
- 📊 Performance tracking (satisfaction rates, average wait times, visitor rotation)
- 🔒 Secure API access with authentication

## Getting Started

### Prerequisites
- JDK 21
- Docker & Docker Compose
- Maven 3.8+

### Installation

1. Clone the repository
```bash
git clone https://github.com/yourusername/wrm.git
cd wrm
```

2. Start the PostgreSQL container
```bash
docker-compose up -d
```

3. Build the application
```bash
./mvnw clean install
```

4. Run the application
```bash
./mvnw spring-boot:run
```

The application will be available at `http://localhost:8080`

## Development

### Project Structure
```
wrm/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/wrm/
│   │   │       ├── waitingroom/
│   │   │       ├── scheduling/
│   │   │       └── analytics/
│   │   └── resources/
│   └── test/
└── docker/
```

### API Endpoints

#### Visitor Management
```
# Visitors
GET    /api/v1/visitors              # List all visitors
POST   /api/v1/visitors              # Add new visitor
GET    /api/v1/visitors/{id}         # Get visitor details
PUT    /api/v1/visitors/{id}         # Update visitor status
DELETE /api/v1/visitors/{id}         # Delete visitor

# Visitor Status
GET    /api/v1/visitors/{id}/status  # Get current status of visitor
POST   /api/v1/visitors/{id}/status  # Update status (Waiting, In Progress, Completed, Cancelled)

# Scheduling Algorithms
POST   /api/v1/schedules/algorithm   # Set or update scheduling algorithm (FIFO, Priority, Shortest Job First)
```

#### Performance Analytics
```
GET    /api/v1/analytics/average-wait-time # Get average wait time across all visitors
GET    /api/v1/analytics/satisfaction-rate # Get satisfaction rate
GET    /api/v1/analytics/visitor-rotation  # Get visitor rotation stats
```

### Example Request/Response:

```json
# POST /api/v1/visitors
Request:
{
    "name": "John Doe",
    "visitorType": "VIP",
    "expectedServiceTime": 15
}

Response:
{
    "id": 1,
    "name": "John Doe",
    "visitorType": "VIP",
    "status": "Waiting",
    "expectedServiceTime": 15,
    "arrivalTime": "2024-11-18T10:00:00Z"
}
```

## Caching Configuration

Redis is used for caching:
- Visitor statuses
- Frequently accessed scheduling data

Cache configuration example in `application.yml`:
```yaml
spring:
  cache:
    type: redis
    redis:
      time-to-live: 600000
      cache-null-values: false
  redis:
    host: localhost
    port: 6379
```

## Testing

The project follows Test-Driven Development (TDD) practices:

```bash
# Run unit tests
./mvnw test

# Run integration tests
./mvnw verify -P integration-test
```

## API Documentation

Once the application is running, you can access:
- Swagger UI: `http://localhost:8080/swagger-ui.html`