#!/bin/sh

java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /opt/app/probate-orchestrator-service.jar $@

