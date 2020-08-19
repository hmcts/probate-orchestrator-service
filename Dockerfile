ARG APP_INSIGHTS_AGENT_VERSION=2.5.1
FROM hmctspublic.azurecr.io/base/java:openjdk-11-distroless-1.4

COPY build/libs/probate-orchestrator-service.jar lib/applicationinsights-agent-2.5.1.jar lib/AI-Agent.xml /opt/app/

EXPOSE 8888
CMD [ "probate-orchestrator-service.jar" ]