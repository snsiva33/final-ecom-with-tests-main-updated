# Ekart Backend - Ready-to-Run Package

This package was prepared from the uploaded project and includes:
- Dockerfile (created if missing)
- docker-compose.yml for local testing (MySQL + app)
- render.yaml for Render.com deployment (example)
- README with run instructions.

## Local (recommended for testing)
1. Ensure Docker and Docker Compose are installed.
2. From project root run:
   ```
   docker-compose up --build
   ```
   This will start MySQL (3306) and the app (8080).
3. App will be available at http://localhost:8080

## Local without Docker (run in IDE)
1. Ensure Java 17 and Maven wrapper present (`mvnw`).
2. Configure `application.properties` or set env vars:
   - SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/ekartdb
   - SPRING_DATASOURCE_USERNAME=root
   - SPRING_DATASOURCE_PASSWORD=rootpassword
3. Run `./mvnw spring-boot:run` or run Application main class from IDE.

## Deploy to Render.com
1. Create a new Web Service using Docker.
2. Push this repo to GitHub and connect.
3. Set environment variables properly (use managed DB or external MySQL).
4. Use `render.yaml` as an example configuration.

If you need me to change database names, ports, or add a sample `application.properties` file, tell me the desired values and I'll update and repackage.
