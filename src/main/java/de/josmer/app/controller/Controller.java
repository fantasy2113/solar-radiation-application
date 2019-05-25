package de.josmer.app.controller;

import de.josmer.app.entities.User;
import de.josmer.app.interfaces.IJwtToken;
import de.josmer.app.interfaces.IUserBCrypt;
import de.josmer.app.interfaces.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

abstract class Controller {
    private static final Logger A_LOGGER = LoggerFactory.getLogger(AppController.class.getName());
    static final Long TTL_MILLIS = TimeUnit.DAYS.toMillis(5);
    final IUserRepository userRep;
    final IJwtToken jwtToken;
    IUserBCrypt userBCrypt;

    Controller(IUserRepository userRep, IJwtToken jwtToken, IUserBCrypt userBCrypt) {
        this.userRep = userRep;
        this.jwtToken = jwtToken;
        this.userBCrypt = userBCrypt;
    }

    final boolean isAccess(final String token) {
        try {
            Optional<User> userOptional = userRep.get(Integer.valueOf(jwtToken.decode(token).getId()));
            return userOptional.isPresent() && userOptional.get().isActive();
        } catch (Exception e) {
            A_LOGGER.info(e.getMessage());
        }
        return false;
    }
}
