package de.josmer.dwdcdc.app.controller;

import java.util.LinkedList;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.josmer.dwdcdc.app.entities.SolRadExp;
import de.josmer.dwdcdc.app.entities.web.WebInfo;
import de.josmer.dwdcdc.app.interfaces.IJwtToken;
import de.josmer.dwdcdc.app.interfaces.ISolRadExporter;
import de.josmer.dwdcdc.app.interfaces.ISolRadRepository;
import de.josmer.dwdcdc.app.interfaces.IUserBCrypt;
import de.josmer.dwdcdc.app.interfaces.IUserRepository;
import de.josmer.dwdcdc.app.requests.RadRequest;

@RestController
public final class RadController extends AppController {

	private final ISolRadExporter solRadExp;

	@Autowired
	public RadController(IUserRepository userRep, IJwtToken jwtToken, IUserBCrypt userBCrypt,
			ISolRadRepository solRadRep, ISolRadExporter solRadExp) {
		super(userRep, jwtToken, userBCrypt, solRadRep);
		this.solRadExp = solRadExp;
	}

	@GetMapping("/export_rad")
	public void exportRad(HttpServletResponse response, @CookieValue("token") final String token,
			@RequestParam("startDate") final String startDate, @RequestParam("endDate") final String endDate,
			@RequestParam("lon") final double lon, @RequestParam("lat") final double lat,
			@RequestParam("type") final String type) throws Exception {
		LOGGER.info("getBean - export_rad");
		if (!isAccess(token)) {
			return;
		}
		initExcelExport(response, "sonneneinstrahlung_",
				getSolRadExps(new RadRequest(lat, lon, startDate, endDate, type)), solRadExp.getProps(),
				solRadExp.getHeaders());
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

	@GetMapping("/rad")
	public LinkedList<SolRadExp> getRad(@CookieValue("token") final String token, final RadRequest req) {
		LOGGER.info("getBean - rad");
		if (!isAccess(token)) {
			return new LinkedList<>();
		}
		return getSolRadExps(req);
	}

	private LinkedList<SolRadExp> getSolRadExps(RadRequest req) {
		return solRadExp.getItems(solRadRep.find(getDate(req.getStartDate()), getDate(req.getEndDate()),
				getSolRadTypes(req.getType()), req.getLon(), req.getLat()), req.getLon(), req.getLat());
	}
}
