package de.josmer.solardb.base.controller;

import de.josmer.solardb.base.UserBCrypt;
import de.josmer.solardb.base.interfaces.IUserRepository;
import de.josmer.solardb.base.security.JwtToken;
import de.josmer.solardb.controller.AppController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public abstract class Controller {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AppController.class.getName());
    protected static final Long TTL_MILLIS = TimeUnit.DAYS.toMillis(5);
    protected final IUserRepository userRep;
    protected final JwtToken jwtToken;
    protected UserBCrypt userBCrypt;

    protected Controller(IUserRepository userRep, JwtToken jwtToken, UserBCrypt userBCrypt) {
        this.userRep = userRep;
        this.jwtToken = jwtToken;
        this.userBCrypt = userBCrypt;
    }

    protected final boolean isAccess(final String token) {
        try {
            return userRep.get(Integer.valueOf(jwtToken.decode(token).getId())).isPresent();
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return false;
    }
}
