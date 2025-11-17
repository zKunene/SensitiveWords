# Stage 1: Build the jar
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom and download dependencies first (caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Package the app
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/assessment-0.0.1-SNAPSHOT.jar app.jar

# Expose API port
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java","-jar","app.jar"]
