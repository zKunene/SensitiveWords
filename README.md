# Sensitive Words API

The Sensitive Words API is a Spring Boot–based REST service for storing, managing, and evaluating sensitive words. It exposes typed REST endpoints with Swagger UI and supports both **local development** and **containerized deployment via Docker**.

---

## Technologies Used

* Java 21
* Spring Boot 3.5.x
* Spring Web, Spring Data JPA
* Microsoft SQL Server
* Testcontainers
* Swagger (Springdoc)
* Docker & Docker Compose
* Maven

---

## Running the Application Locally

### Clone the Repository

```bash
git clone https://github.com/<your-username>/<your-repo>.git
cd <your-repo>
```

### Set Up SQL Server

#### Option A — Use an existing SQL Server instance

Update `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=SensitiveWords
    username: sa
    password: YourStrong!Passw0rd
  jpa:
    hibernate:
      ddl-auto: update
```

#### Option B — Run SQL Server through Docker

```bash
docker run -e "ACCEPT_EULA=Y" \
  -e "SA_PASSWORD=YourStrong!Passw0rd" \
  -p 1433:1433 \
  --name sensitive-mssql \
  -d mcr.microsoft.com/mssql/server:2022-latest
```

### Build the Application

```bash
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

Or:

```bash
java -jar target/assessment-0.0.1-SNAPSHOT.jar
```

### Access Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

---

## Running with Docker

## Build the Docker Image

```bash
docker build -t sensitive-words-api .
```

## Start SQL Server Container (If not already running)

```bash
docker run -e "ACCEPT_EULA=Y" \
  -e "SA_PASSWORD=YourStrong!Passw0rd" \
  -p 1433:1433 \
  --name sensitive-mssql \
  -d mcr.microsoft.com/mssql/server:2022-latest
```

### Run the API Container

```bash
docker run -p 8080:8080 --name sensitive-words-api \
  --link sensitive-mssql:mssql \
  -e SPRING_DATASOURCE_URL="jdbc:sqlserver://mssql:1433;databaseName=SensitiveWords" \
  -e SPRING_DATASOURCE_USERNAME=sa \
  -e SPRING_DATASOURCE_PASSWORD=YourStrong!Passw0rd \
  sensitive-words-api
```

---

## Running with Docker Compose (Recommended)

Create a `docker-compose.yml` file:

```yaml
version: '3.8'

services:
  mssql:
    image: mcr.microsoft.com/mssql/server:2022-latest
    container_name: sensitive-mssql
    environment:
      - ACCEPT_EULA=Y
      - SA_PASSWORD=YourStrong!Passw0rd
    ports:
      - "1433:1433"

  api:
    build: .
    container_name: sensitive-words-api
    depends_on:
      - mssql
    environment:
      SPRING_DATASOURCE_URL: "jdbc:sqlserver://mssql:1433;databaseName=SensitiveWords"
      SPRING_DATASOURCE_USERNAME: sa
      SPRING_DATASOURCE_PASSWORD: YourStrong!Passw0rd
    ports:
      - "8080:8080"
```

Run everything:

```bash
docker compose up --build
```

Access Swagger:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Running Tests

Testcontainers automatically provisions an MSSQL container during tests.

```bash
mvn test
```

---

## Project Structure

```
src/
 └── main/
      ├── java/com/sensitivewords/
      │      ├── controller/
      │      ├── service/
      │      ├── repository/
      │    
      └── resources/
            ├── application.yml
            └── schema.sql (optional)
pom.xml
Dockerfile
docker-compose.yml
README.md
```

---

## Environment Variables

| Variable                     | Description                    |
| ---------------------------- | ------------------------------ |
| `SPRING_DATASOURCE_URL`      | SQL Server JDBC connection URL |
| `SPRING_DATASOURCE_USERNAME` | Database username              |
| `SPRING_DATASOURCE_PASSWORD` | Database password              |


