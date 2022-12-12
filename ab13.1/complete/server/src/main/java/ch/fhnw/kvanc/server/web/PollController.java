package ch.fhnw.kvanc.server.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ch.fhnw.kvanc.server.domain.Answer;
import ch.fhnw.kvanc.server.domain.Poll;
import ch.fhnw.kvanc.server.repository.PollRepository;
import ch.fhnw.kvanc.server.web.dto.AnswerDTO;
import ch.fhnw.kvanc.server.web.dto.PollDTO;
import ch.fhnw.kvanc.server.web.dto.ResultDTO;
import ch.fhnw.kvanc.server.web.sse.SSEMessagingHandler;

@RestController
@RequestMapping("/polls")
@CrossOrigin
public class PollController {
    private Logger log = LoggerFactory.getLogger(PollController.class);
    private PollRepository pollRepository;

    @Autowired
    private MessagingHandler messagingHandler;

    public PollController(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    // [SSE] subscribe for results
    @GetMapping(path="/results", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin
    public SseEmitter subscribeForResults() {
        if (messagingHandler instanceof SSEMessagingHandler) {
            SSEMessagingHandler handler = (SSEMessagingHandler) messagingHandler;
            SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
            sseEmitter.onCompletion(() -> {
                handler.getResultSubscribers().remove(sseEmitter);
                log.info("Removed sse emitter for result messages");
            });
            handler.getResultSubscribers().add(sseEmitter);
            log.info("Successfully subscribed client");
            return sseEmitter;
        }
        return null;
    }

    // [SSE] subscribe for new questions
    @GetMapping(path = "/questions", params = "token", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin
    public SseEmitter subscribeForQuestions(@RequestParam String token) {
        if (messagingHandler instanceof SSEMessagingHandler) {
            // check if token is an UUID
            try {
                UUID.fromString(token);
            } catch (IllegalArgumentException e) {
                return null;
            }
            SSEMessagingHandler handler = (SSEMessagingHandler) messagingHandler;
            SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
            sseEmitter.onCompletion(() -> {
                handler.getQuestionSubscribers().remove(sseEmitter);
                log.info("Removed sse emitter for answer messages");
            });
            handler.getQuestionSubscribers().add(sseEmitter);
            log.info("Successfully subscribed user with token='{}'", token);
            return sseEmitter;
        }
        return null;
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<PollDTO> createPoll(@RequestBody PollDTO dto) {
        try {
            Poll poll = new Poll("", dto.questionText(), new ArrayList<>());
            poll = pollRepository.savePoll(poll);
            PollDTO pollDto = new PollDTO(poll.id(), dto.questionText());

            this.messagingHandler.publishPoll(pollDto);
            this.messagingHandler.publishResult(calculateResult());

            return ResponseEntity.status(HttpStatus.CREATED).body(pollDto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(params = "token")
    public ResponseEntity<List<PollDTO>> findPolls(@RequestParam String token,
            @RequestParam(required = false) String filter) {
        List<PollDTO> dtos = new ArrayList<>();
        try {
            // check if token is an UUID
            UUID.fromString(token);
            if (filter.equals("last")) {
                Poll poll = pollRepository.getActPoll();
                if (poll != null) {
                    PollDTO pollDto = new PollDTO(poll.id(), poll.question());
                    dtos.add(pollDto);
                    return ResponseEntity.ok().body(dtos);
                }
                return ResponseEntity.notFound().build();
            } else {
                List<Poll> polls = pollRepository.getAllPolls();
                for (Poll poll : polls) {
                    PollDTO pollDto = new PollDTO(poll.id(), poll.question());
                    dtos.add(pollDto);
                }
                return ResponseEntity.ok().body(dtos);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @CrossOrigin
    @PostMapping("/{id}/answers") // HTTP Support
    public ResponseEntity<Void> answer(@DestinationVariable("id") @PathVariable String id, @RequestBody AnswerDTO dto) {
        Poll actPoll = pollRepository.getActPoll();
        if ((actPoll == null) || (!actPoll.id().equals(id))) {
            log.info("Poll given is wrong: id='{}'", dto.token());
            return ResponseEntity.badRequest().build();
        }
        List<Answer> answers = actPoll.answers();
        for (Answer answer : answers) {
            if (answer.token().equals(dto.token())) {
                log.info("Answer already given: '{}'", dto.token());
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
            }
        }
        Answer answer = new Answer(dto.token(), dto.answer());
        actPoll.answers().add(answer);
        try {
            this.messagingHandler.publishResult(calculateResult());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        log.debug("Answer set for {}'", dto.token());
        return ResponseEntity.ok().build();
    }

    @CrossOrigin
    @GetMapping("/last/result")
    public ResponseEntity<ResultDTO> getResult() {
        ResultDTO dto = calculateResult();
        log.debug("Result returned '{}'", dto);
        return ResponseEntity.ok().body(dto);
    }

    private ResultDTO calculateResult() {
        int yes = 0;
        int no = 0;
        Poll actPoll = pollRepository.getActPoll();
        if (actPoll != null) {
            List<Answer> answers = actPoll.answers();
            for (Answer answer : answers) {
                if (answer.isYes()) {
                    yes++;
                } else {
                    no++;
                }
            }
        }
        return new ResultDTO(yes, no);
    }

}