FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install -y openjdk-17-jdk maven
COPY . .
RUN ./mvnw package

FROM openjdk:17
EXPOSE 8080
COPY --from=build /build/libs/foodDeliveryApp-1.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]