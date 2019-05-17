package de.josmer.solarradiation.controller;

import de.josmer.solarradiation.controller.security.JwtToken;
import de.josmer.solarradiation.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class Controller {
    static final Logger LOGGER = LoggerFactory.getLogger(AppController.class.getName());
    final UserRepository userRep;
    final JwtToken jwtToken;

    public Controller(UserRepository userRep, JwtToken jwtToken) {
        this.userRep = userRep;
        this.jwtToken = jwtToken;
    }

    boolean isAccess(final String token) {
        try {
            return userRep.get(Integer.valueOf(jwtToken.decode(token).getId())).isPresent();
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return false;
    }
}
