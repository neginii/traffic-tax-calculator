FROM maven:3.8.5-openjdk-11-slim as builder
WORKDIR /app
COPY pom.xml pom.xml
COPY src src
RUN mvn package -DskipTests

FROM openjdk:11.0.15-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/traffic-tax-calculator.jar
COPY external-properties/ /app/
ENTRYPOINT ["java","-jar","/app/traffic-tax-calculator.jar","--spring.config.location=classpath:/application.yml,optional:file:/app/application.yml"]