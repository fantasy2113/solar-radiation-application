package de.josmer.solardb.controller;

import de.josmer.solardb.entities.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class TokenController extends Controller {

    @GetMapping(value = "/token", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getToken(@RequestHeader("login") final String login, @RequestHeader("password") final String password) {
        final Optional<User> optionalUser = getUserRep().get(login);
        if (optionalUser.isPresent() && getUserBCrypt().isPassword(password, optionalUser.get().getPassword())) {
            LOGGER.info("login successful: " + login);
            return getJwtToken().create(String.valueOf(optionalUser.get().getId()), "sol", optionalUser.get().getUsername(), TTL_MILLIS);
        }
        LOGGER.info("login failed");
        return "";
    }
}
