package ch.fhnw.kvanc.server.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
    private Logger logger = LoggerFactory.getLogger(HelloController.class);

    @GetMapping
    public ResponseEntity<String> sayHello() {
        logger.debug("Server called successfully");
        return ResponseEntity
                .ok()
                .body("Hello from server");
    }

    @GetMapping(value="/users", params = "id")
    public ResponseEntity<String> sayHelloWithRequestParameter(@RequestParam String id) {
        logger.debug("Server called successfully");
        return ResponseEntity
                .ok()
                .body("Hello from server to " + id);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<String> sayHelloWithPathVariable(@PathVariable String id) {
        logger.debug("Server called successfully");
        return ResponseEntity
                .ok()
                .body("Hello from server to "+ id);
    }
}
