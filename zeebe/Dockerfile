FROM camunda/zeebe-with-hazelcast-exporter:latest

COPY ./application.yaml /usr/local/zeebe/config/

COPY ./trip-saga.bpmn /

COPY ./docker-entrypoint.sh /
RUN chmod +x /docker-entrypoint.sh

ENTRYPOINT ["/docker-entrypoint.sh"]