package ch.fhnw.kvanc.server.web.sse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ch.fhnw.kvanc.server.web.MessagingHandler;
import ch.fhnw.kvanc.server.web.dto.PollDTO;
import ch.fhnw.kvanc.server.web.dto.ResultDTO;

@Component
public class SSEMessagingHandler implements MessagingHandler {
    private Logger log = LoggerFactory.getLogger(SSEMessagingHandler.class);

    private Collection<SseEmitter> questionSubscribers = Collections.synchronizedCollection(new ArrayList<SseEmitter>());
    private Collection<SseEmitter> resultSubscribers = Collections.synchronizedCollection(new ArrayList<SseEmitter>());

    public Collection<SseEmitter> getQuestionSubscribers() {
        return questionSubscribers;
    }

    public Collection<SseEmitter> getResultSubscribers() {
        return resultSubscribers;
    }

    @Override
    public void publishPoll(PollDTO pollDTO) throws IOException {
        questionSubscribers.forEach((sseEmitter) -> {
            try {
                sseEmitter.send(pollDTO);
            } catch (IOException e) {
                questionSubscribers.remove(sseEmitter);
                log.error("Could not publish question to subscriber. Removed!");
            }
        });
    }

    @Override
    public void publishResult(ResultDTO resultDTO) throws IOException {
        resultSubscribers.forEach((sseEmitter) -> {
            try {
                sseEmitter.send(resultDTO);
            } catch (IOException e) {
                resultSubscribers.remove(sseEmitter);
                log.error("Could not publish result to subscriber. Removed!");
            }
        });
    }

}
