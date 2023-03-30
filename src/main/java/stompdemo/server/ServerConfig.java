package stompdemo.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static stompdemo.server.ServerApp.handshakeEndpoint;
import static stompdemo.server.ServerApp.clientMessagesPrefix;
import static stompdemo.server.ServerApp.topicPrefix;

@Configuration
@EnableWebSocketMessageBroker
@EnableScheduling
public class ServerConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(topicPrefix);
        config.setApplicationDestinationPrefixes(clientMessagesPrefix);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(handshakeEndpoint);
    }

    // implements WebSocketConfigurer
    //@Override
    //public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    //    registry.addHandler(new , "/connect")
    //            .addInterceptors(new HttpSessionHandshakeInterceptor());
    //}
}
