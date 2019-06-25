package de.josmer.dwdcdc.app.controller;

import de.josmer.dwdcdc.app.entities.User;
import de.josmer.dwdcdc.app.entities.web.WebToken;
import de.josmer.dwdcdc.app.interfaces.IJwtToken;
import de.josmer.dwdcdc.app.interfaces.IUserBCrypt;
import de.josmer.dwdcdc.app.interfaces.IUserRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class TokenController extends Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenController.class.getName());

    @Autowired
    public TokenController(IUserRepository userRep, IJwtToken jwtToken, IUserBCrypt userBCrypt) {
        super(userRep, jwtToken, userBCrypt);
    }

    @GetMapping(value = "/token")
    public WebToken getToken(@RequestHeader("login") final String login, @RequestHeader("password") final String password) {
        WebToken webToken = new WebToken();
        final Optional<User> optionalUser = userRep.get(login);
        if (optionalUser.isPresent() && check(password, optionalUser.get())) {
            userRep.updateLastLogin(optionalUser.get());
            LOGGER.info("login successful: " + login);
            webToken.setToken(initToken(optionalUser.get()));
            webToken.setSecret(jwtToken.getSecretKey());
            webToken.setAuthorized(true);
            return webToken;
        }
        LOGGER.info("login failed");
        return webToken;
    }

    private boolean check(String password, User user) {
        return userBCrypt.isPassword(password, user.getPassword()) && user.isActive();
    }

    private String initToken(User user) {
        return jwtToken.create(String.valueOf(user.getId()), "sol", user.getUsername(), TTL_MILLIS);
    }
}
