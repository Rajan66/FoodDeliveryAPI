# Use Maven to build the project
FROM maven:3.8.6-openjdk-22 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

# Use the official OpenJDK 22 runtime image
FROM eclipse-temurin:22-jdk
WORKDIR /app
COPY --from=build /app/target/foodDeliveryApp-1.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
