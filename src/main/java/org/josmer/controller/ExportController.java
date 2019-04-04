package org.josmer.controller;

import org.josmer.interfaces.IExportRepository;
import org.josmer.interfaces.IRadiationRepository;
import org.josmer.security.Key;
import org.jxls.template.SimpleExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/export")
public class ExportController {
    @Autowired
    private IExportRepository exportRepository;
    @Autowired
    private IRadiationRepository radiationRepository;

    @RequestMapping(method = RequestMethod.GET)
    public void export(HttpServletResponse response, @CookieValue("key") final String key) {
        if (!Key.check(key)) {
            return;
        }
        try {
            response.addHeader("Content-disposition", "attachment; filename=People.xls");
            response.setContentType("application/vnd.ms-excel");
            new SimpleExporter().gridExport(
                    exportRepository.getHeaders(),
                    exportRepository.getAll(radiationRepository.find(0, 0, "", 0, 0), 0, 0),
                    "firstName, lastName, ",
                    response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
        }
    }
}
