# ===== STAGE 1: копирование зависимостей =====
FROM gradle:8.10.2-jdk21 AS deps
WORKDIR /app
COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon

# ===== STAGE 2: запуск тестов =====
FROM gradle:8.10.2-jdk21 AS test
WORKDIR /app
COPY . .
RUN gradle test --no-daemon

# ===== STAGE 3: сборка jar =====
FROM gradle:8.10.2-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean bootJar --no-daemon

# ===== STAGE 4: запуск приложения =====
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
