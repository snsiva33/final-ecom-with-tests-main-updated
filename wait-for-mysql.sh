#!/bin/sh
set -e

# derive host/port from SPRING_DATASOURCE_URL if possible
DBURL="${SPRING_DATASOURCE_URL:-}"
if echo "$DBURL" | grep -q "jdbc:postgresql"; then
  DB_HOST=$(echo "$DBURL" | sed -n 's#jdbc:postgresql://\([^:/]*\).*#\1#p')
  DB_PORT=$(echo "$DBURL" | sed -n 's#jdbc:postgresql://[^:]*:\?\([0-9]*\).*#\1#p')
  [ -z "$DB_PORT" ] && DB_PORT=5432
else
  DB_HOST="${DB_HOST:-localhost}"
  DB_PORT="${DB_PORT:-5432}"
fi

echo "Waiting for DB at $DB_HOST:$DB_PORT (derived from SPRING_DATASOURCE_URL)"
COUNT=0
while ! nc -z "$DB_HOST" "$DB_PORT"; do
  COUNT=$((COUNT+1))
  echo "Waiting for DB ($COUNT) $DB_HOST:$DB_PORT..."
  sleep 2
  if [ $COUNT -gt 30 ]; then
    echo "Timeout waiting for DB $DB_HOST:$DB_PORT"
    exit 1
  fi
done

echo "DB reachable, starting app..."
# exec the java command so it becomes PID 1 (foreground) and logs appear in Render UI
exec "$@"
