package org.josmer.app.controller;

import org.josmer.app.core.IExportRepository;
import org.josmer.app.core.IRadiationRepository;
import org.josmer.app.entity.Export;
import org.josmer.app.logic.security.Authenticator;
import org.josmer.app.logic.security.Key;
import org.josmer.app.logic.utils.Toolbox;
import org.jxls.template.SimpleExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class ApplicationController {
    @Autowired
    private IExportRepository exportRepository;
    @Autowired
    private IRadiationRepository radiationRepository;

    @RequestMapping(value = "/app", method = RequestMethod.GET)
    public String app(@CookieValue("key") final String key) {
        if (!Key.check(key)) {
            return Toolbox.readFile("src/main/resources/static/html/accessDenied.html");
        }
        return Toolbox.readFile("src/main/resources/static/html/app.html");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return Toolbox.readFile("src/main/resources/static/html/index.html");
    }

    @RequestMapping(value = "/key", method = RequestMethod.GET)
    public String key(@RequestHeader(value = "login") final String login, @RequestHeader(value = "password") final String password) {
        if (!isValid(login, password)) {
            return Key.undefined();
        }
        return Key.get();
    }

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void export(HttpServletResponse response, @CookieValue("key") final String key, @RequestParam("startDate") final int startDate, @RequestParam("endDate") final int endDate, @RequestParam("typ") final String typ, @RequestParam("lon") final double lon, @RequestParam("lat") final double lat) {
        if (!Key.check(key)) {
            return;
        }
        try {
            response.addHeader("Content-disposition", "attachment; filename=People.xls");
            response.setContentType("application/vnd.ms-excel");
            new SimpleExporter().gridExport(
                    exportRepository.getHeaders(),
                    exportRepository.getAll(radiationRepository.find(startDate, endDate, typ, lon, lat), lon, lat),
                    exportRepository.getProps(),
                    response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
        }
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Export find(@CookieValue("key") final String key, SearchRequest searchRequest) {
        if (!Key.check(key)) {
            return null;
        }
        return new Export();/*(exportRepository.getAll(radiationRepository.find(searchRequest.getStartDate(), searchRequest.getEndDate(),
                searchRequest.getTyp(), searchRequest.getLon(), searchRequest.getLat()), searchRequest.getLon(), searchRequest.getLat());*/
    }

    protected boolean isValid(final String login, final String password) {
        return new Authenticator().authenticate(login, password);
    }

}
