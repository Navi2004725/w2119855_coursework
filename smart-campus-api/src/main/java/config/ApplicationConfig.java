package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import filter.LoggingFilter;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.ext.ContextResolver;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api/v1")
public class ApplicationConfig extends ResourceConfig {
    public ApplicationConfig() {
        packages("resource", "mapper");
        register(LoggingFilter.class);
        register((ContextResolver<ObjectMapper>) type -> new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
    }
}
