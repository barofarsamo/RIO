# Build stage
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# Copy pom.xml first to leverage Docker cache
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17.0.8-jdk-slim AS run
WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/riyobox-backend-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=production"]
