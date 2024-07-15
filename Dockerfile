# Stage 1: Build the project
FROM ubuntu:latest AS build

# Install necessary packages and dependencies
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        apt-transport-https \
        ca-certificates \
        gnupg \
        software-properties-common && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Install OpenJDK 22 and Maven
RUN apt-get update && \
    apt-get install -y openjdk-22-jdk maven

# Set working directory
WORKDIR /app

# Copy the Maven project and build
COPY . .
RUN mvn clean package

# Stage 2: Run the application
FROM openjdk:22

# Set working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/foodDeliveryApp-1.jar app.jar

# Expose port 8080
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
