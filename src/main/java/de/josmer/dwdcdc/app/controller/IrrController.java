package de.josmer.dwdcdc.app.controller;

import de.josmer.dwdcdc.app.controller.requests.IrrRequest;
import de.josmer.dwdcdc.app.entities.SolIrrExp;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class IrrController extends AppController {

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
}
