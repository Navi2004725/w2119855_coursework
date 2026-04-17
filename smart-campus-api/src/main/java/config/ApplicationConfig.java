package config;

import filter.LoggingFilter;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api/v1")
public class ApplicationConfig extends ResourceConfig {
    public ApplicationConfig() {
        packages("resource", "mapper");
        register(LoggingFilter.class);
    }
}
