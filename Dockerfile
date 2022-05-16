FROM maven:3.8.5-openjdk-11-slim as builder
WORKDIR /app
COPY pom.xml pom.xml
RUN mvn -B -f pom.xml -s /usr/share/maven/ref/settings-docker.xml dependency:resolve
COPY src src
RUN mvn -B -s /usr/share/maven/ref/settings-docker.xml package -DskipTests

FROM openjdk:11.0.15-jre
WORKDIR /app
COPY --from=builder /app/target/traffic-tax-calculator-0.0.1-SNAPSHOT.jar /app/traffic-tax-calculator.jar
ENTRYPOINT ["java","-jar","/app/traffic-tax-calculator.jar"]