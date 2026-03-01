# Digital Wallet — Axon Framework + Kafka + MongoDB

> A microservices application implementing **Domain-Driven Design (DDD)**, **CQRS**, and **Event Sourcing** using **Axon Framework** with **Apache Kafka** as the event bus and **MongoDB** as the event store and read model storage — built on top of **Spring Boot** and **Spring Cloud**.

---

## 📐 Architecture Overview

This project separates concerns across three core patterns:

| Pattern | How it's applied |
|---|---|
| **DDD** | Business logic lives inside Aggregates, with shared contracts (Commands, Events, Queries) defined in the `common` module |
| **CQRS** | `wallet-service` is split into a `command` module (write side) and a `query` module (read side) |
| **Event Sourcing** | Every state change is stored as an immutable Domain Event in MongoDB via the Axon MongoDB Extension |

---

## 🗂️ Project Structure

```
digital-wallet/
│
├── common/                        # Shared contracts across all microservices
│   ├── dto/                       # Data Transfer Objects
│   ├── vo/                        # Value Objects (WalletId, Money, etc.)
│   └── event/                     # Domain Events published via Axon Event Bus
│
├── microservices/
│   ├── identity-service/          # User identity, authentication & authorization
│   │
│   ├── wallet-service/
│   │   ├── command/               # Write side: Aggregates, Command Handlers, @EventSourcingHandlers
│   │   └── query/                 # Read side: Event Handlers, Projections, Query Handlers
│   │
│   └── notification-service/      # Listens to domain events and sends notifications
│
├── spring-cloud/
│   ├── gateway/                   # API Gateway — single entry point for all client requests
│   └── eureka-server/             # Service Discovery — registry for all microservices
│
├── deployment/                    # Docker Compose / infrastructure configs
├── gradle/wrapper/
├── settings.gradle
├── gradlew
└── gradlew.bat
```

---

## 🔍 Module Responsibilities

### `common/`
The shared library included by all microservices. It defines the language of the system:
- **`dto/`** — request/response objects passed over the wire
- **`vo/`** — immutable Value Objects that carry domain meaning (e.g. `Money`, `WalletId`)
- **`event/`** — Domain Events that represent facts (`WalletCreatedEvent`, `MoneyWithdrawnEvent`). These are published by Axon and consumed by any interested service

### `microservices/identity-service/`
Handles user registration, login, and token issuance. Other services trust the identity provided by this service through the API Gateway.

### `microservices/wallet-service/command/`
The **write side** of the wallet bounded context. Contains:
- Axon `@Aggregate` classes that enforce business rules
- `@CommandHandler` methods that process incoming commands
- `@EventSourcingHandler` methods that rebuild aggregate state from events stored in MongoDB

### `microservices/wallet-service/query/`
The **read side** of the wallet bounded context. Contains:
- `@EventHandler` methods that build and update MongoDB read model collections
- `@QueryHandler` methods that serve queries from those read models
- Projection classes (views) stored in MongoDB

### `microservices/notification-service/`
A downstream consumer. Subscribes to domain events from the Axon Event Bus (distributed via Kafka) and triggers notifications (email, push, etc.) without coupling to any other service.

### `spring-cloud/gateway/`
The single entry point for all external HTTP traffic. Routes requests to the appropriate microservice via Eureka-based service discovery.

### `spring-cloud/eureka-server/`
The service registry. All microservices register here on startup so the Gateway and other services can locate them dynamically.

### `deployment/`
Docker Compose files to spin up the full infrastructure stack: MongoDB, Apache Kafka, Postgres.

---

## 🔁 Event Flow

```
Client HTTP Request
        │
        ▼
  [ API Gateway ]
        │
        ▼
  [ identity-service ]  ← authenticates the request
        │
        ▼
  [ wallet-service/command ]
        │  @CommandHandler → AggregateLifecycle.apply(event)
        ▼
  [ Domain Event ]
        │
        ├──► [ MongoDB: domainevents ]     ← persisted as source of truth (Axon MongoDB Extension)
        │
        └──► [ Kafka topic ]               ← distributed to other services (Axon Kafka Extension)
                    │
                    ├──► [ wallet-service/query ]     → updates MongoDB read model
                    │
                    └──► [ notification-service ]     → sends notification to user

Client Query Request
        │
        ▼
  [ API Gateway ]
        │
        ▼
  [ wallet-service/query ]
        │  @QueryHandler reads from MongoDB projection
        ▼
  [ HTTP Response ]
```

---

## 🛠️ Tech Stack

| Technology | Role |
|---|---|
| Java 17+ | Primary language |
| Spring Boot 3.x | Application framework |
| Axon Framework 4.x | CQRS / Event Sourcing / DDD backbone |
| Axon Kafka Extension | Distributes domain events across microservices |
| Axon MongoDB Extension | Event Store, Token Store, Saga Store |
| Apache Kafka | Message broker for cross-service event streaming |
| MongoDB | Event store + query-side read models |
| Spring Cloud Gateway | API Gateway |
| Eureka Server | Service discovery & registry |
| Gradle | Multi-module build tool |
| Docker Compose | Local infrastructure orchestration |

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Docker & Docker Compose

### 1. Start Infrastructure

```bash
cd deployment
docker-compose up -d
```

Starts MongoDB, Postgres, and Kafka.

### 2. Build All Modules

```bash
./gradlew clean build
```

### 3. Start Services in Order

```bash
# 1. Eureka Server
cd spring-cloud/eureka-server && ./gradlew bootRun

# 2. API Gateway
cd spring-cloud/gateway && ./gradlew bootRun

# 3. Identity Service
cd microservices/identity-service && ./gradlew bootRun

# 4. Wallet Command
cd microservices/wallet-service/command && ./gradlew bootRun

# 5. Wallet Query
cd microservices/wallet-service/query && ./gradlew bootRun

# 6. Notification Service
cd microservices/notification-service && ./gradlew bootRun
```

---

## 👤 Author

**Kung Sovannda**
GitHub: [@kungsovannda](https://github.com/kungsovannda)