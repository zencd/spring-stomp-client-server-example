package stompdemo.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import stompdemo.client.ClientMessage;

import static stompdemo.server.ServerApp.clientMessagesShort;
import static stompdemo.server.ServerApp.sessionId;
import static stompdemo.server.ServerApp.topicEvents;

@Controller
@Slf4j
public class ServerController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping(clientMessagesShort)
    public void onClientMessage(ClientMessage message, SimpMessageHeaderAccessor headers) {
        log.info("Received a client message: {}", message.getText());

        var simpSessionId = headers.getHeader("simpSessionId"); // like b4e69700-4435-4814-5aab-0cba738a3cab

        {
            ServerMessage reply = new ServerMessage("Hello back: " + message.getText());
            var replyDest = topicEvents(sessionId);
            log.info("Sending a reply to {} - {}", replyDest, reply);
            messagingTemplate.convertAndSend(replyDest, reply);
        }
    }
}
