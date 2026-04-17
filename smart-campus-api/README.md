# Smart Campus REST API (JAX-RS)

This project is a Java JAX-RS (Jersey) REST API for managing campus rooms, sensors, and sensor readings.

## API Overview

- **Framework:** JAX-RS (Jersey), no Spring Boot
- **Server:** Embedded Grizzly HTTP server
- **Storage:** In-memory `HashMap` and `ArrayList` (no database)
- **Base path:** `/api/v1`
- **Main entities:** `Room`, `Sensor`, `SensorReading`

### Core Endpoints

- `GET /api/v1` - API discovery
- `GET /api/v1/rooms` - List rooms
- `POST /api/v1/rooms` - Create room
- `GET /api/v1/rooms/{id}` - Get room by id
- `DELETE /api/v1/rooms/{id}` - Delete room (blocked if linked sensors exist)
- `GET /api/v1/sensors` - List sensors
- `GET /api/v1/sensors?type=CO2` - Filter sensors by type
- `POST /api/v1/sensors` - Create sensor
- `GET /api/v1/sensors/{sensorId}/readings` - List readings for one sensor
- `POST /api/v1/sensors/{sensorId}/readings` - Add reading for one sensor

## Setup Instructions

### Prerequisites

- Java 17+
- Maven 3.8+

### Build

```bash
mvn clean install
```

### Run server

```bash
mvn exec:java
```

Server starts at:

`http://localhost:8080/api/v1`

## cURL Examples

### 1) Create room

```bash
curl -X POST "http://localhost:8080/api/v1/rooms" \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Lab A\",\"capacity\":40,\"sensorIds\":[]}"
```

### 2) Create sensor

Replace `ROOM_ID` with an existing room id.

```bash
curl -X POST "http://localhost:8080/api/v1/sensors" \
  -H "Content-Type: application/json" \
  -d "{\"type\":\"CO2\",\"status\":\"ACTIVE\",\"roomId\":\"ROOM_ID\"}"
```

### 3) Get rooms

```bash
curl "http://localhost:8080/api/v1/rooms"
```

### 4) Filter sensors by type

```bash
curl "http://localhost:8080/api/v1/sensors?type=CO2"
```

### 5) Add reading

Replace `SENSOR_ID` with an existing sensor id.

```bash
curl -X POST "http://localhost:8080/api/v1/sensors/SENSOR_ID/readings" \
  -H "Content-Type: application/json" \
  -d "{\"value\":512.4}"
```

## Coursework Theory Answers

### 1) JAX-RS lifecycle

By default, resource classes are request-scoped. A new resource instance can be created for each request. The JAX-RS runtime handles routing, parameter binding, content negotiation, and response conversion.

### 2) HATEOAS

HATEOAS means API responses can include links to related actions/resources. Clients then discover valid next steps dynamically instead of hard-coding all URLs.

### 3) ID vs full object in payloads

Sending IDs is better for relationships because it reduces payload size and avoids duplication. It also separates ownership of data and prevents accidental deep object updates.

### 4) DELETE idempotency

DELETE is idempotent because repeating the same delete request should keep the system in the same final state (resource removed). The first call removes it; later calls should not create new side effects.

### 5) `@Consumes` behavior

`@Consumes` tells JAX-RS which request media types are accepted, for example `application/json`. If a client sends an unsupported type, the server can return HTTP 415.

### 6) QueryParam vs PathParam

`@PathParam` identifies a specific resource in the URL path, such as `/rooms/{id}`. `@QueryParam` is for optional filters or modifiers, such as `/sensors?type=CO2`.

### 7) Sub-resource benefits

Sub-resources model nested relationships clearly, like readings belonging to one sensor. They improve API organization and keep parent/child logic modular.

### 8) HTTP 422 vs 404

Use 404 when the requested URI resource itself is not found. Use 422 when the request syntax is valid but semantically incorrect, for example a sensor references a room id that does not exist.

### 9) Stack trace security risk

Returning stack traces reveals internal class names, code paths, and infrastructure details. Attackers can use this information to target vulnerabilities, so production APIs should hide stack traces.

### 10) Logging filter benefits

Logging filters provide request/response visibility for debugging, monitoring, and auditing. They help diagnose failures and measure API behavior consistently across endpoints.

## Notes

- Custom exceptions are mapped to required HTTP status codes:
  - `RoomNotEmptyException` -> `409 Conflict`
  - `LinkedResourceNotFoundException` -> `422 Unprocessable Entity`
  - `SensorUnavailableException` -> `403 Forbidden`
  - Global fallback mapper -> `500 Internal Server Error`
- Error body format:

```json
{
  "error": "message",
  "status": 409
}
```
