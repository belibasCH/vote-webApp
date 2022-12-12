package ch.fhnw.kvanc.server.web;

import java.io.IOException;

import ch.fhnw.kvanc.server.web.dto.PollDTO;
import ch.fhnw.kvanc.server.web.dto.ResultDTO;

public interface MessagingHandler {
    void publishPoll(PollDTO pollDTO) throws IOException;
    void publishResult(ResultDTO resultDTO) throws IOException;
}
