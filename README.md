# Axon Framework + Spring Boot 4: DDD, CQRS & Event Sourcing

> A reference implementation of a **Digital Wallet** demonstrating the integration of **Axon Framework** with **Spring Boot 4**, using **PostgreSQL** as the event store and **Apache Kafka** for distributed messaging. This project serves as a blueprint for implementing scalable, event-driven microservices with a strong focus on domain-centric design and modern security standards.

---

## 🏛️ Architectural Foundation

This project is built on modern distributed system patterns:

- **Domain-Driven Design (DDD)**: Business logic lives in Aggregates that define the consistency boundaries.
- **CQRS**: Complete separation of command processing and query reporting.
- **Event Sourcing**: The source of truth is the immutable sequence of domain events persisted in PostgreSQL.
- **Database per Service**: Every microservice owns its private database (PostgreSQL or MongoDB), ensuring strict service isolation.
- **Security-First Architecture**: Implements **OAuth2 & OpenID Connect (OIDC)** using **Spring Authorization Server**.
- **Distributed Event Bus**: **Apache Kafka** handles the distribution of events across service boundaries.

---

## 🎓 Educational Focus

This project is designed as a learning resource. Key implementation details to explore:

1.  **Aggregate Modeling**: See `wallet-service/command` for how to implement state-changing logic and event application using Axon's `@Aggregate`.
2.  **Service Isolation**: Notice the `deployment/postgres` configuration where separate database containers are used for each service to demonstrate the **Database per Service** pattern.
3.  **Cross-Service Communication**: Trace how an event published in the `command` side travels through **Kafka** to reach the `query` side and the `notification-service`.
4.  **JWT Security**: Explore how the `gateway` and microservices act as Resource Servers, validating tokens issued by the `identity-service`.
5.  **Event Store Strategy**: Observe the use of **PostgreSQL** as an Event Store, a common alternative to Axon Server for teams already comfortable with RDBMS.

---

## 🏗️ Messaging, Persistence & Security

### 1. Persistence (Event Store)
The **Command Side** uses **PostgreSQL** as the primary Event Store. This ensures ACID compliance for event persistence during aggregate state changes.

### 2. Messaging (Event Bus)
**Apache Kafka** acts as the backbone for cross-service communication. Events are automatically streamed to Kafka topics via the Axon Kafka Extension.

### 3. Security (OAuth2 / OIDC)
The `identity-service` acts as a **Spring Authorization Server**. All other microservices (Wallet, Notification) act as **OAuth2 Resource Servers**.

### 4. Service Isolation (Database per Service)
*   **Identity Service**: `dw-identity` (Postgres)
*   **Wallet Command**: `dw-postgres` (Postgres Event Store)
*   **Wallet Query**: `mongodb-dw` (MongoDB Read Models)
*   **Notification Service**: `dw-notification` (Postgres)

---

## 🗂️ Project Structure

```text
digital-wallet/
├── common/                # Shared Language (Events, DTOs, Value Objects)
├── microservices/
│   ├── identity-service/  # OAuth2 Authorization Server (Support for JWT/OIDC)
│   ├── wallet-service/    # Core Domain Logic
│   │   ├── command/       # Write Side: Aggregates (Database: Postgres Event Store)
│   │   └── query/         # Read Side: Projections (Database: MongoDB Read Model)
│   └── notification-service/ # Integration Service (Resource Server)
├── spring-cloud/
│   ├── eureka/            # Service Discovery
│   └── gateway/           # Edge Service (API Gateway & Security Filter)
└── deployment/            # Infrastructure (Docker Compose)
    ├── postgres/          # Separate Postgres containers for service isolation
    ├── kafka/             # Distributed Message Broker (Kraft mode)
    ├── mongodb/           # Document Store for Read Models
    └── axonserver/        # (Testing Only) Local Command/Query Bus
```

---

## 🚀 Getting Started

### 1. Start Infrastructure
To spin up all required infrastructure components (PostgreSQL, Kafka, MongoDB, and Axon Server), simply run:

```bash
docker-compose up -d
```

### 2. Build and Run
```bash
./gradlew clean build
```

Run the microservices in the following order:
1. **Eureka**: `spring-cloud/eureka`
2. **Gateway**: `spring-cloud/gateway`
3. **Identity**: `microservices/identity-service`
4. **Wallet**: `command` and `query` modules.

---

## 👤 Author
**Kung Sovannda**
- GitHub: [@kungsovannda](https://github.com/kungsovannda)