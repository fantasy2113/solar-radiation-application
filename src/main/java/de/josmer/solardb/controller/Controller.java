package de.josmer.solardb.controller;

import de.josmer.solardb.controller.security.JwtToken;
import de.josmer.solardb.repositories.UserRepository;
import de.josmer.solardb.utils.UserBCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

abstract class Controller {
    static final Logger LOGGER = LoggerFactory.getLogger(AppController.class.getName());
    static final Long TTL_MILLIS = TimeUnit.DAYS.toMillis(5);
    @Autowired
    private UserRepository userRep;
    @Autowired
    private JwtToken jwtToken;
    @Autowired
    private UserBCrypt userBCrypt;

    final boolean isAccess(final String token) {
        try {
            return userRep.get(Integer.valueOf(jwtToken.decode(token).getId())).isPresent();
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return false;
    }

    public UserRepository getUserRep() {
        return userRep;
    }

    public JwtToken getJwtToken() {
        return jwtToken;
    }

    public UserBCrypt getUserBCrypt() {
        return userBCrypt;
    }
}
