package de.josmer.dwdcdc.app.controller;

import de.josmer.dwdcdc.app.controller.requests.IrrRequest;
import de.josmer.dwdcdc.app.controller.requests.RadRequest;
import de.josmer.dwdcdc.app.entities.SolIrrExp;
import de.josmer.dwdcdc.app.entities.SolRadExp;
import de.josmer.dwdcdc.app.interfaces.*;
import org.jxls.template.SimpleExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public final class AppController extends Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class.getName());
    private final ISolRadExporter solRadExp;
    private final ISolIrrExporter solIrrExp;
    private final ISolRadRepository solRadRep;
    private final ISolIrrRepository solIrrRep;

    @Autowired
    public AppController(IUserRepository userRep, IJwtToken jwtToken, IUserBCrypt userBCrypt, ISolRadExporter solRadExp,
                         ISolIrrExporter solIrrExp, ISolRadRepository solRadRep, ISolIrrRepository solIrrRep) {
        super(userRep, jwtToken, userBCrypt);
        this.solRadExp = solRadExp;
        this.solIrrExp = solIrrExp;
        this.solRadRep = solRadRep;
        this.solIrrRep = solIrrRep;
    }

    @GetMapping(value = "/number_of_rad", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getNumberOfRad(@CookieValue("token") final String token) {
        LOGGER.info("get - number_of_rad");
        if (!isAccess(token)) {
            return "0";
        }
        return Long.toString(solRadRep.getNumberOfRadiations());
    }

    @GetMapping("/export_rad")
    public void exportRad(HttpServletResponse response, @CookieValue("token") final String token, @RequestParam("startDate") final String startDate,
                          @RequestParam("endDate") final String endDate, @RequestParam("lon") final double lon, @RequestParam("lat") final double lat,
                          @RequestParam("type") final String type) throws Exception {
        LOGGER.info("get - export_rad");
        if (!isAccess(token)) {
            return;
        }
        final List<SolRadExp> items = solRadExp.getItems(solRadRep.find(getDate(startDate), getDate(endDate), getSolRadTypes(type), lon, lat), lon, lat);
        initExcelExport(response, "sonneneinstrahlung_", items, solRadExp.getProps(), solRadExp.getHeaders());
    }

    @GetMapping("/export_irr")
    public void exportIrr(HttpServletResponse response, @CookieValue("token") final String token, @RequestParam("year") final int year,
                          @RequestParam("lon") final double lon, @RequestParam("lat") final double lat, @RequestParam("ae") final int ae,
                          @RequestParam("ye") final int ye) throws Exception {
        LOGGER.info("get - export_irr");
        if (!isAccess(token)) {
            return;
        }
        final List<SolIrrExp> items = solIrrExp.getItems(solIrrRep.getIrradiation(solRadRep.findGlobal(getStartDate(year), getEndDate(year), lon, lat),
                lon, lat, ae, ye, year), lon, lat);
        initExcelExport(response, "umrechnung_", items, solIrrExp.getProps(), solIrrExp.getHeaders());
    }

    @GetMapping("/rad")
    public List<SolRadExp> getRad(@CookieValue("token") final String token, final RadRequest req) {
        LOGGER.info("get - rad");
        if (!isAccess(token)) {
            return new ArrayList<>();
        }
        return solRadExp.getItems(solRadRep.find(getDate(req.getStartDate()), getDate(req.getEndDate()), getSolRadTypes(req.getType()), req.getLon(),
                req.getLat()), req.getLon(), req.getLat());
    }

    @GetMapping("/irr")
    public List<SolIrrExp> getIrr(@CookieValue("token") final String token, final IrrRequest req) {
        LOGGER.info("get - irr");
        if (!isAccess(token)) {
            return new ArrayList<>();
        }
        return solIrrExp.getItems(
                solIrrRep.getIrradiation(
                        solRadRep.findGlobal(getStartDate(req.getYear()), getEndDate(req.getYear()), req.getLon(),
                                req.getLat()), req.getLon(), req.getLat(), req.getAe(), req.getYe(), req.getYear()), req.getLon(), req.getLat());
    }

    private void initExcelExport(HttpServletResponse response, String exportName, List<?> items, String props, List<String> headers) throws Exception {
        response.addHeader("Content-disposition", "attachment; filename=" + exportName + System.currentTimeMillis() + ".xls");
        response.setContentType("application/vnd.ms-excel");
        new SimpleExporter().gridExport(headers, items, props, response.getOutputStream());
        response.flushBuffer();
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
