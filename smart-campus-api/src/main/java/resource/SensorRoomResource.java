package resource;

import exception.RoomNotEmptyException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Room;
import repository.DataStore;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorRoomResource {
    private final DataStore dataStore = DataStore.getInstance();

    @GET
    public List<Room> getRooms() {
        return dataStore.getRooms().values().stream().toList();
    }

    @POST
    public Response createRoom(Room room) {
        if (room.getId() == null || room.getId().isBlank()) {
            room.setId(UUID.randomUUID().toString());
        }
        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }
        dataStore.getRooms().put(room.getId(), room);
        return Response.created(URI.create("/api/v1/rooms/" + room.getId())).entity(room).build();
    }

    @GET
    @Path("/{id}")
    public Room getRoomById(@PathParam("id") String id) {
        Room room = dataStore.getRooms().get(id);
        if (room == null) {
            throw new NotFoundException("Room with id '" + id + "' was not found");
        }
        return room;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteRoom(@PathParam("id") String id) {
        Room room = dataStore.getRooms().get(id);
        if (room == null) {
            throw new NotFoundException("Room with id '" + id + "' was not found");
        }
        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Cannot delete room '" + id + "' because sensors are still linked");
        }
        dataStore.getRooms().remove(id);
        return Response.noContent().build();
    }
}
