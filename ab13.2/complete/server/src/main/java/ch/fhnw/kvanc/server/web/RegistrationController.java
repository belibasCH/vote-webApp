package ch.fhnw.kvanc.server.web;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.fhnw.kvanc.server.web.dto.RegistrationDTO;
import ch.fhnw.kvanc.server.web.dto.TokenDTO;

@RestController
@RequestMapping("/registrations")
public class RegistrationController {
    private Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @CrossOrigin
    @PostMapping
    public ResponseEntity<TokenDTO> createTokenForUser(@RequestBody RegistrationDTO dto) {
        String email = dto.email();
        if ((email == null) || (! email.contains("@"))) {
            logger.info("No email given: '{}'", email);
            return ResponseEntity.badRequest().build();
        }
        TokenDTO tokenDTO = new TokenDTO(UUID.randomUUID().toString());
        logger.debug("Token created for '" + email + "'");
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenDTO);
    }
}