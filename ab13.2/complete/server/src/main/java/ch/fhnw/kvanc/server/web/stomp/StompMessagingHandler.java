package ch.fhnw.kvanc.server.web.stomp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import ch.fhnw.kvanc.server.web.MessagingHandler;
import ch.fhnw.kvanc.server.web.dto.PollDTO;
import ch.fhnw.kvanc.server.web.dto.ResultDTO;

@Component
public class StompMessagingHandler implements MessagingHandler {
    @Autowired private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void publishPoll(PollDTO pollDTO) {
        this.simpMessagingTemplate.convertAndSend("/topic/question", pollDTO);
    }

    @Override
    public void publishResult(ResultDTO resultDTO) {
        this.simpMessagingTemplate.convertAndSend("/topic/result", resultDTO);
    }
    
}
