package stompdemo.client;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import stompdemo.server.ServerApp;
import stompdemo.server.ServerMessage;

import java.lang.reflect.Type;

import static stompdemo.server.ServerApp.clientMessagesFull;
import static stompdemo.server.ServerApp.sessionId;
import static stompdemo.server.ServerApp.topicEvents;

@Slf4j
public class ClientApp extends StompSessionHandlerAdapter {

    @SneakyThrows
    public static void run() {
        var webSocketClient = new StandardWebSocketClient();
        var stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.setTaskScheduler(new ConcurrentTaskScheduler());
        stompClient.connect(ServerApp.handshakeUrl, new ClientApp());
    }

    @SneakyThrows
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.warn("New session: {}", session.getSessionId());
        {
            // subscribe
            String dest = topicEvents(sessionId);
            log.warn("Subscribing to {}", dest);
            var headers = new StompHeaders();
            headers.setDestination(dest);
            headers.set("time", "" + System.nanoTime());
            headers.set("what", "client subscribes");
            session.subscribe(headers, this);
        }
        {
            // send the first message
            var msg = new ClientMessage("from client " + System.nanoTime());
            log.warn("Sending to {} - {}", clientMessagesFull, msg);
            var headers = new StompHeaders();
            headers.setDestination(clientMessagesFull);
            headers.set("time", "" + System.nanoTime());
            headers.set("what", "client sends message");
            session.send(headers, msg);
        }
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ServerMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        String dest = headers.getDestination(); // like /topic/events/100500
        ServerMessage msg = (ServerMessage) payload;
        log.warn("Received a server message: {} - {}", dest, msg.text());
    }
}
