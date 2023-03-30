package stompdemo.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        SimpMessageType simpMessageType = event.getMessage().getHeaders().get("simpMessageType", SimpMessageType.class); // like SUBSCRIBE
        StompCommand stompCommand = event.getMessage().getHeaders().get("stompCommand", StompCommand.class); // like SUBSCRIBE
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        var sessionAttrs = headerAccessor.getSessionAttributes();
        var nativeHeaders = (Map<String, List<String>>)event.getMessage().getHeaders().get("nativeHeaders");
        String what = nativeHeaders.get("what").get(0); // "client subscribes"
        String destination = nativeHeaders.get("destination").get(0); // "/topic/events/100500"
        log.info("what: " + what);
        log.info("destination: " + destination);
    }
}