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
import java.util.List;

@RestController
@RequestMapping("/")
public class ApplicationController {
    @Autowired
    private IExportRepository exportRepository;
    @Autowired
    private IRadiationRepository radiationRepository;

    @GetMapping(value = "/app", produces = MediaType.TEXT_HTML_VALUE)
    public String app(@CookieValue("key") final String key) {
        if (!Key.check(key)) {
            return Toolbox.readFile("src/main/resources/static/html/accessDenied.html");
        }
        return Toolbox.readFile("src/main/resources/static/html/app.html");
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String login() {
        return Toolbox.readFile("src/main/resources/static/html/login.html");
    }

    @GetMapping(value = "/key", produces = MediaType.TEXT_PLAIN_VALUE)
    public String key(@RequestHeader("login") final String login, @RequestHeader("password") final String password) {
        if (!isValid(login, password)) {
            return Key.undefined();
        }
        return Key.get();
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response, @CookieValue("key") final String key, @RequestParam("startDate") final int startDate, @RequestParam("endDate") final int endDate, @RequestParam("typ") final String typ, @RequestParam("lon") final double lon, @RequestParam("lat") final double lat) {
        if (!Key.check(key)) {
            return;
        }
        try {
            response.addHeader("Content-disposition", "attachment; filename=einstrahlung.xls");
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

    @GetMapping("/find")
    public List<Export> find(@CookieValue("key") final String key, final SearchRequest searchRequest) {
        if (!Key.check(key)) {
            return null;
        }
        return exportRepository.getAll(radiationRepository.find(searchRequest.getStartDate(), searchRequest.getEndDate(), searchRequest.getTyp(),
                searchRequest.getLon(), searchRequest.getLat()), searchRequest.getLon(), searchRequest.getLat());
    }

    protected boolean isValid(final String login, final String password) {
        return new Authenticator().authenticate(login, password);
    }

}
