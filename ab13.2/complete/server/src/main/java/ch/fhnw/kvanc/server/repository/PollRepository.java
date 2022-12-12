package ch.fhnw.kvanc.server.repository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ch.fhnw.kvanc.server.domain.Poll;

@Component
public class PollRepository {
    private Logger log = LoggerFactory.getLogger(PollRepository.class);
    private List<Poll> polls = new ArrayList<Poll>();

    public Poll savePoll(Poll p) {
        Poll poll = new Poll(Integer.toString(polls.size()), p.question(), p.answers());
        polls.add(poll);
        log.debug("Successfully added poll[{}] to repository", poll.id());
        return poll;
    }

    public List<Poll> getAllPolls() {
        return polls;
    }

    public Poll getActPoll() {
        Poll last = null;
        if (polls.size()>0) {
            last = polls.get(polls.size()-1);
        }
        return last;
    }

    public void reset() {
        this.polls = new ArrayList<Poll>();
        log.info("Reset successfully");
    }

}