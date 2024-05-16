FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /

COPY /src /src
COPY pom.xml /
COPY audit-spring-boot-starter /audit-spring-boot-starter
COPY log-spring-boot-starter /log-spring-boot-starter

RUN mvn -f /audit-spring-boot-starter/pom.xml clean install
RUN mvn -f /log-spring-boot-starter/pom.xml clean install
RUN mvn -f /pom.xml clean package -Dmaven.test.skip=true

FROM openjdk:17-jdk-slim
COPY --from=build /target/*.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]