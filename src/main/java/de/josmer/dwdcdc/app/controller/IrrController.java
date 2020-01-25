package de.josmer.dwdcdc.app.controller;

import de.josmer.dwdcdc.app.Application;
import de.josmer.dwdcdc.app.entities.SolIrrExp;
import de.josmer.dwdcdc.app.entities.cache.IrradiationCache;
import de.josmer.dwdcdc.app.interfaces.*;
import de.josmer.dwdcdc.app.requests.IrrRequest;
import de.josmer.dwdcdc.app.spring.AppContext;
import de.josmer.dwdcdc.app.spring.BeanNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DependsOn(BeanNames.APP_CONTEXT)
@RestController
public final class IrrController extends AppController {

    private final IIrradiationCaching irradiationCaching;
    private final ISolIrrExporter solIrrExp;
    private final ISolIrrRepository solIrrRep;

    @Autowired
    public IrrController(IUserRepository userRep, IJwtToken jwtToken, IUserBCrypt userBCrypt, ISolRadRepository solRadRep,
                         ISolIrrExporter solIrrExp, ISolIrrRepository solIrrRep) {
        super(userRep, jwtToken, userBCrypt, solRadRep);
        this.solIrrExp = solIrrExp;
        this.solIrrRep = solIrrRep;
        this.irradiationCaching = AppContext.getIIrradiationCaching();
    }

    @GetMapping("/export_irr")
    public void exportIrr(HttpServletResponse response, @CookieValue("token") final String token,
                          @RequestParam("year") final int year, @RequestParam("lon") final double lon,
                          @RequestParam("lat") final double lat, @RequestParam("ae") final int ae, @RequestParam("ye") final int ye)
            throws Exception {
        LOGGER.info("getBean - export_irr");
        if (!isAccess(token)) {
            return;
        }
        initExcelExport(response, "umrechnung_", getSolIrrExps(new IrrRequest(lat, lon, year, ae, ye)),
                solIrrExp.getProps(), solIrrExp.getHeaders());
    }

    @GetMapping("/irr")
    public List<SolIrrExp> getIrr(@CookieValue("token") final String token, final IrrRequest req) {
        LOGGER.info("getBean - irr");
        if (!isAccess(token) || Application.isDemoMode()) {
            return new ArrayList<>();
        }
        return getSolIrrExps(req);
    }

    private List<SolIrrExp> getItems(IrrRequest req) {
        return solIrrExp
                .getItems(
                        solIrrRep.getIrradiation(
                                solRadRep.findGlobal(getStartDate(req.getYear()), getEndDate(req.getYear()),
                                        req.getLon(), req.getLat()),
                                req.getLon(), req.getLat(), req.getAe(), req.getYe(), req.getYear()),
                        req.getLon(), req.getLat());
    }

    private List<SolIrrExp> getSolIrrExps(final IrrRequest req) {
        if (Application.isDemoMode()) {
            return new ArrayList<>();
        }
        Optional<IIrradiationCache> optionalDbCache = irradiationCaching.get(req);
        if (optionalDbCache.isPresent()) {
            return optionalDbCache.get().getMonths();
        }
        List<SolIrrExp> solIrrExps = getItems(req);
        executeTask(() -> {
            irradiationCaching.add(new IrradiationCache(req.getKey(), solIrrExps));
            LOGGER.info("Create DbCache: " + req.getKey());
        });
        return solIrrExps;
    }
}
