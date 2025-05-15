#!/usr/bin/env bash
set -e

cd "$(dirname "$0")"

pushd .. > /dev/null
./mvnw -DskipTests=true -DskipITs=true clean package
popd >/dev/null

docker compose down
docker compose build
docker compose up -d
docker compose logs -f app


