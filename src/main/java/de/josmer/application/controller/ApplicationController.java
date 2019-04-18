package de.josmer.application.controller;

import de.josmer.application.controller.requests.CalculationRequest;
import de.josmer.application.controller.requests.RadiationRequest;
import de.josmer.application.entities.Calculated;
import de.josmer.application.entities.ExportRadiation;
import de.josmer.application.entities.User;
import de.josmer.application.library.geo.GaussKrueger;
import de.josmer.application.library.interfaces.ICalculatedRepository;
import de.josmer.application.library.interfaces.IExportRadiRepository;
import de.josmer.application.library.interfaces.IRadiationRepository;
import de.josmer.application.library.interfaces.IUserRepository;
import de.josmer.application.library.security.Authentication;
import de.josmer.application.library.security.Token;
import de.josmer.application.library.utils.Toolbox;
import org.jxls.template.SimpleExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class ApplicationController {
    private static final String LOGIN_HTML = "src/main/resources/static/html/login.html";
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class.getName());
    private final IExportRadiRepository exportRep;
    private final IRadiationRepository radiationRepository;
    private final IUserRepository userRepository;
    private final ICalculatedRepository calculatedRepository;

    @Autowired
    public ApplicationController(IExportRadiRepository exportRep, IRadiationRepository radiationRepository, IUserRepository userRepository, ICalculatedRepository calculatedRepository) {
        this.exportRep = exportRep;
        this.radiationRepository = radiationRepository;
        this.userRepository = userRepository;
        this.calculatedRepository = calculatedRepository;
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String login() {
        return Toolbox.readFile(LOGIN_HTML);
    }

    @GetMapping(value = "/radi_app", produces = MediaType.TEXT_HTML_VALUE)
    public String radiationApp(@CookieValue("token") final String token) {
        if (isAccess(Token.getAuthentication(token))) {
            return Toolbox.readFile("src/main/resources/static/html/radi.html");
        }
        return Toolbox.readFile(LOGIN_HTML);
    }

    @GetMapping(value = "/calc_app", produces = MediaType.TEXT_HTML_VALUE)
    public String calculationApp(@CookieValue("token") final String token) {
        if (isAccess(Token.getAuthentication(token))) {
            return Toolbox.readFile("src/main/resources/static/html/calc.html");
        }
        return Toolbox.readFile(LOGIN_HTML);
    }

    @GetMapping(value = "/save_user", produces = MediaType.TEXT_HTML_VALUE)
    public String saveUser(@RequestHeader("login") final String login, @RequestHeader("password") final String password) {
        if (isParameter(login, password)) {
            return "Benutzername oder Passwort sind nicht lang genug!";
        }
        if (userRepository.get(login).isPresent()) {
            return "Benutzername ist schon vorhanden!";
        }

        if (isLogin(login) || password.contains(" ")) {
            return "Benutzername oder Passwort enthalten ungültige Zeichen!";
        }
        Optional<User> optionalUser = createUser(login, password);
        if (optionalUser.isPresent() && optionalUser.get().isActive()) {
            return Token.get(optionalUser.get().getId());
        }
        return "Etwas ist schief gelaufen!";
    }

    @GetMapping(value = "/token", produces = MediaType.TEXT_PLAIN_VALUE)
    public String token(@RequestHeader("login") final String login, @RequestHeader("password") final String password) {
        final Optional<User> optionalUser = userRepository.get(login);
        if (optionalUser.isPresent() && Toolbox.isPassword(password, optionalUser.get().getPassword())) {
            LOGGER.info("login successful");
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

    @GetMapping("/export_radi")
    public void exportRadi(HttpServletResponse response, @CookieValue("token") final String token, @RequestParam("startDate") final String startDate, @RequestParam("endDate") final String endDate, @RequestParam("lon") final double lon, @RequestParam("lat") final double lat, @RequestParam("type") final String type) {
        if (!isAccess(Token.getAuthentication(token))) {
            return;
        }
        try {
            response.addHeader("Content-disposition", "attachment; filename=sonneneinstrahlung_" + System.currentTimeMillis() + ".xls");
            response.setContentType("application/vnd.ms-excel");
            new SimpleExporter().gridExport(
                    exportRep.getHeaders(),
                    exportRep.getAll(radiationRepository.findGlobal(new GaussKrueger(), getDate(startDate), getDate(endDate), type, lon, lat), lon, lat),
                    exportRep.getProps(),
                    response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
    }

    @GetMapping("/radiation")
    public List<ExportRadiation> radiation(@CookieValue("token") final String token, final RadiationRequest req) {
        if (!isAccess(Token.getAuthentication(token))) {
            return new ArrayList<>();
        }
        return exportRep.getAll(radiationRepository.findGlobal(new GaussKrueger(), getDate(req.getStartDate()), getDate(req.getEndDate()),
                req.getType(), req.getLon(), req.getLat()), req.getLon(), req.getLat());
    }

    @GetMapping("/calculation")
    public List<ExportRadiation> calculation(@CookieValue("token") final String token, final CalculationRequest req) {
        if (!isAccess(Token.getAuthentication(token))) {
            return new ArrayList<>();
        }
        final int startDate = Integer.valueOf(req.getYear() + "01");
        final int endDate = Integer.valueOf(req.getYear() + "12");

        final double[] eGlobHor = radiationRepository.findGlobal(new GaussKrueger(), startDate, endDate, req.getLon(), req.getLat());
        List<Calculated> calculateds = calculatedRepository.calculateds(eGlobHor, req.getLon(), req.getLat(), req.getAe(), req.getYe(), req.getYear());

        return new LinkedList<>();
    }

    private boolean isParameter(String login, String password) {
        return login == null || password == null || login.equals("") || password.equals("");
    }

    private boolean isLogin(final String login) {
        Pattern special = Pattern.compile("[!#$%&*()_+=|<>?{}\\[\\]~ ]");
        Matcher hasSpecial = special.matcher(login);
        return hasSpecial.find();
    }

    private int getDate(final String date) {
        try {
            return Integer.valueOf(date.replace("-", "").replace("#", ""));
        } catch (NumberFormatException e) {
            LOGGER.info(e.getMessage());
            return 0;
        }
    }

    private Optional<User> createUser(String login, String password) {
        userRepository.saveUser(login, password);
        return userRepository.get(login);
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
