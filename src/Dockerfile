# Use Maven image to build the app
FROM maven:3.9.2-eclipse-temurin-17 AS build

WORKDIR /LIMS_BACKEND

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

# Use lightweight JRE image for runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /LIMS_BACKEND/target/*.jar LIMS-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
