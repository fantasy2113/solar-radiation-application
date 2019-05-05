package de.josmer.app.controller;

import de.josmer.app.controller.security.Authentication;
import de.josmer.app.controller.security.Token;
import de.josmer.app.library.interfaces.IUserRepository;
import de.josmer.app.model.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.OptionalInt;

abstract class Controller {
    static final Logger LOGGER = LoggerFactory.getLogger(AppController.class.getName());

    final IUserRepository userRep;

    Controller(IUserRepository userRep) {
        this.userRep = userRep;
    }

    boolean isAccess(final Authentication auth) {
        Optional<String> optionalToken = auth.getToken();
        OptionalInt optionalUserId = auth.getUserId();
        if (optionalToken.isPresent() && optionalUserId.isPresent()) {
            Optional<User> optionalUser = userRep.get(optionalUserId.getAsInt());
            return Token.check(optionalToken.get()) && optionalUser.isPresent();
        }
        return false;
    }
}
