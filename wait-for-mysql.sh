#!/usr/bin/env bash
set -e

# Get host from DB_HOST or from SPRING_DATASOURCE_URL if provided
DB_HOST="${DB_HOST:-}"
DB_PORT="${DB_PORT:-3306}"
TIMEOUT="${DB_TIMEOUT:-60}"

if [ -z "$DB_HOST" ] && [ -n "$SPRING_DATASOURCE_URL" ]; then
  # extract hostname from jdbc url
  DB_HOST=$(echo "$SPRING_DATASOURCE_URL" | sed -n 's#.*//\([^:/]*\).*#\1#p')
fi

if [ -z "$DB_HOST" ]; then
  echo "DB host not set (DB_HOST or SPRING_DATASOURCE_URL missing). Skipping DB wait."
  exec "$@"
fi

echo "Waiting for DB $DB_HOST:$DB_PORT (timeout ${TIMEOUT}s)..."
elapsed=0
while ! nc -z "$DB_HOST" "$DB_PORT"; do
  sleep 1
  elapsed=$((elapsed+1))
  if [ "$elapsed" -ge "$TIMEOUT" ]; then
    echo "Timeout waiting for $DB_HOST:$DB_PORT"
    exit 1
  fi
done

echo "DB reachable, starting app..."
exec "$@"
