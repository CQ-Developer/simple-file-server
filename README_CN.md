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

### 基础路径

所有 API 路径均以 `/sfs` 为上下文前缀。完整请求路径格式：

```
http://{host}:{port}/sfs/api/...
```

### 认证方式

#### 表单登录

默认表单登录路径 `/sfs/login`。登录页面由 Spring Security 自动生成。

#### 登出

```
POST /sfs/logout
```

无需额外参数，服务端使当前会话失效。

#### 认证失败响应

| 场景 | HTTP 状态 | 响应体 |
|---|---|---|
| 未认证（无有效凭据） | `401 Unauthorized` | `{"success": false, "message": "Invalid username or password"}` |
| 权限不足（已认证但无权限） | `403 Forbidden` | `{"success": false, "message": "Insufficient permissions"}` |

### API 端点

#### 上传文件

**上传一个或多个文件。**

```
POST /api/upload
```

**权限要求**

| 字段 | 值 |
|---|---|
| 认证 | 是 |
| 权限 | `sfs:file:upload` |

**请求**

- **Content-Type:** `multipart/form-data`
- **Body:**

| 字段 | 类型 | 必填 | 描述 |
|---|---|---|---|
| `files` | `multipart[]` | 是 | 上传的文件列表（最多 10 个文件） |

**响应示例**

```json
{
  "success": true,
  "message": "ok",
  "data": null
}
```

**说明**

- 单次最多上传 10 个文件
- 单文件最大 1 GB，请求总大小上限 10 GB

#### 下载文件

**根据 UUID 下载文件。**

```
GET /api/download/{id}
```

**权限要求**

| 字段 | 值 |
|---|---|
| 认证 | 是 |
| 权限 | `sfs:file:download` |

**路径参数**

| 参数 | 类型 | 描述 |
|---|---|---|
| `id` | `UUID` | 文件唯一标识符 |

**响应**

- **Content-Disposition:** `attachment; filename="{文件名}"`（UTF-8 编码）
- **Content-Type:** 自动检测的媒体类型
- **Content-Length:** 文件字节数
- **Body:** 文件二进制流

**错误响应**

文件不存在：

```json
{
  "success": false,
  "message": "File not found"
}
```

#### 删除文件

**根据 UUID 删除文件。**

```
DELETE /api/delete/{id}
```

**权限要求**

| 字段 | 值 |
|---|---|
| 认证 | 是 |
| 权限 | `sfs:file:delete` |
| 附加限制 | 仅文件的上传者可删除 |

**路径参数**

| 参数 | 类型 | 描述 |
|---|---|---|
| `id` | `UUID` | 文件唯一标识符 |

**响应示例**

```json
{
  "success": true,
  "message": "ok",
  "data": null
}
```

#### 文件列表查询

**分页查询已上传文件，支持按文件名模糊搜索。**

```
GET /api/files
```

**权限要求**

| 字段 | 值 |
|---|---|
| 认证 | 是 |
| 权限 | 任意已认证用户 |

**查询参数**

| 参数 | 类型 | 必填 | 默认值 | 描述 |
|---|---|---|---|---|
| `name` | `string` | 否 | — | 文件名关键字（LIKE `%name%` 模糊匹配） |
| `page` | `integer` | 否 | `1` | 页码（从 1 开始） |
| `size` | `integer` | 否 | `10` | 每页记录数 |

**响应示例**

```json
{
  "success": true,
  "message": "ok",
  "data": {
    "pages": 3,
    "records": [
      {
        "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
        "name": "photo.jpg",
        "hash": "abc123def456...",
        "size": 1048576,
        "uploader": "admin",
        "uploadTime": "2026-07-08T10:30:00"
      }
    ]
  }
}
```

### 安全与权限

#### 权限映射

| 端点 | 所需权限 |
|---|---|
| `POST /api/upload` | `sfs:file:upload` |
| `GET /api/download/{id}` | `sfs:file:download` |
| `DELETE /api/delete/{id}` | `sfs:file:delete` |
| `GET /api/files` | 任意已认证用户 |

### 响应模型

#### 通用响应

```json
{
  "success": true,
  "message": "ok",
  "data": null
}
```

| 字段 | 类型 | 描述 |
|---|---|---|
| `success` | `boolean` | 操作是否成功 |
| `message` | `string` | 提示信息（成功为 `"ok"`，失败为错误描述） |
| `data` | `any` | 响应数据，可能为 `null` |

#### 分页响应

```json
{
  "pages": 3,
  "records": []
}
```

| 字段 | 类型 | 描述 |
|---|---|---|
| `pages` | `integer` | 总页数 |
| `records` | `array` | 当前页记录列表 |

#### 文件记录

```json
{
  "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "name": "photo.jpg",
  "hash": "abc123def456...",
  "size": 1048576,
  "uploader": "admin",
  "uploadTime": "2026-07-08T10:30:00"
}
```

| 字段 | 类型 | 描述 |
|---|---|---|
| `id` | `UUID` | 文件唯一标识符 |
| `name` | `string` | 文件名 |
| `hash` | `string` | SHA-256 文件哈希值 |
| `size` | `number` | 文件大小（字节） |
| `uploader` | `string` | 上传者用户名 |
| `uploadTime` | `string` | 上传时间（ISO-8601 格式） |

### 错误处理

#### 业务错误

所有业务异常均以 HTTP 200 + `success: false` 的方式返回。

| 场景 | HTTP 状态 | `success` | `message` |
|---|---|---|---|
| 业务异常 | `200 OK` | `false` | 具体错误描述 |
| 缺少请求参数 | `200 OK` | `false` | `"Missing form data"` |
| 请求方法不支持 | `200 OK` | `false` | `"Unsupported request method"` |

#### 安全错误

| 场景 | HTTP 状态 | 响应体 |
|---|---|---|
| 未认证 | `401 Unauthorized` | `{"success": false, "message": "Invalid username or password"}` |
| 权限不足 | `403 Forbidden` | `{"success": false, "message": "Insufficient permissions"}` |

## 构建与测试

```bash
./gradlew test
./gradlew bootJar
```
