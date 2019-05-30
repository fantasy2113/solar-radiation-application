package de.josmer.dwdcdc.app.controller;

import de.josmer.dwdcdc.app.controller.requests.IrrRequest;
import de.josmer.dwdcdc.app.entities.SolIrrExp;
import de.josmer.dwdcdc.app.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
public class IrrController extends AppController {

    private final ISolIrrExporter solIrrExp;
    private final ISolIrrRepository solIrrRep;

    @Autowired
    public IrrController(IUserRepository userRep, IJwtToken jwtToken, IUserBCrypt userBCrypt, ISolRadRepository solRadRep, ISolIrrExporter solIrrExp, ISolIrrRepository solIrrRep) {
        super(userRep, jwtToken, userBCrypt, solRadRep);
        this.solIrrExp = solIrrExp;
        this.solIrrRep = solIrrRep;
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

    @GetMapping("/export_irr")
    public void exportIrr(HttpServletResponse response, @CookieValue("token") final String token, @RequestParam("year") final int year,
                          @RequestParam("lon") final double lon, @RequestParam("lat") final double lat, @RequestParam("ae") final int ae,
                          @RequestParam("ye") final int ye) throws Exception {
        LOGGER.info("get - export_irr");
        if (!isAccess(token)) {
            return;
        }
        final LinkedList<SolIrrExp> items = solIrrExp.getItems(solIrrRep.getIrradiation(solRadRep.findGlobal(getStartDate(year), getEndDate(year), lon, lat),
                lon, lat, ae, ye, year), lon, lat);
        initExcelExport(response, "umrechnung_", items, solIrrExp.getProps(), solIrrExp.getHeaders());
    }
}
