
ARG APP_INSIGHTS_AGENT_VERSION=2.5.1-BETA
FROM hmctspublic.azurecr.io/base/java:openjdk-8-distroless-1.2

COPY lib/AI-Agent.xml /opt/app/
COPY build/libs/probate-orchestrator-service.jar /opt/app/

EXPOSE 8888

CMD [ "probate-orchestrator-service.jar" ]
