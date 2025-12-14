FROM gradle:8.10.2-jdk21 AS builder
WORKDIR /app
COPY build.gradle settings.gradle ./
RUN gradle build -x test --no-daemon || return 0
COPY . .
RUN gradle clean bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]