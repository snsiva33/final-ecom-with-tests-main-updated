# Dockerfile (robust: supports pom.xml in subfolders via POM_PATH arg)
ARG POM_PATH=./pom.xml
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /build

# Copy everything (so pom.xml can be in subfolder)
COPY . /build

# Resolve dependencies using specified POM_PATH (default ./pom.xml)
RUN mvn -B -f ${POM_PATH} dependency:go-offline || true

# Build the project using specified POM
RUN mvn -B -f ${POM_PATH} -DskipTests package

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
RUN apt-get update && apt-get install -y netcat && rm -rf /var/lib/apt/lists/*

# copy jar from build stage (copy first matching jar)
COPY --from=build /build/**/target/*.jar app.jar

COPY wait-for-mysql.sh /app/wait-for-mysql.sh
RUN chmod +x /app/wait-for-mysql.sh

EXPOSE 8080
ENTRYPOINT ["/app/wait-for-mysql.sh"]
