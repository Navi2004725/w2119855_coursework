package resource;

import exception.LinkedResourceNotFoundException;
import exception.SensorUnavailableException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Sensor;
import model.SensorReading;
import repository.DataStore;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {
    private final DataStore dataStore = DataStore.getInstance();
    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public List<SensorReading> getReadings() {
        ensureSensorExists();
        return dataStore.getReadingsBySensor().getOrDefault(sensorId, new ArrayList<>());
    }

    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = ensureSensorExists();
        if (sensor.getStatus() == Sensor.Status.MAINTENANCE) {
            throw new SensorUnavailableException("Sensor '" + sensorId + "' is in MAINTENANCE mode");
        }

        if (reading.getId() == null || reading.getId().isBlank()) {
            reading.setId(UUID.randomUUID().toString());
        }
        if (reading.getTimestamp() == null) {
            reading.setTimestamp(Instant.now());
        }

        dataStore.getReadingsBySensor()
                .computeIfAbsent(sensorId, key -> new ArrayList<>())
                .add(reading);

        sensor.setCurrentValue(reading.getValue());
        dataStore.getSensors().put(sensorId, sensor);

        return Response.created(URI.create("/api/v1/sensors/" + sensorId + "/readings/" + reading.getId()))
                .entity(reading)
                .build();
    }

    private Sensor ensureSensorExists() {
        Sensor sensor = dataStore.getSensors().get(sensorId);
        if (sensor == null) {
            throw new LinkedResourceNotFoundException("Sensor with id '" + sensorId + "' does not exist");
        }
        return sensor;
    }
}
