#!/bin/bash

if [[ -n "$CASSANDRA_KEYSPACES" && $1 == 'cassandra' ]]; then

  IFS=',' read -ra keyspace <<<"$CASSANDRA_KEYSPACES"
  for i in "${keyspace[@]}"; do
    CQL="CREATE KEYSPACE $i WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1};"
    until echo "$CQL" | cqlsh; do
      echo "cqlsh: Cassandra is unavailable - retry later"
      sleep 2
    done &
  done

fi

exec /docker-entrypoint.sh "$@"
