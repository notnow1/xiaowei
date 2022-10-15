FROM openjdk:8-jdk-alpine
MAINTAINER qixiaowei

COPY target/*.jar /app.jar
EXPOSE 8080

ENTRYPOINT ["/bin/sh","-c","java -Duser.timezone=Asia/Shanghai -Dfile.encoding=utf8 -Djava.security.egd=file:/dev/./urandom -jar /app.jar"]