# Axon Framework + Spring Boot 4: DDD, CQRS & Event Sourcing

> A reference implementation of a **Digital Wallet** demonstrating the integration of **Axon Framework** with **Spring Boot 4**, using **PostgreSQL** as the event store and **Apache Kafka** for distributed messaging. This project serves as a blueprint for implementing scalable, event-driven microservices with a strong focus on domain-centric design and modern security standards.

---

## 🏛️ Architectural Foundation

This project is built on modern distributed system patterns:

- **Domain-Driven Design (DDD)**: Business logic lives in Aggregates that define the consistency boundaries.
- **CQRS**: Complete separation of command processing and query reporting.
- **Event Sourcing**: The source of truth is the immutable sequence of domain events persisted in PostgreSQL.
- **Database per Service**: Every microservice owns its private database (PostgreSQL or MongoDB), ensuring strict service isolation.
- **Security-First Architecture**: Implements **OAuth2 & OpenID Connect (OIDC)** using **Spring Authorization Server** for centralized identity and access management.
- **Distributed Event Bus**: **Apache Kafka** handles the distribution of events across service boundaries, while Axon Server is maintained only for local testing/prototyping.

---

## 🏗️ Messaging, Persistence & Security

The project leverages a robust infrastructure stack for production-like reliability:

### 1. Persistence (Event Store)
The **Command Side** uses **PostgreSQL** as the primary Event Store and Token Store. This ensures ACID compliance for event persistence during aggregate state changes.

### 2. Messaging (Event Bus)
**Apache Kafka** acts as the backbone for cross-service communication. When an event is published by an aggregate, it is automatically streamed to Kafka topics.

### 3. Security (OAuth2 / OIDC)
The `identity-service` acts as a **Spring Authorization Server**, issuing JWT tokens to clients. All other microservices (Wallet, Notification) act as **OAuth2 Resource Servers**, validating incoming tokens via the public keys provided by the Authorization Server.

### 4. Service Isolation (Database per Service)
*   **Identity Service**: PostgreSQL (User accounts, Client registrations, Consent)
*   **Wallet Command**: PostgreSQL (Event Store / Token Store / Saga Store)
*   **Wallet Query**: MongoDB (Optimized Read Models)
*   **Notification Service**: Dedicated event consumer state.

---

## 🗂️ Project Structure

```text
digital-wallet/
├── common/                # Shared Language (Events, DTOs, Value Objects)
├── microservices/
│   ├── identity-service/  # OAuth2 Authorization Server (Support for JWT/OIDC)
│   ├── wallet-service/    # Core Domain Logic (Resource Server)
│   │   ├── command/       # Write Side: Aggregates (Database: Postgres Event Store)
│   │   └── query/         # Read Side: Projections (Database: MongoDB Read Model)
│   └── notification-service/ # Integration Service (Resource Server)
├── spring-cloud/
│   ├── eureka/            # Service Discovery
│   └── gateway/           # Edge Service (API Gateway & Security Filter)
└── deployment/            # Infrastructure (Docker Compose)
    ├── postgres/          # Relational Database (Primary Store)
    ├── kafka/             # Distributed Message Broker
    ├── mongodb/           # Document Store (Read Models)
    └── axonserver/        # (Testing Only) Local Command/Query Bus
```

---

## 🔄 Integration Flow

1.  **Authorize**: Client authenticates with `identity-service` (OAuth2) and receives a JWT.
2.  **Command**: Client sends a request with the JWT to the `gateway`, which routes it to `wallet-service/command`.
3.  **Persist**: The command side validates the request, processes the command, and saves the event to the **PostgreSQL Event Store**.
4.  **Publish**: The `Axon Kafka Extension` pushes the event to a Kafka topic.
5.  **Project**: `wallet-service/query` listens to Kafka, consumes the event, and updates its private **MongoDB read model**.

---

## 🛠️ Technology Stack

| Component            | Technology                                     |
|----------------------|------------------------------------------------|
| **Language**         | Java 21                                        |
| **Core Framework**    | Spring Boot 4.0.3                              |
| **Security**         | Spring Authorization Server (OAuth2/OIDC)      |
| **Messaging**        | Apache Kafka (Axon Kafka Extension)            |
| **Event Store**      | PostgreSQL (via JDBC/JPA)                      |
| **Read Models**      | MongoDB (Axon MongoDB Extension)               |
| **Service Control**  | Axon Framework 4.13.0                          |
| **Service Discovery**| Spring Cloud Eureka                            |
| **API Gateway**      | Spring Cloud Gateway                           |

---

## 🚀 Getting Started

### 1. Start Primary Infrastructure
```bash
# Start Kafka, Postgres, and MongoDB
cd deployment/kafka && docker-compose up -d
cd ../postgres && docker-compose up -d
cd ../mongodb && docker-compose up -d
```

### 2. Build and Run
```bash
./gradlew clean build
```
Run services in order: **Eureka**, **Gateway**, **Identity Service** (Auth Server), then the **Wallet** modules.

---

## 👤 Author
**Kung Sovannda**
- GitHub: [@kungsovannda](https://github.com/kungsovannda)