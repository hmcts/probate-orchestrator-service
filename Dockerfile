ARG APP_INSIGHTS_AGENT_VERSION=3.4.11
FROM hmctspublic.azurecr.io/base/java:11-distroless

COPY build/libs/probate-orchestrator-service.jar lib/AI-Agent.xml /opt/app/

EXPOSE 8888
CMD [ "probate-orchestrator-service.jar" ]
