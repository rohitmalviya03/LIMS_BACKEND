# Use Maven image to build the app
FROM maven:3.9.2-eclipse-temurin-17 AS build

WORKDIR /LIMS_BACKEND

# Copy pom.xml and download dependencies
COPY pom.xml .

RUN mvn dependency:go-offline

# Copy the source code (assuming you're in root of repo)
COPY src ./src

# Build the jar
RUN mvn clean package -DskipTests

# Use lightweight image to run the jar
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built jar from the previous stage
COPY --from=build /LIMS_BACKEND/target/LIMS-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "LIMS-0.0.1-SNAPSHOT.jar"]
