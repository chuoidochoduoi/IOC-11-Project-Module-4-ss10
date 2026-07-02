#!/bin/bash
CONTAINER_NAME="kafka-pharmacy"
KAFKA_BIN="/opt/bitnami/kafka/bin"

docker exec $CONTAINER_NAME $KAFKA_BIN/kafka-topics.sh --create --topic medicine-stock-events --partitions 3 --replication-factor 1 --bootstrap-server localhost:9092
docker exec $CONTAINER_NAME $KAFKA_BIN/kafka-topics.sh --create --topic medicine-price-updates --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092
docker exec $CONTAINER_NAME $KAFKA_BIN/kafka-topics.sh --create --topic pharmacy-notifications --partitions 2 --replication-factor 1 --bootstrap-server localhost:9092

docker exec $CONTAINER_NAME $KAFKA_BIN/kafka-topics.sh --list --bootstrap-server localhost:9092
docker exec $CONTAINER_NAME $KAFKA_BIN/kafka-topics.sh --describe --bootstrap-server localhost:9092