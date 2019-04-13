package de.josmer.app.controller;

import de.josmer.app.controller.requests.SearchRequest;
import de.josmer.app.entities.Export;
import de.josmer.app.entities.User;
import de.josmer.app.lib.geo.GaussKrueger;
import de.josmer.app.lib.interfaces.IExportRepository;
import de.josmer.app.lib.interfaces.IRadiationRepository;
import de.josmer.app.lib.interfaces.IUserRepository;
import de.josmer.app.lib.security.Authentication;
import de.josmer.app.lib.security.Token;
import de.josmer.app.lib.utils.Toolbox;
import org.jxls.template.SimpleExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class ApplicationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class.getName());
    private final IExportRepository exportRep;
    private final IRadiationRepository radiationRepository;
    private final IUserRepository userRepository;

    @Autowired
    public ApplicationController(IExportRepository exportRep, IRadiationRepository radiationRepository, IUserRepository userRepository) {
        this.exportRep = exportRep;
        this.radiationRepository = radiationRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String login() {
        return Toolbox.readFile("src/main/resources/static/html/login.html");
    }

    @GetMapping(value = "/app", produces = MediaType.TEXT_HTML_VALUE)
    public String app(@CookieValue("token") final String token) {
        if (isAccess(Token.getAuthentication(token))) {
            return Toolbox.readFile("src/main/resources/static/html/app.html");
        }
        return Toolbox.readFile("src/main/resources/static/html/login.html");
    }

    @GetMapping(value = "/save_user", produces = MediaType.TEXT_HTML_VALUE)
    public String saveUser(@RequestHeader("login") final String login, @RequestHeader("password") final String password) {
        if (login == null || password == null || login.equals("") || password.equals("")) {
            return "Benutzername oder Passwort sind nicht lang genug!";
        }
        if (userRepository.get(login).isPresent()) {
            return "Benutzername ist schon vorhanden!";
        }
        Pattern special = Pattern.compile("[!#$%&*()_+=|<>?{}\\[\\]~ ]");
        Matcher hasSpecial = special.matcher(login);
        if (hasSpecial.find() || password.contains(" ")) {
            return "Benutzername oder Passwort enthalten ungültige Zeichen!";
        }
        userRepository.saveUser(login, password);
        Optional<User> optionalUser = userRepository.get(login);
        if (optionalUser.isPresent() && optionalUser.get().isActive()) {
            return Token.get(optionalUser.get().getId());
        }
        return "Etwas ist schief gelaufen!";
    }

    @GetMapping(value = "/token", produces = MediaType.TEXT_PLAIN_VALUE)
    public String token(@RequestHeader("login") final String login, @RequestHeader("password") final String password) {
        final Optional<User> optionalUser = userRepository.get(login);
        if (optionalUser.isPresent() && Toolbox.isPassword(password, optionalUser.get().getPassword())) {
            LOGGER.info("login");
            return Token.get(optionalUser.get().getId());
        }
        LOGGER.info("login failed");
        return "";
    }

    @GetMapping(value = "/count", produces = MediaType.TEXT_PLAIN_VALUE)
    public String count(@CookieValue("token") final String token) {
        if (!isAccess(Token.getAuthentication(token))) {
            return "-1";
        }
        return Long.toString(radiationRepository.count());
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response, @CookieValue("token") final String token, @RequestParam("startDate") final String startDate, @RequestParam("endDate") final String endDate, @RequestParam("lon") final double lon, @RequestParam("lat") final double lat, @RequestParam("type") final String type) {
        if (!isAccess(Token.getAuthentication(token))) {
            return;
        }
        try {
            response.addHeader("Content-disposition", "attachment; filename=sonneneinstrahlung_" + System.currentTimeMillis() + ".xls");
            response.setContentType("application/vnd.ms-excel");
            new SimpleExporter().gridExport(
                    exportRep.getHeaders(),
                    exportRep.getAll(radiationRepository.find(new GaussKrueger(), getDate(startDate), getDate(endDate), type, lon, lat), lon, lat),
                    exportRep.getProps(),
                    response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
    }

    @GetMapping("/find")
    public List<Export> find(@CookieValue("token") final String token, final SearchRequest req) {
        if (!isAccess(Token.getAuthentication(token))) {
            return new ArrayList<>();
        }
        return exportRep.getAll(radiationRepository.find(new GaussKrueger(), getDate(req.getStartDate()), getDate(req.getEndDate()),
                req.getType(), req.getLon(), req.getLat()), req.getLon(), req.getLat());
    }


    private int getDate(final String date) {
        try {
            return Integer.valueOf(date.replace("-", "").replace("#", ""));
        } catch (NumberFormatException e) {
            LOGGER.info(e.getMessage());
            return 0;
        }
    }

    private boolean isAccess(final Authentication auth) {
        Optional<String> optionalToken = auth.getToken();
        OptionalInt optionalUserId = auth.getUserId();
        if (optionalToken.isPresent() && optionalUserId.isPresent()) {
            Optional<User> optionalUser = userRepository.get(optionalUserId.getAsInt());
            return Token.check(optionalToken.get()) && optionalUser.isPresent();
        }
        return false;
    }
}
