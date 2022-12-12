package ch.fhnw.kvanc.server.domain;

public record Answer(
        String token,
        // tracks vote, what can be "yes" or "no"
        boolean isYes) {
}