FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests clean package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/bff-springboot-1.0.0.jar app.jar
ENV SERVER_PORT=8080
ENV FUNCTIONS_BASE_URL=https://functionsbiblioteca-d4bpb6h8fybvbhac.eastus-01.azurewebsites.net/api
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
