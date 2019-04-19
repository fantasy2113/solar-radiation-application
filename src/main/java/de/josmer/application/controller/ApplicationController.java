package de.josmer.application.controller;

import de.josmer.application.controller.requests.CalculationRequest;
import de.josmer.application.controller.requests.RadiationRequest;
import de.josmer.application.entities.Calculated;
import de.josmer.application.entities.ExportCalc;
import de.josmer.application.entities.ExportRadi;
import de.josmer.application.entities.User;
import de.josmer.application.library.geo.GaussKrueger;
import de.josmer.application.library.interfaces.*;
import de.josmer.application.library.security.Token;
import de.josmer.application.library.utils.Toolbox;
import org.jxls.template.SimpleExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ApplicationController extends AController {

    @Autowired
    public ApplicationController(IExportRadiRepository exportRadiRepo, IExportCalcRepository exportCalcRepo, IRadiationRepository radiRepo, IUserRepository userRepo, ICalculatedRepository calcRepo) {
        super(exportRadiRepo, exportCalcRepo, radiRepo, userRepo, calcRepo);
    }

    @GetMapping(value = "/save_user", produces = MediaType.TEXT_HTML_VALUE)
    public String saveUser(@RequestHeader("login") final String login, @RequestHeader("password") final String password) {
        if (isParameter(login, password)) {
            return "Benutzername oder Passwort sind nicht lang genug!";
        }
        if (userRepo.get(login).isPresent()) {
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
    public String getToken(@RequestHeader("login") final String login, @RequestHeader("password") final String password) {
        final Optional<User> optionalUser = userRepo.get(login);
        if (optionalUser.isPresent() && Toolbox.isPassword(password, optionalUser.get().getPassword())) {
            LOGGER.info("login successful");
            return Token.get(optionalUser.get().getId());
        }
        LOGGER.info("login failed");
        return "";
    }

    @GetMapping(value = "/count", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getCount(@CookieValue("token") final String token) {
        if (!isAccess(Token.getAuthentication(token))) {
            return "-1";
        }
        return Long.toString(radiRepo.count());
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
                    exportRadiRepo.getHeaders(),
                    exportRadiRepo.getAll(radiRepo.find(new GaussKrueger(), getDate(startDate), getDate(endDate), type, lon, lat), lon, lat),
                    exportRadiRepo.getProps(),
                    response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
    }

    @GetMapping("/radiation")
    public List<ExportRadi> getRadiation(@CookieValue("token") final String token, final RadiationRequest req) {
        if (!isAccess(Token.getAuthentication(token))) {
            return new ArrayList<>();
        }
        return exportRadiRepo.getAll(radiRepo.find(new GaussKrueger(), getDate(req.getStartDate()), getDate(req.getEndDate()),
                req.getType(), req.getLon(), req.getLat()), req.getLon(), req.getLat());
    }

    @GetMapping("/calculation")
    public List<ExportCalc> getCalculation(@CookieValue("token") final String token, final CalculationRequest req) {
        if (!isAccess(Token.getAuthentication(token))) {
            return new ArrayList<>();
        }
        final int startDate = Integer.valueOf(req.getYear() + "01");
        final int endDate = Integer.valueOf(req.getYear() + "12");

        final double[] eGlobHor = radiRepo.findGlobal(new GaussKrueger(), startDate, endDate, req.getLon(), req.getLat());
        List<Calculated> calculateds = calcRepo.calculateds(eGlobHor, req.getLon(), req.getLat(), req.getAe(), req.getYe(), req.getYear());

        return exportCalcRepo.getAll(calculateds, req.getLon(), req.getLat());
    }


}
