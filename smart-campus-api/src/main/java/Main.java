import config.ApplicationConfig;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.io.IOException;
import java.net.URI;

public class Main {
    private static final URI BASE_URI = URI.create("http://localhost:8080/");

    public static HttpServer startServer() {
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, new ApplicationConfig());
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = startServer();
        System.out.println("Smart Campus API started at " + BASE_URI);
        System.out.println("Press Enter to stop the server...");
        System.in.read();
        server.shutdownNow();
    }
}
