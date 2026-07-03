# Simple File Server

> 中文文档请见 [README_CN.md](./README_CN.md)

A lightweight file management service supporting upload, download, delete, and
paginated search. Built with Spring Boot 4.1.0 on Java 25.

## Tech Stack

Java 25, Spring Boot 4.1.0, Gradle (Kotlin DSL), PostgreSQL, Hibernate JPA,
Docker (eclipse-temurin:25-jre-alpine)

## Quick Start

### Docker Compose (Recommended)

```bash
docker compose --env-file docker/.env -f docker/docker-compose.yaml up -d
```

### Local Build

Prerequisites: JDK 25, PostgreSQL

```bash
./gradlew bootJar
java -jar build/libs/simple-file-server-0.0.1-SNAPSHOT.jar
```

## Environment Variables

| Variable | Description | Default |
|---|---|---|
| POSTGRES_HOST | Database host | postgresql |
| POSTGRES_PORT | Database port | 5432 |
| POSTGRES_DB | Database name | sfs |
| POSTGRES_USER | Database user | sfs |
| POSTGRES_PASSWORD | Database password | sfs |

## API Reference

All endpoints are prefixed with `/sfs/api` and require HTTP Basic authentication.

| Method | Path | Authority | Description |
|---|---|---|---|
| POST | /sfs/api/upload | sfs:file:upload | Upload file(s) (multipart, field: `files`) |
| GET | /sfs/api/download/{id} | sfs:file:download | Download a file by UUID |
| DELETE | /sfs/api/delete/{id} | sfs:file:delete | Delete a file (owner only) |
| GET | /sfs/api/files | Authenticated | List/search files (?name=&page=&size=) |

## Build & Test

```bash
./gradlew test
./gradlew bootJar
```
