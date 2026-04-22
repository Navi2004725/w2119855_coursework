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
