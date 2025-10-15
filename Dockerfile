# if you're using a multi-stage Dockerfile, add this in the runtime stage
FROM eclipse-temurin:17-jre-jammy

# install netcat (nc) and bash (if you need bash)
USER root
RUN apt-get update \
 && apt-get install -y --no-install-recommends netcat-openbsd bash \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY app.jar app.jar
COPY wait-for-mysql.sh /app/wait-for-mysql.sh
RUN chmod +x /app/wait-for-mysql.sh

# keep existing entrypoint/cmd
CMD ["/app/wait-for-mysql.sh"]
