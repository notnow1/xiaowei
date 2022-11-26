FROM harbor.qixiaowei.net/library/openjdk:8-jdk-alpine-qxw
MAINTAINER qixiaowei

ENV PARAMS="--server.port=8080"

COPY qixiaowei-service/qixiaowei-operate-cloud/target/*.jar /app.jar

ENTRYPOINT ["/bin/sh","-c","java -Duser.timezone=Asia/Shanghai -Dfile.encoding=utf8 -Djava.security.egd=file:/dev/./urandom -jar /app.jar ${PARAMS}"]

EXPOSE 8080