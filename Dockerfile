# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime - Ubuntu/Debian based
FROM eclipse-temurin:17-jre

# Create non-root user - для Ubuntu/Debian синтаксис другой!
RUN groupadd -r spring && useradd -r -g spring -m spring
USER spring

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]