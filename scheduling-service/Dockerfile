# STAGE 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy the pom.xml and download dependencies first (this caches the downloads!)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the .jar file
COPY src ./src
RUN mvn clean package -DskipTests

# STAGE 2: Run the application using a lightweight JRE
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the built .jar file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the standard Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]