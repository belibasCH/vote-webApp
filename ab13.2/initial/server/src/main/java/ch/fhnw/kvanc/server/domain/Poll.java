package ch.fhnw.kvanc.server.domain;

import java.util.List;

public record Poll(
        String id,
        String question,
        List<Answer> answers) {
}
