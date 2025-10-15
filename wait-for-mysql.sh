#!/bin/bash
set -e

# Accept derived host:port from SPRING_DATASOURCE_URL or env
DB_URL="${SPRING_DATASOURCE_URL:-${SPRING_DATASOURCE_URL}}"

# If SPRING_DATASOURCE_URL is jdbc:postgresql://host:5432/dbname, extract host and port
# Supports formats: jdbc:postgresql://host:5432/dbname or host:5432
if [[ "$DB_URL" =~ jdbc: ]]; then
  # extract host:port
  hostport=$(echo "$DB_URL" | sed -E 's#jdbc:postgresql://([^/]+)/.*#\1#')
else
  hostport="$DB_URL"
fi

# Default timeout seconds
TIMEOUT=${WAIT_TIMEOUT:-60}
SLEEP_INTERVAL=${WAIT_INTERVAL:-2}

if [[ -z "$hostport" ]]; then
  echo "Waiting for DB at <unknown host> (SPRING_DATASOURCE_URL not set)"
  exit 1
fi

HOST=$(echo "$hostport" | cut -d: -f1)
PORT=$(echo "$hostport" | cut -d: -f2)
if [[ -z "$PORT" ]]; then
  PORT=5432
fi

echo "Waiting for DB at $HOST:$PORT (derived from SPRING_DATASOURCE_URL)"

start_ts=$(date +%s)
while true; do
  # try nc if available
  if command -v nc >/dev/null 2>&1; then
    if nc -z "$HOST" "$PORT" >/dev/null 2>&1; then
      echo "DB reachable, starting app..."
      break
    fi
  else
    # fallback: use bash /dev/tcp
    if (echo > /dev/tcp/"$HOST"/"$PORT") >/dev/null 2>&1; then
      echo "DB reachable (via /dev/tcp), starting app..."
      break
    fi
  fi

  now=$(date +%s)
  elapsed=$((now - start_ts))
  if [[ "$elapsed" -ge "$TIMEOUT" ]]; then
    echo "Timeout waiting for DB $HOST:$PORT"
    exit 1
  fi

  echo "Waiting for DB ($elapsed) $HOST:$PORT..."
  sleep "$SLEEP_INTERVAL"
done

# finally run the jar
exec java -jar /app/app.jar
