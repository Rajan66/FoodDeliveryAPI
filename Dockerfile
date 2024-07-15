FROM ubuntu:latest AS build

RUN apt-get update && \
    apt-get install -y openjdk-22-jdk maven

WORKDIR /app

COPY . .
RUN mvn clean package

FROM openjdk:22

WORKDIR /app


COPY --from=build /app/target/foodDeliveryApp-1.jar app.jar
EXPOSE 8080


ENTRYPOINT ["java", "-jar", "app.jar"]