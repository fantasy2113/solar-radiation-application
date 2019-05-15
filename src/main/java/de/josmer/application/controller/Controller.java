package de.josmer.application.controller;

import de.josmer.application.controller.security.Authentication;
import de.josmer.application.controller.security.Token;
import de.josmer.application.library.interfaces.IUserRepository;
import de.josmer.application.model.entities.User;
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
