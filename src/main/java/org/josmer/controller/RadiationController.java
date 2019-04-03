package org.josmer.controller;

import org.josmer.entities.Radiation;
import org.josmer.interfaces.IRadiationRepository;
import org.josmer.security.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/radiation")
public final class RadiationController {
    @Autowired
    IRadiationRepository radiationRepository;

    @ResponseBody
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public List<Radiation> find(@CookieValue("key") final String key, @RequestParam(name = "startDate") final int startDate, @RequestParam(name = "endDate") final int endDate, @RequestParam(name = "typ") final String typ, @RequestParam(name = "lon") final double lon, @RequestParam(name = "lat") final double lat) {
        if (!Key.check(key)) {
            return null;
        }
        return radiationRepository.find(startDate, endDate, typ, lon, lat);
    }

}
