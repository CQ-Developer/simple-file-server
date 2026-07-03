# Simple File Server

轻量级文件管理服务，支持文件上传、下载、删除、分页搜索。
基于 Spring Boot 4.1.0，运行于 Java 25。

## 技术栈

Java 25、Spring Boot 4.1.0、Gradle（Kotlin DSL）、PostgreSQL、Hibernate JPA、
Docker（eclipse-temurin:25-jre-alpine）

## 快速开始

### Docker Compose（推荐）

```bash
docker compose --env-file docker/.env -f docker/docker-compose.yaml up -d
```

### 本地构建

前置条件：JDK 25、PostgreSQL

```bash
./gradlew bootJar
java -jar build/libs/simple-file-server-0.0.1-SNAPSHOT.jar
```

## 环境变量

| 变量 | 说明 | 默认值 |
|---|---|---|
| POSTGRES_HOST | 数据库主机 | postgresql |
| POSTGRES_PORT | 数据库端口 | 5432 |
| POSTGRES_DB | 数据库名 | sfs |
| POSTGRES_USER | 数据库用户 | sfs |
| POSTGRES_PASSWORD | 数据库密码 | sfs |

## API 参考

所有端点前缀为 `/sfs/api`，需要 HTTP Basic 认证。

| 方法 | 路径 | 权限 | 说明 |
|---|---|---|---|
| POST | /sfs/api/upload | sfs:file:upload | 上传文件（multipart，字段名 `files`） |
| GET | /sfs/api/download/{id} | sfs:file:download | 根据 UUID 下载文件 |
| DELETE | /sfs/api/delete/{id} | sfs:file:delete | 删除文件（仅上传者本人） |
| GET | /sfs/api/files | 任意认证用户 | 分页查询文件 (?name=&page=&size=) |

## 构建与测试

```bash
./gradlew test
./gradlew bootJar
```
