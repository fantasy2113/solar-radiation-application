package org.josmer.application.controller;

import org.josmer.application.entities.Export;
import org.josmer.application.interfaces.IExportRepository;
import org.josmer.application.interfaces.IRadiationRepository;
import org.josmer.application.requests.SearchRequest;
import org.josmer.application.security.Authenticator;
import org.josmer.application.security.Key;
import org.josmer.application.utils.Toolbox;
import org.jxls.template.SimpleExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@RestController
public class ApplicationController {

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
            return Key.undefined();
        }
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
            System.out.println(e.getMessage());
        }
    }

    @GetMapping("/find")
    public List<Export> find(@CookieValue("key") final String key, final SearchRequest req) {
        if (!Key.check(key)) {
            return new LinkedList<>();
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
            System.out.println(e.getMessage());
            return 0;
        }
    }
}
