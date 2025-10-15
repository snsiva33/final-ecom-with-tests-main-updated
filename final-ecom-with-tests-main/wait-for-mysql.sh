#!/bin/bash
set -e

# DB host/port come from env (set in Render or docker-compose)
DB_HOST=${DB_HOST:-db}
DB_PORT=${DB_PORT:-3306}

echo "Waiting for MySQL at $DB_HOST:$DB_PORT..."

while ! nc -z "$DB_HOST" "$DB_PORT"; do
  printf '.'
  sleep 1
done

echo
echo "MySQL reachable. Starting app..."
exec java -jar /app/app.jar
