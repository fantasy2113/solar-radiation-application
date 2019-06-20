package de.josmer.dwdcdc.app.controller;

import de.josmer.dwdcdc.app.entities.User;
import de.josmer.dwdcdc.app.entities.web.WebToken;
import de.josmer.dwdcdc.app.interfaces.IJwtToken;
import de.josmer.dwdcdc.app.interfaces.IUserBCrypt;
import de.josmer.dwdcdc.app.interfaces.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(value = "/create_user")
    public WebToken createUser(@RequestHeader("login") final String login, @RequestHeader("password") final String password) {
        WebToken webToken = new WebToken();
        if (!isParameter(login, password) && userRep.get(login).isEmpty()) {
            Optional<User> optionalUser = getCreatedUser(login, password);
            if (optionalUser.isPresent() && optionalUser.get().isActive()) {
                webToken.setToken(jwtToken.create(String.valueOf(optionalUser.get().getId()), "sol", optionalUser.get().getUsername(), TTL_MILLIS));
                webToken.setSecret(jwtToken.getSecretKey());
                webToken.setAuthorized(true);
                return webToken;
            }
        } else {
            webToken.setUserError(true);
            return webToken;
        }
        webToken.setError(true);
        return webToken;
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
