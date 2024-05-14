#FROM maven:3.8.5-openjdk-17 AS build
#WORKDIR /
#COPY /src /src
#COPY pom.xml /
#
#RUN mvn clean package
#
#FROM tomcat:jre17-temurin-jammy
#COPY --from=build /target/*.war /usr/local/tomcat/webapps/ROOT.war