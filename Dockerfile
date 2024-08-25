FROM eclipse-temurin:22.0.2_9-jdk

LABEL authors="yuri"

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]