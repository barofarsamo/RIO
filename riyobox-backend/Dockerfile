# Build stage
FROM maven:3.8.7-openjdk-17-slim AS build
WORKDIR /app

# Copy pom.xml first to leverage Docker cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# Run stage
FROM openjdk:17-jdk-slim AS run
WORKDIR /app

# Copy built jar - using wildcard to be more flexible
COPY --from=build /app/target/*.jar app.jar

# Expose port (Render will use PORT env var, but 8080 is a good default)
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=production"]
