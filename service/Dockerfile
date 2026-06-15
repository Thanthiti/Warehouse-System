FROM eclipse-temurin:11-jre-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 9443

ENTRYPOINT ["java", "-jar", "app.jar"]
