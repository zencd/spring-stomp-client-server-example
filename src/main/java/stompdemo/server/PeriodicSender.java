package stompdemo.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static stompdemo.server.ServerApp.sessionId;
import static stompdemo.server.ServerApp.topicEvents;

@Component
@Slf4j
@RequiredArgsConstructor
public class PeriodicSender {

    private final SimpMessagingTemplate template;

    @Scheduled(fixedRate = 3000)
    public void run() {
        String destination = topicEvents(sessionId);
        ServerMessage msg = new ServerMessage("Current time is " + System.nanoTime());
        log.info("Time broadcast: {}", msg);
        template.convertAndSend(destination, msg);
    }
}
