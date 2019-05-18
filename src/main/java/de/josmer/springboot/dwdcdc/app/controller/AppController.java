package de.josmer.springboot.dwdcdc.app.controller;

import de.josmer.springboot.dwdcdc.app.controller.requests.IrrRequest;
import de.josmer.springboot.dwdcdc.app.controller.requests.RadRequest;
import de.josmer.springboot.dwdcdc.app.entities.SolIrrExp;
import de.josmer.springboot.dwdcdc.app.entities.SolRadExp;
import de.josmer.springboot.dwdcdc.app.interfaces.*;
import org.jxls.template.SimpleExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public final class AppController extends Controller {
    private final ISolRadExporter solRadExp;
    private final ISolIrrExporter solIrrExp;
    private final ISolRadRepository solRadRep;
    private final ISolIrrRepository solIrrRep;

    @Autowired
    public AppController(IUserRepository userRep, IJwtToken jwtToken, IUserBCrypt userBCrypt, ISolRadExporter solRadExp, ISolIrrExporter solIrrExp, ISolRadRepository solRadRep, ISolIrrRepository solIrrRep) {
        super(userRep, jwtToken, userBCrypt);
        this.solRadExp = solRadExp;
        this.solIrrExp = solIrrExp;
        this.solRadRep = solRadRep;
        this.solIrrRep = solIrrRep;
    }

    @GetMapping(value = "/number_of_rad", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getNumberOfRad(@CookieValue("token") final String token) {
        if (!isAccess(token)) {
            return "0";
        }
        return Long.toString(solRadRep.getNumberOfRadiations());
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
                    solRadExp.getItems(solRadRep.find(getDate(startDate), getDate(endDate), type, lon, lat), lon, lat),
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
                    solIrrExp.getItems(solIrrRep.getIrradiation(solRadRep.findGlobal(getStartDate(year), getEndDate(year), lon, lat), lon, lat, ae, ye, year), lon, lat),
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
        return solRadExp.getItems(solRadRep.find(getDate(req.getStartDate()), getDate(req.getEndDate()), req.getType(), req.getLon(), req.getLat()), req.getLon(), req.getLat());
    }

    @GetMapping("/irr")
    public List<SolIrrExp> getIrr(@CookieValue("token") final String token, final IrrRequest req) {
        if (!isAccess(token)) {
            return new ArrayList<>();
        }
        return solIrrExp.getItems(solIrrRep.getIrradiation(solRadRep.findGlobal(getStartDate(req.getYear()), getEndDate(req.getYear()), req.getLon(), req.getLat()), req.getLon(), req.getLat(), req.getAe(), req.getYe(), req.getYear()), req.getLon(), req.getLat());
    }

    private int getDate(final String date) {
        try {
            return Integer.valueOf(date.replace("-", "").replace("#", ""));
        } catch (NumberFormatException e) {
            LOGGER.info(e.getMessage());
            return 0;
        }
    }

    private Integer getEndDate(int year) {
        return Integer.valueOf(year + "12");
    }

    private Integer getStartDate(int year) {
        return Integer.valueOf(year + "01");
    }

}
