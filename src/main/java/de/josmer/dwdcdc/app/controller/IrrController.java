package de.josmer.dwdcdc.app.controller;

import java.util.LinkedList;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.josmer.dwdcdc.app.entities.SolIrrExp;
import de.josmer.dwdcdc.app.entities.cache.IrradiationCache;
import de.josmer.dwdcdc.app.interfaces.IIrradiationCache;
import de.josmer.dwdcdc.app.interfaces.IIrradiationCaching;
import de.josmer.dwdcdc.app.interfaces.IJwtToken;
import de.josmer.dwdcdc.app.interfaces.ISolIrrExporter;
import de.josmer.dwdcdc.app.interfaces.ISolIrrRepository;
import de.josmer.dwdcdc.app.interfaces.ISolRadRepository;
import de.josmer.dwdcdc.app.interfaces.IUserBCrypt;
import de.josmer.dwdcdc.app.interfaces.IUserRepository;
import de.josmer.dwdcdc.app.requests.IrrRequest;

@RestController
public final class IrrController extends AppController {

	private final ISolIrrExporter solIrrExp;
	private final ISolIrrRepository solIrrRep;
	private final IIrradiationCaching irradiationCaching;

	@Autowired
	public IrrController(IUserRepository userRep, IJwtToken jwtToken, IUserBCrypt userBCrypt,
			ISolRadRepository solRadRep, ISolIrrExporter solIrrExp, ISolIrrRepository solIrrRep,
			@Qualifier("IrradiationRamCaching") IIrradiationCaching irradiationCaching) {
		super(userRep, jwtToken, userBCrypt, solRadRep);
		this.solIrrExp = solIrrExp;
		this.solIrrRep = solIrrRep;
		this.irradiationCaching = irradiationCaching;
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

	private LinkedList<SolIrrExp> getSolIrrExps(final IrrRequest req) {
		Optional<IIrradiationCache> optionalDbCache = irradiationCaching.get(req);
		if (optionalDbCache.isPresent()) {
			return optionalDbCache.get().getMonths();
		}
		LinkedList<SolIrrExp> solIrrExps = getItems(req);
		executeTask(() -> {
			irradiationCaching.add(new IrradiationCache(req.getKey(), solIrrExps));
			LOGGER.info("Create DbCache: " + req.getKey());
		});
		return solIrrExps;
	}

	private LinkedList<SolIrrExp> getItems(IrrRequest req) {
		return solIrrExp
				.getItems(
						solIrrRep.getIrradiation(
								solRadRep.findGlobal(getStartDate(req.getYear()), getEndDate(req.getYear()),
										req.getLon(), req.getLat()),
								req.getLon(), req.getLat(), req.getAe(), req.getYe(), req.getYear()),
						req.getLon(), req.getLat());
	}
}
