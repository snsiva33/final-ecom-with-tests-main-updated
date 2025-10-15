# Dockerfile (auto-detect pom.xml)
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /build

# Copy whole repo into build context so find can see pom.xml
COPY . /build

# Find POM and go-offline (best-effort)
RUN set -eux; \
    POM=$(find . -maxdepth 6 -name pom.xml | head -n 1); \
    if [ -z "$POM" ]; then echo "pom.xml not found"; exit 1; fi; \
    echo "Using POM for dependency go-offline: $POM"; \
    mvn -B -f "$POM" dependency:go-offline || true

# Build using discovered POM
RUN set -eux; \
    POM=$(find . -maxdepth 6 -name pom.xml | head -n 1); \
    if [ -z "$POM" ]; then echo "pom.xml not found"; exit 1; fi; \
    echo "Building with POM: $POM"; \
    mvn -B -f "$POM" -DskipTests package

# final stage (example)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=build /build/**/target/*.jar app.jar
COPY wait-for-mysql.sh /app/wait-for-mysql.sh
RUN chmod +x /app/wait-for-mysql.sh

# use sh -c so the script can exec the java command and keep logs visible
ENTRYPOINT ["sh", "-c", "/app/wait-for-mysql.sh java -jar /app/app.jar"]
