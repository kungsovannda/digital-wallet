# Digital Wallet: Axon Framework Microservices

A modern, cloud-native Digital Wallet implementation demonstrating the power of **Domain-Driven Design (DDD)**, **CQRS**, and **Event Sourcing** using the **Axon Framework**. This project serves as a reference architecture for building scalable, resilient, and auditable financial systems.

## �️ Architectural Overview

The project is built on the principle of **CQRS (Command Query Responsibility Segregation)**, separating the "Write" operations from the "Read" operations to allow independent scaling and optimized data models.

*   **Command Side (Write)**: Powered by **PostgreSQL** (Event Store). Every state change is stored as a sequence of events, ensuring a 100% accurate audit log.
*   **Query Side (Read)**: Powered by **MongoDB**. Optimized projections are updated asynchronously to provide high-performance data retrieval.
*   **Event Bus**: **Kafka** is used as the distributed event backbone, ensuring reliable communication between microservices.
*   **Service Discovery & Gateway**: **Spring Cloud Eureka** handles service registration, while **Spring Cloud Gateway** provides a unified API entry point.

## 🛠️ Technology Stack

*   **Core**: Java 21, Spring Boot 4.0.x
*   **Orchestration**: Axon Framework 4.13.0
*   **Message Broker**: Apache Kafka
*   **Security**: OAuth2/OIDC (Spring Authorization Server)
*   **Databases**: PostgreSQL (Command), MongoDB (Query)
*   **Infrastructure**: Spring Cloud Gateway, Eureka, Docker Compose

---

## 📡 API Reference

All requests should be directed through the Gateway at **`http://localhost:8080`**.

### 🔐 Identity & Auth (9090)
| Method | Endpoint | Description |
|:--- |:--- |:--- |
| `GET` | `/login` | Default login page |
| `GET` | `/register` | User registration page |
| `POST` | `/register` | Handle new user registration |
| `GET` | `/api/v1/users` | Retrieve authenticated user details |

### 💳 Wallet Commands (8081)
| Method | Endpoint | Command Triggered |
|:--- |:--- |:--- |
| `POST` | `/api/v1/wallets` | `CreateWalletCommand` |
| `PUT` | `/api/v1/wallets/{id}/deposit` | `DepositMoneyCommand` |
| `PUT` | `/api/v1/wallets/{id}/withdraw` | `WithdrawMoneyCommand` |
| `PUT` | `/api/v1/wallets/{id}/transfer` | `TransferMoneyCommand` |
| `PUT` | `/api/v1/wallets/{id}/freeze` | `FreezeWalletCommand` |

### 🔍 Wallet Queries (8082)
| Method | Endpoint | Query Triggered |
|:--- |:--- |:--- |
| `GET` | `/api/v1/wallets/{id}` | `GetWalletQuery` |
| `GET` | `/api/v1/wallets/my-wallets` | `GetUserWalletsQuery` |
| `GET` | `/api/v1/wallets/status` | `GetWalletsByStatusQuery` |
| `GET` | `/api/v1/wallets/{id}/transactions`| `GetTransactionHistoryQuery` |
| `GET` | `/api/v1/wallets/transactions/{tid}`| `GetTransactionByIdQuery` |

---

## 🚀 Getting Started

### Prerequisites
*   Docker & Docker Desktop
*   JDK 21
*   Gradle

### Running the Infrastructure
The project uses a "Database per Service" approach. All infrastructure components (Postgres, Mongo, Kafka, Kafka-UI) can be started via:

```bash
docker-compose up -d
```

### Accessing Tools
*   **Eureka Dashboard**: `http://localhost:8761`
*   **Kafka UI**: `http://localhost:18000` (Admin/qwer)
*   **Identity Server**: `http://localhost:9090`

## 🛡️ Security Configuration
Access to wallet endpoints requires a valid JWT token issued by the Identity Service.
*   **Issuer**: `http://localhost:9090`
*   **Client ID**: `dw-client`
*   **Client Secret**: `secret`

## 📧 Notifications
The `notification-service` listens to events emitted by the wallet aggregates and automatically dispatches transactional emails (Registration, Deposits, etc.) using high-quality HTML templates.