# E-commerce Backend (Spring Boot)

## Overview
Simple e-commerce backend built with:
- Spring Boot (Web, Security)
- JPA / Hibernate
- MySQL (production), H2 (tests)
- JWT authentication
- Unit tests (JUnit5 + Mockito), integration test with H2

APIs:
- `/api/auth/login` - POST { "email","password" } -> returns JWT
- `/api/auth/register` - POST -> register user
- `/api/products` - CRUD + search (query params `name`, `category`)
- `/api/orders` - POST to create order (updates product stock)
- `/api/users` - CRUD for users

## Requirements
- Java 21+
- Maven
- MySQL (or use Docker / Render managed DB)

> Note: This project has been upgraded to target Java 21 (LTS). Install a JDK 21 distribution (Eclipse Temurin/Adoptium, Azul Zulu, or other) and set your JAVA_HOME to the JDK 21 installation before building or running locally.

## Setup (local)
1. Clone project.
2. Update `src/main/resources/application.properties` with your MySQL credentials:
   ```
   spring.datasource.url=jdbc:mysql://<host>:3306/ecommerce_db
   spring.datasource.username=<username>
   spring.datasource.password=<password>
   ```
3. Build:
   ```
   mvn clean package
   ```
4. Run:
   ```
   java -jar target/ecommerce-backend-1.0.0.jar
   ```
   or
   ```
   mvn spring-boot:run
   ```

## Tests
Run:
```
mvn test
```

## Deploy to Render/Railway
- Build via Maven or use Maven build pack.
- Provide environment variables to override `spring.datasource.*` and `app.jwt.secret`.
- Ensure `spring.jpa.hibernate.ddl-auto=update` (already set).

## Notes
- Replace `app.jwt.secret` with a secure long secret in production (use env var).
- Sample data is in `src/main/resources/data.sql`.
- Passwords are stored bcrypt-hashed.


## Run with Docker Compose (recommended for local testing)

This project includes a `docker-compose.yml` that starts a MySQL 8 instance configured for the app.

1. Install Docker & Docker Compose.
2. From the project root run:

```bash
docker-compose up -d
```

MySQL credentials (as configured in `docker-compose.yml`):
- Host: `mysql` (Docker network) — from the app container
- Exposed port: `3306` on localhost
- Database: `ecommerce_db`
- User: `ecom_user`
- Password: `strong_password_here`

`application.properties` is preconfigured to use these credentials and the JDBC URL `jdbc:mysql://mysql:3306/ecommerce_db?...` so if you run the app from your host you can either keep the URL pointing at `localhost` or run the app in Docker too. If running the Spring Boot app on your host machine (not in Docker) change the JDBC URL to `jdbc:mysql://127.0.0.1:3306/ecommerce_db?...` in `src/main/resources/application.properties`.

If you prefer to use your own MySQL server, update `spring.datasource.username` and `spring.datasource.password` in `src/main/resources/application.properties` accordingly.
