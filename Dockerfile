 # renovate: datasource=github-releases depName=microsoft/ApplicationInsights-Java
ARG APP_INSIGHTS_AGENT_VERSION=3.7.9
FROM hmctsprod.azurecr.io/base/java:25-distroless

COPY build/libs/probate-orchestrator-service.jar /opt/app/
COPY lib/applicationinsights.json /opt/app/

EXPOSE 8888
CMD [ "probate-orchestrator-service.jar" ]
