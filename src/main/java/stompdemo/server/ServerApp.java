package stompdemo.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import stompdemo.client.ClientApp;

@Configuration
@SpringBootApplication
public class ServerApp {

    public static final String sessionId = "100500";

    public static final String handshakeEndpoint = "/connect";
    public static final String handshakeUrl = "ws://127.0.0.1:8080" + handshakeEndpoint;

    // messages from server to client
    public static final String topicPrefix = "/topic";
    public static String topicEvents(String sessionId) {
        return "/topic/events/%s".formatted(sessionId);
    }

    // messages from client to server
    public static final String clientMessagesPrefix = "/events";
    public static final String clientMessagesShort = "/push";
    public static final String clientMessagesFull = "/events/push";

    @EventListener(value = ApplicationReadyEvent.class)
    public void ready() {
        ClientApp.run();
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerApp.class, args);
    }
}
