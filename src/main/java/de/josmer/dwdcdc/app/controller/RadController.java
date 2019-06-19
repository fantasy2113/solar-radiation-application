package de.josmer.dwdcdc.app.controller;

import de.josmer.dwdcdc.app.base.entities.SolRadExp;
import de.josmer.dwdcdc.app.base.entities.web.WebInfo;
import de.josmer.dwdcdc.app.base.interfaces.*;
import de.josmer.dwdcdc.app.requests.RadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;

@RestController
public final class RadController extends AppController {
    private final ISolRadExporter solRadExp;

    @Autowired
    public RadController(IUserRepository userRep, IJwtToken jwtToken, IUserBCrypt userBCrypt, ISolRadRepository solRadRep, ISolRadExporter solRadExp) {
        super(userRep, jwtToken, userBCrypt, solRadRep);
        this.solRadExp = solRadExp;
    }

    @GetMapping("/export_rad")
    public void exportRad(HttpServletResponse response, @CookieValue("token") final String token, @RequestParam("startDate") final String startDate,
                          @RequestParam("endDate") final String endDate, @RequestParam("lon") final double lon, @RequestParam("lat") final double lat,
                          @RequestParam("type") final String type) throws Exception {
        LOGGER.info("getBean - export_rad");
        if (!isAccess(token)) {
            return;
        }
        initExcelExport(response, "sonneneinstrahlung_", getSolRadExps(new RadRequest(lat, lon, startDate, endDate, type)), solRadExp.getProps(), solRadExp.getHeaders());
    }

    @GetMapping("/rad")
    public LinkedList<SolRadExp> getRad(@CookieValue("token") final String token, final RadRequest req) {
        LOGGER.info("getBean - rad");
        if (!isAccess(token)) {
            return new LinkedList<>();
        }
        return getSolRadExps(req);
    }

    private LinkedList<SolRadExp> getSolRadExps(RadRequest req) {
        return solRadExp.getItems(solRadRep.find(getDate(req.getStartDate()), getDate(req.getEndDate()), getSolRadTypes(req.getType()), req.getLon(),
                req.getLat()), req.getLon(), req.getLat());
    }

    @GetMapping(value = "/number_of_rad")
    public WebInfo getNumberOfRad(@CookieValue("token") final String token) {
        WebInfo webInfo = new WebInfo();
        LOGGER.info("getBean - number_of_rad");
        if (!isAccess(token)) {
            return webInfo;
        }
        webInfo.setNumberOfRad(solRadRep.getNumberOfRadiations());
        return webInfo;
    }
}
