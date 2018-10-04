FROM maven:3.5.2-jdk-8-alpine AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package

FROM openjdk:8-jdk-alpine
EXPOSE 8080
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/dash-api-0.1.0.jar dash-api-0.1.0.jar
ENTRYPOINT ["java", "-jar", "dash-api-0.1.0.jar"]
