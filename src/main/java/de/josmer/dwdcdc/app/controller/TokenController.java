package de.josmer.dwdcdc.app.controller;

import de.josmer.dwdcdc.app.entities.User;
import de.josmer.dwdcdc.app.interfaces.IJwtToken;
import de.josmer.dwdcdc.app.interfaces.IUserBCrypt;
import de.josmer.dwdcdc.app.interfaces.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public final class TokenController extends Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenController.class.getName());

    @Autowired
    public TokenController(IUserRepository userRep, IJwtToken jwtToken, IUserBCrypt userBCrypt) {
        super(userRep, jwtToken, userBCrypt);
    }

    @GetMapping(value = "/token", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getToken(@RequestHeader("login") final String login, @RequestHeader("password") final String password) {
        final Optional<User> optionalUser = userRep.get(login);
        if (optionalUser.isPresent() && userBCrypt.isPassword(password, optionalUser.get().getPassword())) {
            LOGGER.info("login successful: " + login);
            return jwtToken.create(String.valueOf(optionalUser.get().getId()), "sol", optionalUser.get().getUsername(), TTL_MILLIS);
        }
        LOGGER.info("login failed");
        return "";
    }
}
