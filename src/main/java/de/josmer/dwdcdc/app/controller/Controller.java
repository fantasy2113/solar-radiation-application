package de.josmer.dwdcdc.app.controller;

import de.josmer.dwdcdc.app.entities.User;
import de.josmer.dwdcdc.app.interfaces.IJwtToken;
import de.josmer.dwdcdc.app.interfaces.IUserBCrypt;
import de.josmer.dwdcdc.app.interfaces.IUserRepository;
import de.josmer.dwdcdc.library.enums.SolRadTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

abstract class Controller {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AppController.class.getName());
    static final Long TTL_MILLIS = TimeUnit.DAYS.toMillis(5);
    final IUserRepository userRep;
    final IJwtToken jwtToken;
    IUserBCrypt userBCrypt;

    Controller(IUserRepository userRep, IJwtToken jwtToken, IUserBCrypt userBCrypt) {
        this.userRep = userRep;
        this.jwtToken = jwtToken;
        this.userBCrypt = userBCrypt;
    }

    final SolRadTypes getSolRadTypes(String type) {
        return SolRadTypes.valueOf(type.toUpperCase(Locale.ENGLISH));
    }

    final boolean isAccess(final String token) {
        try {
            Optional<User> userOptional = userRep.get(Integer.valueOf(jwtToken.decode(token).getId()));
            return userOptional.isPresent() && userOptional.get().isActive();
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
        return false;
    }
}
