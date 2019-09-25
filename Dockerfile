ARG APP_INSIGHTS_AGENT_VERSION=2.3.1
FROM hmctspublic.azurecr.io/base/java:openjdk-8-distroless-debug-1.1

COPY docker/entrypoint.sh /
COPY lib/AI-Agent.xml /opt/app/
COPY build/libs/probate-orchestrator-service.jar /opt/app/

EXPOSE 8888

CMD [ "probate-orchestrator-service.jar" ]

