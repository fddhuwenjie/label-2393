FROM maven:3.8-openjdk-11 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

FROM tomcat:9-jdk11
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/taopiaopiao.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
