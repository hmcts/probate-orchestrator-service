 # renovate: datasource=github-releases depName=microsoft/ApplicationInsights-Java
ARG APP_INSIGHTS_AGENT_VERSION=3.7.2
FROM hmctspublic.azurecr.io/base/java:21-distroless

COPY build/libs/probate-orchestrator-service.jar /opt/app/
COPY lib/applicationinsights.json /opt/app/

EXPOSE 8888
CMD [ "probate-orchestrator-service.jar" ]
