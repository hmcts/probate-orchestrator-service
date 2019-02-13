FROM hmcts/cnp-java-base:openjdk-8u191-jre-alpine3.9-1.0

COPY docker/entrypoint.sh /
COPY build/libs/probate-orchestrator-service.jar /opt/app/

HEALTHCHECK --interval=10s --timeout=10s --retries=10 CMD http_proxy="" wget -q --spider http://localhost:8888/health || exit 1

EXPOSE 8888

CMD [ "probate-orchestrator-service.jar" ]



