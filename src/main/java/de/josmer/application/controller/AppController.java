package de.josmer.application.controller;

import de.josmer.application.controller.requests.IrrRequest;
import de.josmer.application.controller.requests.RadRequest;
import de.josmer.application.controller.security.JwtToken;
import de.josmer.application.library.geo.GaussKruger;
import de.josmer.application.library.interfaces.*;
import de.josmer.application.library.utils.Toolbox;
import de.josmer.application.model.entities.SolIrrExp;
import de.josmer.application.model.entities.SolRadExp;
import de.josmer.application.model.entities.User;
import org.jxls.template.SimpleExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
public class AppController extends Controller {
    private static final Long TTL_MILLIS = TimeUnit.DAYS.toMillis(5);
    private final ISolRadExporter solRadExp;
    private final ISolIrrExporter solIrrExp;
    private final ISolRadRepository solRadRep;
    private final ISolIrrRepository solIrrRep;

    @Autowired
    public AppController(IUserRepository userRep, ISolRadExporter solRadExp, ISolIrrExporter solIrrExp, ISolRadRepository solRadRep, ISolIrrRepository solIrrRep, JwtToken jwtToken) {
        super(userRep, jwtToken);
        this.solRadExp = solRadExp;
        this.solIrrExp = solIrrExp;
        this.solRadRep = solRadRep;
        this.solIrrRep = solIrrRep;
    }

    @GetMapping(value = "/create_user", produces = MediaType.TEXT_HTML_VALUE)
    public String saveUser(@RequestHeader("login") final String login, @RequestHeader("password") final String password) {

        if (isParameter(login, password)) {
            return "Benutzername oder Passwort sind nicht lang genug!";
        }

        if (userRep.get(login).isPresent()) {
            return "Benutzername ist schon vorhanden!";
        }

        Optional<User> optionalUser = getCreatedUser(login, password);

        if (optionalUser.isPresent() && optionalUser.get().isActive()) {
            return jwtToken.create(String.valueOf(optionalUser.get().getId()), "sol", optionalUser.get().getLogin(), TTL_MILLIS);
        }

        return "Etwas ist schief gelaufen!";
    }

    @GetMapping(value = "/token", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getToken(@RequestHeader("login") final String login, @RequestHeader("password") final String password) {
        final Optional<User> optionalUser = userRep.get(login);

        if (optionalUser.isPresent() && Toolbox.isPassword(password, optionalUser.get().getPassword())) {
            LOGGER.info("login successful");
            return jwtToken.create(String.valueOf(optionalUser.get().getId()), "sol", optionalUser.get().getLogin(), TTL_MILLIS);
        }

        LOGGER.info("login failed");
        return "";
    }

    @GetMapping(value = "/number_of_rad", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getNumberOfRad(@CookieValue("token") final String token) {
        if (!isAccess(token)) {
            return "-1";
        }
        return Long.toString(solRadRep.count());
    }

    @GetMapping("/export_rad")
    public void exportRad(HttpServletResponse response, @CookieValue("token") final String token, @RequestParam("startDate") final String startDate, @RequestParam("endDate") final String endDate, @RequestParam("lon") final double lon, @RequestParam("lat") final double lat, @RequestParam("type") final String type) {
        if (!isAccess(token)) {
            return;
        }
        try {
            response.addHeader("Content-disposition", "attachment; filename=sonneneinstrahlung_" + System.currentTimeMillis() + ".xls");
            response.setContentType("application/vnd.ms-excel");
            new SimpleExporter().gridExport(
                    solRadExp.getHeaders(),
                    solRadExp.getItems(solRadRep.find(new GaussKruger(), getDate(startDate), getDate(endDate), type, lon, lat), lon, lat),
                    solRadExp.getProps(),
                    response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
    }

    @GetMapping("/export_irr")
    public void exportIrr(HttpServletResponse response, @CookieValue("token") final String token, @RequestParam("year") final int year, @RequestParam("lon") final double lon, @RequestParam("lat") final double lat, @RequestParam("ae") final int ae, @RequestParam("ye") final int ye) {
        if (!isAccess(token)) {
            return;
        }
        try {
            response.addHeader("Content-disposition", "attachment; filename=umrechnung_" + System.currentTimeMillis() + ".xls");
            response.setContentType("application/vnd.ms-excel");
            new SimpleExporter().gridExport(
                    solIrrExp.getHeaders(),
                    solIrrExp.getItems(solIrrRep.getSolRadInc(solRadRep.findGlobal(new GaussKruger(), getStartDate(year), getEndDate(year), lon, lat), lon, lat, ae, ye, year), lon, lat),
                    solIrrExp.getProps(),
                    response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
    }

    @GetMapping("/rad")
    public List<SolRadExp> getRad(@CookieValue("token") final String token, final RadRequest req) {
        if (!isAccess(token)) {
            return new ArrayList<>();
        }
        return solRadExp.getItems(solRadRep.find(new GaussKruger(), getDate(req.getStartDate()), getDate(req.getEndDate()), req.getType(), req.getLon(), req.getLat()), req.getLon(), req.getLat());
    }

    @GetMapping("/irr")
    public List<SolIrrExp> getIrr(@CookieValue("token") final String token, final IrrRequest req) {
        if (!isAccess(token)) {
            return new ArrayList<>();
        }
        return solIrrExp.getItems(solIrrRep.getSolRadInc(solRadRep.findGlobal(new GaussKruger(), getStartDate(req.getYear()), getEndDate(req.getYear()), req.getLon(), req.getLat()), req.getLon(), req.getLat(), req.getAe(), req.getYe(), req.getYear()), req.getLon(), req.getLat());
    }

    private boolean isParameter(String login, String password) {
        return login == null || password == null || login.equals("") || password.equals("");
    }

    private int getDate(final String date) {
        try {
            return Integer.valueOf(date.replace("-", "").replace("#", ""));
        } catch (NumberFormatException e) {
            LOGGER.info(e.getMessage());
            return 0;
        }
    }

    private Optional<User> getCreatedUser(String login, String password) {
        userRep.saveUser(login, password);
        return userRep.get(login);
    }

    private Integer getEndDate(int year) {
        return Integer.valueOf(year + "12");
    }

    private Integer getStartDate(int year) {
        return Integer.valueOf(year + "01");
    }

}
