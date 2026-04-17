package resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {
    @GET
    public Map<String, Object> discover() {
        return Map.of(
                "version", "v1",
                "contact", "admin@smartcampus.com",
                "resources", Map.of(
                        "rooms", "/api/v1/rooms",
                        "sensors", "/api/v1/sensors"
                )
        );
    }
}
