# Só rodar as linhas comentadas se o projeto não tiver sido buildado por fora
#FROM maven:3.9.9-eclipse-temurin-21 AS builder
#WORKDIR /app
#COPY pom.xml .
#COPY src ./src
#RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/fiap-pedido-service-*.jar app.jar
EXPOSE 8079
ENTRYPOINT ["java", "-jar", "app.jar"]