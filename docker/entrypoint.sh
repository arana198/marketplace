#!/bin/sh
set -e
set -x
export DEBIAN_FRONTEND=noninteractive

cd /opt/webapp/config
sed -i 's|OAUTH_SERVER_HOST|'"$OAUTH_SERVER_HOST"'|g' application.properties

sed -i 's|DB_HOST|'"$DB_HOST"'|g' application.properties
sed -i 's|DB_SCHEMA|'"$DB_SCHEMA"'|g' application.properties
sed -i 's|DB_USERNAME|'"$DB_USERNAME"'|g' application.properties
sed -i 's|DB_PASSWORD|'"$DB_PASSWORD"'|g' application.properties

sed -i 's|QUEUE_EXCHANGE_NAME|'"$QUEUE_EXCHANGE_NAME"'|g' application.properties
sed -i 's|RABBITMQ_HOST|'"$RABBITMQ_HOST"'|g' application.properties
sed -i 's|RABBITMQ_PORT|'"$RABBITMQ_PORT"'|g' application.properties
sed -i 's|RABBITMQ_USERNAME|'"$RABBITMQ_USERNAME"'|g' application.properties
sed -i 's|RABBITMQ_PASSWORD|'"$RABBITMQ_PASSWORD"'|g' application.properties

cd /opt/webapp
java -jar studyit.jar