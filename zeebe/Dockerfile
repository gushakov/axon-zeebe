FROM maven:latest as downloader

# from https://github.com/zeebe-io/zeebe-simple-monitor/


# seems like 0.6.0 version does not work, incompatible Java version
ENV HAZELCAST_EXPORTER_VERSION=0.5.0

RUN mvn dependency:get -B \
    -DgroupId="io.zeebe.hazelcast" -DartifactId="zeebe-hazelcast-exporter" \
    -Dversion="${HAZELCAST_EXPORTER_VERSION}" -Dpackaging="jar" -Dtransitive=false

RUN mvn dependency:copy -B \
    -Dartifact="io.zeebe.hazelcast:zeebe-hazelcast-exporter:${HAZELCAST_EXPORTER_VERSION}:jar" \
    -DoutputDirectory=/tmp/ \
    -Dmdep.stripVersion=true

FROM camunda/zeebe:latest

COPY --from=downloader /tmp/zeebe-hazelcast-exporter.jar /usr/local/zeebe/lib/zeebe-hazelcast-exporter.jar