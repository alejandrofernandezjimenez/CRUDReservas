# Etapa 1: build con Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: imagen final liviana
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/crudreservas-0.0.1-SNAPSHOT.jar crudreservas.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "crudreservas.jar"]