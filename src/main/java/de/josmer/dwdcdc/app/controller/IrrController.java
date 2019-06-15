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
import java.util.LinkedList;
import java.util.Optional;

@RestController
public final class IrrController extends AppController {

    private final ISolIrrExporter solIrrExp;
    private final ISolIrrRepository solIrrRep;
    private final ISolIrrExpCache solIrrExpCache;

    @Autowired
    public IrrController(IUserRepository userRep, IJwtToken jwtToken, IUserBCrypt userBCrypt, ISolRadRepository solRadRep,
                         ISolIrrExporter solIrrExp, ISolIrrRepository solIrrRep, ISolIrrExpCache solIrrExpCache) {
        super(userRep, jwtToken, userBCrypt, solRadRep);
        this.solIrrExp = solIrrExp;
        this.solIrrRep = solIrrRep;
        this.solIrrExpCache = solIrrExpCache;
    }

    @GetMapping("/irr")
    public LinkedList<SolIrrExp> getIrr(@CookieValue("token") final String token, final IrrRequest req) {
        LOGGER.info("getBean - irr");
        if (!isAccess(token)) {
            return new LinkedList<>();
        }
        return getSolIrrExps(req);
    }

    @GetMapping("/export_irr")
    public void exportIrr(HttpServletResponse response, @CookieValue("token") final String token, @RequestParam("year") final int year,
                          @RequestParam("lon") final double lon, @RequestParam("lat") final double lat, @RequestParam("ae") final int ae,
                          @RequestParam("ye") final int ye) throws Exception {
        LOGGER.info("getBean - export_irr");
        if (!isAccess(token)) {
            return;
        }
        initExcelExport(response, "umrechnung_", getSolIrrExps(new IrrRequest(lat, lon, year, ae, ye)), solIrrExp.getProps(), solIrrExp.getHeaders());
    }

    private LinkedList<SolIrrExp> getSolIrrExps(final IrrRequest req) {
        Optional<LinkedList<SolIrrExp>> optionalSolIrrExps = solIrrExpCache.get(req);
        if (optionalSolIrrExps.isPresent()) {
            return optionalSolIrrExps.get();
        }
        LinkedList<SolIrrExp> solIrrExps = solIrrExp.getItems(
                solIrrRep.getIrradiation(
                        solRadRep.findGlobal(getStartDate(req.getYear()), getEndDate(req.getYear()), req.getLon(),
                                req.getLat()), req.getLon(), req.getLat(), req.getAe(), req.getYe(), req.getYear()), req.getLon(), req.getLat());
        solIrrExpCache.add(req, solIrrExps);
        return solIrrExps;
    }
}
