package de.josmer.application.controller;

import de.josmer.application.entities.Export;
import de.josmer.application.interfaces.IExportRepository;
import de.josmer.application.interfaces.IRadiationRepository;
import de.josmer.application.requests.SearchRequest;
import de.josmer.application.security.Authenticator;
import de.josmer.application.security.Key;
import de.josmer.application.utils.Toolbox;
import org.jxls.template.SimpleExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class ApplicationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class.getName());

    @Autowired
    private IExportRepository exportRep;
    @Autowired
    private IRadiationRepository radiationRep;

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String login() {
        return Toolbox.readFile("src/main/resources/static/html/login.html");
    }

    @GetMapping(value = "/app", produces = MediaType.TEXT_HTML_VALUE)
    public String app(@CookieValue("key") final String key) {
        if (key == null || !Key.check(key)) {
            return Toolbox.readFile("src/main/resources/static/html/login.html");
        }
        return Toolbox.readFile("src/main/resources/static/html/app.html");
    }

    @GetMapping(value = "/key", produces = MediaType.TEXT_PLAIN_VALUE)
    public String key(@RequestHeader("login") final String login, @RequestHeader("password") final String password) {
        if (!isValid(login, password)) {
            LOGGER.info("login failes");
            return Key.undefined();
        }
        LOGGER.info("login");
        return Key.get();
    }

    @GetMapping(value = "/count", produces = MediaType.TEXT_PLAIN_VALUE)
    public String count(@CookieValue("key") final String key) {
        if (!Key.check(key)) {
            return "-1";
        }
        return Long.toString(radiationRep.count());
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response, @CookieValue("key") final String key, @RequestParam("startDate") final String startDate, @RequestParam("endDate") final String endDate, @RequestParam("lon") final double lon, @RequestParam("lat") final double lat, @RequestParam("type") final String type) {
        if (!Key.check(key)) {
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
    public List<Export> find(@CookieValue("key") final String key, final SearchRequest req) {
        if (!Key.check(key)) {
            return null;
        }
        return exportRep.getAll(
                radiationRep.find(getDate(req.getStartDate()), getDate(req.getEndDate()), req.getType(),
                        req.getLon(), req.getLat()), req.getLon(), req.getLat());
    }

    protected boolean isValid(final String login, final String password) {
        return new Authenticator().authenticate(login, password);
    }

    private int getDate(final String date) {
        try {
            return Integer.valueOf(date.replace("-", "").replace("#", ""));
        } catch (NumberFormatException e) {
            LOGGER.info(e.getMessage());
            return 0;
        }
    }
}
