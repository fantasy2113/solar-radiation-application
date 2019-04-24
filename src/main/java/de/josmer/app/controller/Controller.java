package de.josmer.app.controller;

import de.josmer.app.entities.User;
import de.josmer.app.library.interfaces.*;
import de.josmer.app.library.security.Authentication;
import de.josmer.app.library.security.Token;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class Controller {

    static final String LOGIN_HTML = "src/main/resources/static/html/login.html";
    static final Logger LOGGER = LoggerFactory.getLogger(AppController.class.getName());
    final ISolarRadiationExport solRadiExport;
    final ISolarRadiationInclinedExport solRadIncRepo;
    final ISolarRadiationRepository solRadiRepo;
    final IUserRepository userRepo;
    final ISolarRadiationInclinedRepository solRadiIncRepo;

    Controller(ISolarRadiationExport solRadiExport, ISolarRadiationInclinedExport solRadIncRepo, ISolarRadiationRepository solRadiRepo, IUserRepository userRepo, ISolarRadiationInclinedRepository solRadiIncRepo) {
        this.solRadiExport = solRadiExport;
        this.solRadIncRepo = solRadIncRepo;
        this.solRadiRepo = solRadiRepo;
        this.userRepo = userRepo;
        this.solRadiIncRepo = solRadiIncRepo;
    }

    boolean isParameter(String login, String password) {
        return login == null || password == null || login.equals("") || password.equals("");
    }

    boolean isLogin(final String login) {
        Pattern special = Pattern.compile("[!#$%&*()_+=|<>?{}\\[\\]~ ]");
        Matcher hasSpecial = special.matcher(login);
        return hasSpecial.find();
    }

    int getDate(final String date) {
        try {
            return Integer.valueOf(date.replace("-", "").replace("#", ""));
        } catch (NumberFormatException e) {
            LOGGER.info(e.getMessage());
            return 0;
        }
    }

    Optional<User> createUser(String login, String password) {
        userRepo.saveUser(login, password);
        return userRepo.get(login);
    }

    boolean isAccess(final Authentication auth) {
        Optional<String> optionalToken = auth.getToken();
        OptionalInt optionalUserId = auth.getUserId();
        if (optionalToken.isPresent() && optionalUserId.isPresent()) {
            Optional<User> optionalUser = userRepo.get(optionalUserId.getAsInt());
            return Token.check(optionalToken.get()) && optionalUser.isPresent();
        }
        return false;
    }
}
