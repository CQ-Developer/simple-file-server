FROM eclipse-temurin:25-jdk AS builder
WORKDIR /app
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle gradle
RUN ./gradlew dependencies --no-daemon
COPY src src
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:25-jre-alpine
RUN addgroup -S sfs && adduser -S -G sfs -h /home/sfs sfs
WORKDIR /home/sfs
USER sfs
COPY --from=builder --chown=sfs:sfs /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
