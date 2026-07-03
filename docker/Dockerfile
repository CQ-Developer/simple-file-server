FROM eclipse-temurin:25-alpine AS builder
WORKDIR /app
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle gradle
RUN ./gradlew dependencies --no-daemon
COPY src src
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:25-alpine
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.tuna.tsinghua.edu.cn/g' /etc/apk/repositories && \
    addgroup -S sfs && \
    adduser -S -G sfs -h /home/sfs sfs && \
    apk update && \
    apk add --no-cache curl
WORKDIR /home/sfs
USER sfs
COPY --from=builder --chown=sfs:sfs /app/build/libs/*.jar app.jar
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:8080/sfs/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
