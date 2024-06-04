#FROM findepi/graalvm:java17-native
FROM openjdk:17.0.2-oraclelinux8

MAINTAINER Lion Li

RUN mkdir -p /ai-chat/powerjob/logs

WORKDIR /ai-chat/powerjob

ENV LANG=C.UTF-8 LC_ALL=C.UTF-8 JAVA_OPTS="-Xms512m -Xmx1024m"

EXPOSE 7700

ADD ./target/ai-chat-powerjob-server.jar ./app.jar

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar app.jar \
           -XX:+HeapDumpOnOutOfMemoryError -Xlog:gc*,:time,tags,level -XX:+UseZGC ${JAVA_OPTS}
