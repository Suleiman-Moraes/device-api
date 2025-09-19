# Device API

A RESTful API built with **Spring Boot 3.5**, **Java 21**, and **PostgreSQL** for managing device resources.
It supports creation, retrieval, filtering, updating (full and partial), and deletion of devices with proper validations and persistence.
The project is containerized with Docker and integrates database migrations via Flyway.

---

## Features

* **CRUD operations** for devices
* **Filtering & pagination** (by brand, state, name, etc.)
* **Validation rules** to prevent invalid or conflicting updates
* **OpenAPI 3 / Swagger UI** for interactive API documentation
* **Database versioning** with Flyway
* **Containerized setup** with Docker & Docker Compose
* **Actuator endpoints** for health and metrics

---

## Tech Stack

* **Java 21**
* **Spring Boot 3.5.x**

  * Web, Validation, Data JPA, Actuator
* **PostgreSQL 17** (via Docker Compose)
* **Flyway** (database migrations)
* **MapStruct** (DTO ↔ entity mapping)
* **Springdoc OpenAPI** (Swagger UI)
* **Lombok** (boilerplate reduction)

---

## Getting Started

### Prerequisites

* [Java 21+](https://adoptium.net/)
* [Maven 3.9+](https://maven.apache.org/)
* [Docker](https://www.docker.com/) & [Docker Compose](https://docs.docker.com/compose/)

### Build

```bash
mvn clean install
```

### Run with Docker Compose

```bash
docker-compose -f docker-compose-full.yaml up -d
```

This starts:

* **PostgreSQL** (on port `5432`)
* **Device API** (on port `8080`)

### Alternative: Build images manually

```bash
docker build -f Dockerfile -t device_api .
docker build -f Dockerfile2 \
  -t suleimanmoraes/device_api:1.0.0 \
  -t suleimanmoraes/device_api:latest .
```

---

## Testing

This project includes **integration tests** in addition to unit tests.

* **Rest-Assured** is used for testing REST API endpoints.
* **Testcontainers (PostgreSQL)** spins up a real PostgreSQL database inside Docker during the test lifecycle.

### Requirements

To run integration tests locally you must have **Docker** installed and available in your environment, since Testcontainers relies on it.

### Run all tests

```bash
mvn test
```

---

## API Endpoints

### Base URL

```
http://localhost:8080/api/v1/devices
```

### Main operations

| Method | Endpoint                      | Description                         |
| ------ | ----------------------------- | ----------------------------------- |
| GET    | `/devices`                    | Get paginated list of devices       |
| GET    | `/devices/{id}`               | Get device by ID                    |
| GET    | `/devices/state?state=IN_USE` | Get devices by state                |
| GET    | `/devices/brand?brand=ACME`   | Get devices by brand                |
| POST   | `/devices`                    | Create a new device                 |
| PUT    | `/devices/{id}`               | Update an existing device (full)    |
| PATCH  | `/devices/{id}`               | Partially update an existing device |
| DELETE | `/devices/{id}`               | Delete a device by ID               |

---

## Example DTOs

**Create Device Request**

```json
{
  "name": "Laptop X1",
  "brand": "Lenovo",
  "state": "AVAILABLE"
}
```

**Device Response**

```json
{
  "id": 1,
  "name": "Laptop X1",
  "brand": "Lenovo",
  "state": "AVAILABLE",
  "creationTime": "2025-09-19T14:32:45Z"
}
```

---

## Swagger & API Docs

* Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* OpenAPI JSON: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## Development Notes

* **DB migrations** live under `src/main/resources/db/migration`
* **Profiles & configs** can be set via `application.yml`
* **Logs** configurable through `LOG_LEVEL` environment variable
* **Tests** run with `mvn test`

---

## License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).

