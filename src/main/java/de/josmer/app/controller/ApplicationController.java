package de.josmer.app.controller;

import de.josmer.app.controller.requests.SearchRequest;
import de.josmer.app.entities.Export;
import de.josmer.app.entities.User;
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
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class ApplicationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class.getName());
    @Autowired
    private IExportRepository exportRep;
    @Autowired
    private IRadiationRepository radiationRep;
    @Autowired
    private IUserRepository userRepository;

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
        if (userRepository.get(login).isPresent()) {
            return "Fehler: Benutzername ist schon vorhanden!";
        }

        Pattern special = Pattern.compile("[!#$%&*()_+=|<>?{}\\[\\]~]");
        Matcher hasSpecial = special.matcher(password);
        if (hasSpecial.find()) {
            return "Fehler: Benutzername enthält ungültige Zeichen!";
        }

        userRepository.saveUser(login, password);

        Optional<User> optionalUser = userRepository.get(login);
        if (optionalUser.isPresent() && optionalUser.get().isActive()) {
            return Token.get(optionalUser.get().getId());
        }

        return "Fehler: Etwas ist schief gelaufen!";
    }

    @GetMapping(value = "/token", produces = MediaType.TEXT_PLAIN_VALUE)
    public String token(@RequestHeader("login") final String login, @RequestHeader("password") final String password) {
        final Optional<User> optionalUser = userRepository.get(login);
        if (optionalUser.isPresent() && Toolbox.isPassword(password, optionalUser.get().getPassword())) {
            LOGGER.info("login");
            return Token.get(optionalUser.get().getId());
        }
        LOGGER.info("login failes");
        return "";
    }

    @GetMapping(value = "/count", produces = MediaType.TEXT_PLAIN_VALUE)
    public String count(@CookieValue("token") final String token) {
        if (!isAccess(Token.getAuthentication(token))) {
            return "-1";
        }
        return Long.toString(radiationRep.count());
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
                    exportRep.getAll(radiationRep.find(getDate(startDate), getDate(endDate), type, lon, lat), lon, lat),
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
            return null;
        }
        return exportRep.getAll(radiationRep.find(getDate(req.getStartDate()), getDate(req.getEndDate()),
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
        return auth.getToken().isPresent() && auth.getUserId().isPresent()
                && userRepository.get(auth.getUserId().getAsInt()).isPresent()
                && userRepository.get(auth.getUserId().getAsInt()).get().isActive()
                && Token.check(auth.getToken().get());
    }
}
