# Hospital Management System Backend

This project is the backend for a Hospital Management System built using a microservice architecture. It manages authentication, user profiles, and fast user search while ensuring secure, high-performance operations. The API Gateway intercepts incoming client requests, delegates authentication to the Identity Microservice, and forwards the request to the appropriate target microservice upon successful authentication.ity Microservice, which then manages Spring Security for authorization across other services.

## Features

- **Microservice Architecture**:
  The system is divided into multiple microservices for scalability and maintainability:

  - **API Gateway**: Entry point for all client requests and is responsible for authenticating users via the Identity Microservice.
  - **Identity Microservice**: Manages authentication, access and refresh tokens, and secure login, integrated with PostgreSQL.
  - **Profile Microservice**: Handles user profiles, integrated with PostgreSQL.
  - **Search Microservice**: Manages user search functionality, integrated with Elasticsearch and Redis for caching.

- **Authentication**:

  - The **API Gateway** is responsible for handling authentication by delegating authentication requests to the **Identity Microservice**.
  - The **Identity Microservice** generates and validates access tokens and refresh tokens.
  - Tokens are stored in **httpOnly cookies** for added security and sent back to the client upon successful authentication.
  - **Spring Security** is used in every microservice to handle authorization based on the authenticated tokens.

- **Real-Time Updates**:

  - **Kafka** listens for user creation and updates from the Identity Microservice and synchronizes the data with the Search Microservice to ensure real-time updates.

- **Caching for Efficiency**:

  - **Redis** is used in the Search Microservice to cache logged-in user information for faster access.

- **High Performance and Scalability**:
  - **PostgreSQL** is used for secure data storage, while Elasticsearch ensures fast and efficient searching.

## Architecture Overview

### 1. API Gateway Microservice

- Handles all incoming client requests.
- Intercepts the requests and calls the **Identity Microservice** to authenticate the user.
- If authentication is successful, the API Gateway forwards the request to the appropriate target microservice for further processing.
- Handles authorization by passing authenticated tokens to downstream services.

### 2. Identity Microservice

- Responsible for user authentication and token management.
- Uses **Spring Security** to manage authorization within the service.
- Generates and validates access tokens and refresh tokens.
- Stores tokens in httpOnly cookies to prevent cross-site scripting (XSS) attacks.

### 3. Profile Microservice

- Manages user profiles and interacts with **PostgreSQL** for data storage.
- Secured with **Spring Security** to ensure only authorized users can access profile data.

### 4. Search Microservice

- Provides fast user search capabilities.
- Integrates with **Elasticsearch** for optimized searches.
- Uses **Redis** to cache logged-in user data for faster access.
- Secured with **Spring Security** to ensure only authorized users can perform search operations.

### 5. Kafka

- Listens for changes in the Identity Microservice (user creation and updates) and syncs them with the Search Microservice to ensure real-time updates.

## Tech Stack

- **Spring Boot** for building microservices.
- **Spring Security** for authentication and authorization.
- **PostgreSQL** for relational database management.
- **Elasticsearch** for fast search functionality.
- **Redis** for caching caching logged-in user data in the Search Microservice.
- **Kafka** for event-driven messaging.
- **JWT** for token-based authentication.
- **httpOnly Cookies** for secure token storage.

## Getting Started

### Prerequisites

- **JDK 11+** installed on your machine.
- **Docker** and **Docker Compose** installed.
- **PostgreSQL** installed and running (if not using Docker).
- **Elasticsearch** and **Redis** set up (if not using Docker).
- **Kafka** set up (if not using Docker).

### Installation

**Step 1: Set up environment variables**

Create a `.env` file in the root directory and add the necessary environment variables for the services:

```bash
DB_USER=your-db-user
DB_PASSWORD=your-db-password
```

**Step 2: Using Docker-Compose to start PostgreSQL, Redis, Elasticsearch, and Kafka in the background**

- If you don’t want Docker compose, you must install and start each service manually:

  - **PostgreSQL** → Install & run on port 5432
  - **Redis** → Install & run on port 6379
  - **Elasticsearch** → Install & run on port 9200
  - **Kafka** → Install & run on port 9092

**Step 3: Clone the Repository**

Clone the repository from GitHub to your local machine and navigate into the project directory:

```bash
git clone https://github.com/chloenth/mycare-portal.git
cd mycare-portal
```

**Step 4: Start Microservices One by One**
Each microservice must be started separately.

**Start API Gateway**

```bash
cd api-gateway
mvn clean install
mvn spring-boot:run
```

Runs on port 8080.

**Start Identity Service**

```bash
cd identity-service
mvn clean install
mvn spring-boot:run
```

Runs on port 8081.

**Start Profile Service**

```bash
cd identity-service
mvn clean install
mvn spring-boot:run
```

Runs on port 8082.

**Start Search Service**

```bash
cd search-service
mvn clean install
mvn spring-boot:run
```

Runs on port 8083.

## API Endpoints

**PUBLIC - POST identity/auth/token**:

- User login endpoint. Returns an access token and refresh token stored in httpOnly cookies.

**PRIVATE - GET identity/auth/logout**:

- reset access token in cookies response and delete refresh token.

**PRIVATE - GET search/users/my-info**:

- Get my info

**PRIVATE - POST identity/users/registration**:

- Add new user

**PRIVATE - GET search/users/{userId}**:

- Get specific user base on userId

**PRIVATE - PUT search/users/{userId}/change-username**:

- Change username of user

**PRIVATE - PUT search/users/{userId}/change-password**:

- Change password of user

**PRIVATE - PUT profile/users/{profileId}**:

- Update user profile

## License

This project is licensed under the **MIT License** - see the **LICENSE** file for details.
