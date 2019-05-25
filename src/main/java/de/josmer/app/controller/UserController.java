package de.josmer.app.controller;

import de.josmer.app.entities.User;
import de.josmer.app.interfaces.IJwtToken;
import de.josmer.app.interfaces.IUserBCrypt;
import de.josmer.app.interfaces.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public final class UserController extends Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class.getName());

    @Autowired
    public UserController(IUserRepository userRep, IJwtToken jwtToken, IUserBCrypt userBCrypt) {
        super(userRep, jwtToken, userBCrypt);
    }

    @GetMapping(value = "/create_user", produces = MediaType.TEXT_HTML_VALUE)
    public String createUser(@RequestHeader("login") final String login, @RequestHeader("password") final String password) {
        if (isParameter(login, password)) {
            return "Benutzername oder Passwort sind nicht lang genug!";
        }
        if (userRep.get(login).isPresent()) {
            return "Benutzername ist schon vorhanden!";
        }
        Optional<User> optionalUser = getCreatedUser(login, password);
        if (optionalUser.isPresent() && optionalUser.get().isActive()) {
            return jwtToken.create(String.valueOf(optionalUser.get().getId()), "sol", optionalUser.get().getUsername(), TTL_MILLIS);
        }
        return "Etwas ist schief gelaufen!";
    }

    private Optional<User> getCreatedUser(String username, String password) {
        LOGGER.info("create: " + username);
        userRep.createUser(username, password);
        return userRep.get(username);
    }

    private boolean isParameter(String login, String password) {
        return login == null || password == null || login.equals("") || password.equals("");
    }
}
