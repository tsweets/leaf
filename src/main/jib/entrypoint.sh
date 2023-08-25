#!/bin/sh

echo "Starting Leaf Fake Processor"
exec java ${JAVA_OPTS} -noverify -XX:+AlwaysPreTouch -Djava.security.egd=file:/dev/./urandom -cp /app/resources/:/app/classes/:/app/libs/* "org.beer30.leaf.LeafApplication"  "$@"
