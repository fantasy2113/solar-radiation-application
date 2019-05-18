package de.josmer.solardb.base.controller;

import de.josmer.solardb.base.UserBCrypt;
import de.josmer.solardb.base.interfaces.IUser;
import de.josmer.solardb.base.interfaces.IUserRepository;
import de.josmer.solardb.base.security.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class TokenController extends Controller {

    @Autowired
    public TokenController(IUserRepository userRep, JwtToken jwtToken, UserBCrypt userBCrypt) {
        super(userRep, jwtToken, userBCrypt);
    }

    @GetMapping(value = "/token", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getToken(@RequestHeader("login") final String login, @RequestHeader("password") final String password) {
        final Optional<IUser> optionalUser = userRep.get(login);
        if (optionalUser.isPresent() && userBCrypt.isPassword(password, optionalUser.get().getPassword())) {
            LOGGER.info("login successful: " + login);
            return jwtToken.create(String.valueOf(optionalUser.get().getId()), "sol", optionalUser.get().getUsername(), TTL_MILLIS);
        }
        LOGGER.info("login failed");
        return "";
    }
}
