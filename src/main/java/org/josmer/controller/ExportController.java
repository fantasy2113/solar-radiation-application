package org.josmer.controller;

import org.josmer.interfaces.IExportRepository;
import org.josmer.interfaces.IRadiationRepository;
import org.josmer.security.Key;
import org.jxls.template.SimpleExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public void export(HttpServletResponse response, @CookieValue("key") final String key, @RequestParam(name = "startDate") final int startDate, @RequestParam(name = "endDate") final int endDate, @RequestParam(name = "typ") final String typ, @RequestParam(name = "lon") final double lon, @RequestParam(name = "lat") final double lat) {
        if (!Key.check(key)) {
            return;
        }
        try {
            response.addHeader("Content-disposition", "attachment; filename=People.xls");
            response.setContentType("application/vnd.ms-excel");
            new SimpleExporter().gridExport(
                    exportRepository.getHeaders(),
                    exportRepository.getAll(radiationRepository.find(startDate, endDate, typ, lon, lat), lon, lat),
                    "firstName, lastName, ",
                    response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
        }
    }
}
