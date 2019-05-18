package de.josmer.dwdcdc.springboot.base.controller;

import de.josmer.dwdcdc.springboot.base.interfaces.IJwtToken;
import de.josmer.dwdcdc.springboot.base.interfaces.IUserBCrypt;
import de.josmer.dwdcdc.springboot.base.interfaces.IUserRepository;
import de.josmer.dwdcdc.springboot.controller.AppController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public abstract class Controller {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AppController.class.getName());
    protected static final Long TTL_MILLIS = TimeUnit.DAYS.toMillis(5);
    protected final IUserRepository userRep;
    protected final IJwtToken jwtToken;
    protected IUserBCrypt userBCrypt;

    protected Controller(IUserRepository userRep, IJwtToken jwtToken, IUserBCrypt userBCrypt) {
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
