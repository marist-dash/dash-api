# Development
FROM openjdk:8-jdk-alpine
COPY application.properties application.properties
EXPOSE 8081
ARG JAR_FILE=target/dash-parse-0.1.0.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]