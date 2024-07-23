FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install -y openjdk-17-jdk maven

WORKDIR /app

COPY mvnw .
COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package

FROM openjdk:17
EXPOSE 7000
ENV PORT 7000
COPY --from=build /app/target/foodDeliveryApp-1.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]