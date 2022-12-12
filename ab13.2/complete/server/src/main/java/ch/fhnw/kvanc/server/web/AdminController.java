package ch.fhnw.kvanc.server.web;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.fhnw.kvanc.server.repository.PollRepository;

@RestController
@RequestMapping("/admin")
public class AdminController {
    PollRepository pollRepository;

    // using constructor injection instead of @Autowired
    public AdminController(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    @PutMapping(value = "/polls", params = "method")
    public ResponseEntity<Void> admin(@RequestParam String method) throws IOException {
        if ((method != null) && (method.equals("reset"))) {
            pollRepository.reset();
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
