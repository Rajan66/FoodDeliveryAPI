FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install -y openjdk-22-jdk maven
COPY . .
RUN ./mvnw package

FROM openjdk:22
EXPOSE 8080
COPY --from=build /target/foodDeliveryApp-1.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]