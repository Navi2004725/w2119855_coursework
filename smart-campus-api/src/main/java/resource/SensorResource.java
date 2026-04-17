package resource;

import exception.LinkedResourceNotFoundException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Room;
import model.Sensor;
import repository.DataStore;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {
    private final DataStore dataStore = DataStore.getInstance();

    @GET
    public List<Sensor> getSensors(@QueryParam("type") String type) {
        List<Sensor> sensors = new ArrayList<>(dataStore.getSensors().values());
        if (type == null || type.isBlank()) {
            return sensors;
        }
        return sensors.stream()
                .filter(sensor -> sensor.getType() != null && sensor.getType().toUpperCase(Locale.ROOT).equals(type.toUpperCase(Locale.ROOT)))
                .toList();
    }

    @POST
    public Response createSensor(Sensor sensor) {
        Room room = dataStore.getRooms().get(sensor.getRoomId());
        if (room == null) {
            throw new LinkedResourceNotFoundException("Room with id '" + sensor.getRoomId() + "' does not exist");
        }

        if (sensor.getId() == null || sensor.getId().isBlank()) {
            sensor.setId(UUID.randomUUID().toString());
        }
        if (sensor.getStatus() == null) {
            sensor.setStatus(Sensor.Status.ACTIVE);
        }

        dataStore.getSensors().put(sensor.getId(), sensor);
        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }
        room.getSensorIds().add(sensor.getId());

        return Response.created(URI.create("/api/v1/sensors/" + sensor.getId())).entity(sensor).build();
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingsResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}
